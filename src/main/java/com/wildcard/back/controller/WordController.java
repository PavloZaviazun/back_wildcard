package com.wildcard.back.controller;

import com.wildcard.back.dao.LibDAO;
import com.wildcard.back.dao.UserDAO;
import com.wildcard.back.dao.WordDAO;
import com.wildcard.back.models.Lib;
import com.wildcard.back.models.Translation;
import com.wildcard.back.models.User;
import com.wildcard.back.service.MainService;
import com.wildcard.back.util.Constants;
import com.wildcard.back.util.PartOfSpeech;
import com.wildcard.back.models.Word;
import com.wildcard.back.service.QueryService;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class WordController {
    private LibDAO libDAO;
    private WordDAO wordDAO;
    private UserDAO userDAO;
    private EntityManager entityManager;

    @GetMapping("/searchByWord/{word}/page/{page}")
    public Page<Word> searchWord(@PathVariable String word,
                                 @PathVariable int page) {
        return wordDAO.searchByWord(word.toLowerCase(), PageRequest.of(page, 20));
    }

    @GetMapping("/searchByLetter/{letter}/page/{page}")
    public Page<Word> searchByLetter(@PathVariable String letter,
                                     @PathVariable int page) {
        return wordDAO.searchByLetter(letter, PageRequest.of(page, 2));
    }

    @GetMapping("/partsOfSpeech/{word}")
    public String[] getPartsOfSpeech(@PathVariable String word) {
        List<Word> list = null;
        String wordRequest = Validation.wordValidation(word);
        if (wordRequest != null) {
            list = wordDAO.findByWord(wordRequest);
        }
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).getPartOfSpeech().toString();
        }
        return array;
    }

    @GetMapping("/lib/{id}/words/get/page/{page}")
    public PagedListHolder<Word> getLibWords(@PathVariable int id,
                                             @PathVariable int page) {
        List<Integer> resultList = new QueryService(entityManager).selectWordsId(id);
        List<Word> list = new ArrayList<>();
        for (Integer el : resultList) {
            if (wordDAO.findById(el).isPresent()) {
                list.add(wordDAO.findById(el).get());
            }
        }
        PagedListHolder<Word> pageHolder = new PagedListHolder<>(list);
        pageHolder.setPageSize(3);
        pageHolder.setPage(page);
        return pageHolder;
    }

    @GetMapping("/lib/{name}/words/get/{shuffleFlag}")
    public List<Word> getLibALLWords(@PathVariable String name,
                                     @PathVariable int shuffleFlag) {
        Lib lib = libDAO.findLib(name);
        List<Integer> resultList = new QueryService(entityManager).selectWordsId(lib.getId());
        List<Word> list = new ArrayList<>();
        for (Integer el : resultList) {
            if (wordDAO.findById(el).isPresent()) {
                list.add(wordDAO.findById(el).get());
            }
        }
        if(shuffleFlag > 0) {
            Collections.shuffle(list, new Random(shuffleFlag));
            return list;
        }
        return list;
    }

    @GetMapping("/word/{id}/get")
    public Word getWord(@PathVariable int id) {
        if (wordDAO.findById(id).isPresent()) {
            return wordDAO.findById(id).get();
        }
        return new Word();
    }

    @DeleteMapping("/word/{id}/delete")
    public void deleteWord(@PathVariable int id) {
        wordDAO.deleteById(id);
    }

    @PostMapping("/user/customlib/word/{id}/update")
    public String updateWordInCustLib(@PathVariable int id,
                                      @RequestParam String word,
                                      @RequestParam String partOfSpeech,
                                      @RequestParam String description,
                                      @RequestParam String example,
                                      @RequestParam String translationRu,
                                      @RequestParam String translationUa){
        Word wordObj = wordDAO.getOne(id);
        boolean wasUpdated = false;

        if (!wordObj.getWord().equals(word)) {
            String wordRequest = null;
            if (word != null && !word.isEmpty()) wordRequest = Validation.wordValidation(word);
            if (wordRequest == null) return Constants.WORD_DOESNT_FIT;
            wordObj.setWord(wordRequest);
            wasUpdated = true;
        }

        if (!wordObj.getPartOfSpeech().equals(PartOfSpeech.valueOf(partOfSpeech.toUpperCase()))) {
            PartOfSpeech partOfSpeechRequest = Validation.partOfSpeechValidation(partOfSpeech.toUpperCase());
            if (partOfSpeechRequest == null) return Constants.PART_OF_SPEECH_DOESNT_FIT;
            wordObj.setPartOfSpeech(partOfSpeechRequest);
            wasUpdated = true;
        }

        if (!wordObj.getDescription().equals(description)) {
            String descriptionRequest = null;
            if (description != null && !description.isEmpty())
                descriptionRequest = Validation.sentenceValidation(description);
            if (descriptionRequest == null) return Constants.DESCRIPTION_DOESNT_FIT;
            wordObj.setDescription(descriptionRequest);
            wasUpdated = true;
        }

        if (!wordObj.getExample().equals(example)) {
            String exampleRequest = null;
            if (example != null && !example.isEmpty()) exampleRequest = Validation.sentenceValidation(example);
            if (exampleRequest == null) return Constants.EXAMPLE_DOESNT_FIT;
            wordObj.setExample(exampleRequest);
            wasUpdated = true;

        }

        Translation translationObj = new Translation(translationRu, translationUa);
        String translation = translationObj.toString();

        if (!wordObj.getTranslation().equals(translation)) {
            String translationRequest = null;
            if (translation != null && !translation.isEmpty())
                translationRequest = Validation.oneStepValidation(translation, Constants.JSON_PATTERN);
            if (translationRequest == null) return Constants.TRANSLATION_DOESNT_FIT;
            wordObj.setTranslation(translationRequest);
            wasUpdated = true;
        }

        if (wasUpdated) {
            wordDAO.save(wordObj);
            return Constants.WORD_UPDATE_SUCCESS;
        }
        return Constants.WORD_UPDATE_UNSUCCESS;
    }

    @PatchMapping("/word/{id}/update")
    public String updateWord(@PathVariable int id,
                             @RequestParam String word,
                             @RequestParam(value = "approved", required = false) String checkboxValue,
                             @RequestParam String partOfSpeech,
                             @RequestParam String description,
                             @RequestParam String example,
                             @RequestParam String translationRu,
                             @RequestParam String translationUa,
                             @RequestParam MultipartFile image) {
        Word wordObj = wordDAO.getOne(id);
        boolean wasUpdated = false;

        if (!wordObj.getWord().equals(word)) {
            String wordRequest = null;
            if (word != null && !word.isEmpty()) wordRequest = Validation.wordValidation(word);
            if (wordRequest == null) return Constants.WORD_DOESNT_FIT;
            wordObj.setWord(wordRequest);
            wasUpdated = true;
        }

        if (!wordObj.getPartOfSpeech().equals(PartOfSpeech.valueOf(partOfSpeech.toUpperCase()))) {
            PartOfSpeech partOfSpeechRequest = Validation.partOfSpeechValidation(partOfSpeech.toUpperCase());
            if (partOfSpeechRequest == null) return Constants.PART_OF_SPEECH_DOESNT_FIT;
            wordObj.setPartOfSpeech(partOfSpeechRequest);
            wasUpdated = true;
        }

        if (!wordObj.getDescription().equals(description)) {
            String descriptionRequest = null;
            if (description != null && !description.isEmpty())
                descriptionRequest = Validation.sentenceValidation(description);
            if (descriptionRequest == null) return Constants.DESCRIPTION_DOESNT_FIT;
            wordObj.setDescription(descriptionRequest);
            wasUpdated = true;
        }

        if (!wordObj.getExample().equals(example)) {
            String exampleRequest = null;
            if (example != null && !example.isEmpty()) exampleRequest = Validation.sentenceValidation(example);
            if (exampleRequest == null) return Constants.EXAMPLE_DOESNT_FIT;
            wordObj.setExample(exampleRequest);
            wasUpdated = true;

        }

        Translation translationObj = new Translation(translationRu, translationUa);
        String translation = translationObj.toString();

        if (!wordObj.getTranslation().equals(translation)) {
            String translationRequest = null;
            if (translation != null && !translation.isEmpty())
                translationRequest = Validation.oneStepValidation(translation, Constants.JSON_PATTERN);
            if (translationRequest == null) return Constants.TRANSLATION_DOESNT_FIT;
            wordObj.setTranslation(translationRequest);
            wasUpdated = true;
        }

        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            wordObj.setImage(imageName);
            MainService.saveImage(image);
            wasUpdated = true;
        }

        boolean checked = checkboxValue != null;
        if (wordObj.isApproved() != checked){
            wordObj.setApproved(checked);
            wasUpdated = true;
        }

        if (wasUpdated) {
            wordDAO.save(wordObj);
            return Constants.WORD_UPDATE_SUCCESS;
        }
        return Constants.WORD_UPDATE_UNSUCCESS;
    }

    @PostMapping("/word/add")
    public String addWord(Principal principal,
                          @RequestParam String word,
                          @RequestParam String partOfSpeech,
                          @RequestParam String description,
                          @RequestParam String example,
                          @RequestParam String translationRu,
                          @RequestParam String translationUa,
                          @RequestParam MultipartFile image) {
        User user = userDAO.findUserByUsername(principal.getName());
        List<? extends GrantedAuthority> collect = new ArrayList<>(user.getAuthorities());

        Word wordObj = new Word();
        for (GrantedAuthority grantedAuthority : collect) {
            if (grantedAuthority.toString().equals("ROLE_ADMIN")) wordObj.setApproved(true);
        }
        String wordRequest = null;
        if (word != null && !word.isEmpty()) wordRequest = Validation.wordValidation(word);
        if (wordRequest == null) return Constants.WORD_DOESNT_FIT;
        wordObj.setWord(wordRequest);

        PartOfSpeech partOfSpeechRequest = Validation.partOfSpeechValidation(partOfSpeech.toUpperCase());
        if (partOfSpeechRequest == null) return Constants.PART_OF_SPEECH_DOESNT_FIT;
        wordObj.setPartOfSpeech(partOfSpeechRequest);

        String descriptionRequest = null;
        if (description != null && !description.isEmpty())
            descriptionRequest = Validation.sentenceValidation(description);
        if (descriptionRequest == null) return Constants.DESCRIPTION_DOESNT_FIT;
        wordObj.setDescription(descriptionRequest);

        String exampleRequest = null;
        if (example != null && !example.isEmpty()) exampleRequest = Validation.sentenceValidation(example);
        if (exampleRequest == null) return Constants.EXAMPLE_DOESNT_FIT;
        wordObj.setExample(exampleRequest);

        Translation translationObj = new Translation(translationRu, translationUa);
        String translation = translationObj.toString();

        String translationRequest = null;
        if (translation != null && !translation.isEmpty())
            translationRequest = Validation.oneStepValidation(translation, Constants.JSON_PATTERN);
        if (translationRequest == null) return Constants.TRANSLATION_DOESNT_FIT;
        wordObj.setTranslation(translationRequest);

        if (image != null) {
            String imageName = image.getOriginalFilename();
            wordObj.setImage(imageName);
            MainService.saveImage(image);
        }

        if (wordObj.getWord() != null && wordObj.getPartOfSpeech() != null
                && wordObj.getDescription() != null && wordObj.getExample() != null
                && wordObj.getTranslation() != null) {
            wordDAO.save(wordObj);
            return Constants.WORD_SAVE_SUCCESS;
        }
        return Constants.WORD_SAVE_UNSUCCESS;
    }

    @GetMapping("/words/get")
    public List<Word> getWords() {
        return wordDAO.findAll();
    }

    @GetMapping("/mode/random/get/{shuffleFlag}")
    public List<Word> getRandomWords(@PathVariable int shuffleFlag) {
        List <Word> randomWords = wordDAO.getRandomWords();
        Collections.shuffle(randomWords, new Random(shuffleFlag));
        return randomWords.subList(0, 5);
    }

    @GetMapping("/words/notapproved/get/page/{page}")
    public PagedListHolder<Word> getWordsNotApproved(@PathVariable int page) {
        List<Word> all = wordDAO.findAll();
        List<Word> collect = all.stream().filter(a -> !a.isApproved()).collect(Collectors.toList());
        PagedListHolder<Word> pageHolder = new PagedListHolder<>(collect);
        pageHolder.setPageSize(3);
        pageHolder.setPage(page);
        return pageHolder;
    }

    @PostMapping("/lib/{idLib}/add")
    public String addNewWordToLib(Principal principal,
                                  @PathVariable int idLib,
                                  @RequestParam String word,
                                  @RequestParam String partOfSpeech,
                                  @RequestParam String description,
                                  @RequestParam String example,
                                  @RequestParam String translationRu,
                                  @RequestParam String translationUa,
                                  @RequestParam MultipartFile image) {
        addWord(principal, word, partOfSpeech, description, example, translationRu, translationUa, image);
        Word wordObj = wordDAO.findByWordAndPartOfSpeech(word, PartOfSpeech.valueOf(partOfSpeech.toUpperCase()));
        if (libDAO.findById(idLib).isPresent()) {
            Lib lib = libDAO.findById(idLib).get();
            List<Word> words = lib.getWords();
            words.add(wordObj);
            lib.setWords(words);
            lib.setUpdateDate(LocalDateTime.now());
            libDAO.save(lib);
            return Constants.WORD_SAVE_SUCCESS;
        }
        return Constants.WORD_SAVE_UNSUCCESS;

    }

    @PostMapping("/lib/{idLib}/{idWord}/add")
    public String addExistWordToLib(@PathVariable int idLib,
                                    @PathVariable int idWord) {
        if (libDAO.findById(idLib).isPresent() && wordDAO.findById(idWord).isPresent()) {
            Lib lib = libDAO.findById(idLib).get();
            Word wordObj = wordDAO.findById(idWord).get();
            List<Word> words = lib.getWords();
            words.add(wordObj);
            lib.setWords(words);
            lib.setUpdateDate(LocalDateTime.now());
            libDAO.save(lib);
            return Constants.WORD_SAVE_SUCCESS;
        }
        return Constants.WORD_SAVE_UNSUCCESS;
    }

}
