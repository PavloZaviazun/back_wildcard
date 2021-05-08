package com.wildcard.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildcard.back.dao.LibDAO;
import com.wildcard.back.dao.WordDAO;
import com.wildcard.back.models.Lib;
import com.wildcard.back.util.PartOfSpeech;
import com.wildcard.back.models.Word;
import com.wildcard.back.service.QueryService;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class WordController {
    private LibDAO libDAO;
    private WordDAO wordDAO;
    private EntityManager entityManager;

    @GetMapping("/searchByWord/{word}")
    public List<Word> searchWord(@PathVariable String word) {
        return wordDAO.searchByWord(word.toLowerCase());
    }

    @GetMapping("/searchByLetter/{letter}/page/{page}")
    public Page<Word> searchByLetter(@PathVariable String letter,
                                     @PathVariable int page) {
        return wordDAO.searchByLetter(letter, PageRequest.of(page + 1, 20));
    }

    @GetMapping("/partsOfSpeech/{word}")
    public String[] getPartsOfSpeech(@PathVariable String word) {
        List<Word> list = null;
        String wordRequest = Validation.wordValidation(word);
        if(wordRequest != null) {
            list = wordDAO.findByWord(wordRequest);
        }
        String[] array = new String[list.size()];
        for(int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).getPartOfSpeech().toString();
        }
        return array;
    }

    @GetMapping("/lib/{id}/words/get")
    public List <Word> getLibWords(@PathVariable int id) {
        List<Integer> resultList = new QueryService(entityManager).selectWordsId(id);
        List<Word> list = new ArrayList <>();
        for(Integer el : resultList) {
            if(wordDAO.findById(el).isPresent()) {
                list.add(wordDAO.findById(el).get());
            }
        }
        return list;
    }

    @GetMapping("/word/{id}/get")
    public Word getWord(@PathVariable int id) {
        if(wordDAO.findById(id).isPresent()) {
            return wordDAO.findById(id).get();
        }
        return new Word();
    }

    @DeleteMapping("/word/{id}/delete")
    public void deleteWord(@PathVariable int id) {
        wordDAO.deleteById(id);
    }

    @PatchMapping("/word/{id}/update")
    public void updateWord(@PathVariable int id,
                           @RequestParam String word,
                           @RequestParam String partOfSpeech,
                           @RequestParam String description,
                           @RequestParam String example,
                           @RequestParam String image,
                           @RequestParam String translation) {
        Word wordObj = wordDAO.getOne(id);
        boolean wasUpdated = false;

        if(!wordObj.getWord().equals(word)) {
            String wordRequest = Validation.wordValidation(word);
            if(wordRequest != null) {
                wordObj.setWord(wordRequest);
                wasUpdated = true;
            }
        }

        if(!wordObj.getPartOfSpeech().equals(PartOfSpeech.valueOf(partOfSpeech.toUpperCase()))) {
            PartOfSpeech partOfSpeechRequest = Validation.partOfSpeechValidation(partOfSpeech.toUpperCase());
            if(partOfSpeechRequest != null) {
                wordObj.setPartOfSpeech(partOfSpeechRequest);
                wasUpdated = true;
            }
        }

        if(!wordObj.getDescription().equals(description)) {
            String descriptionRequest = Validation.sentenceValidation(description);
            if(descriptionRequest != null) {
                wordObj.setDescription(descriptionRequest);
                wasUpdated = true;
            }
        }

        if(!wordObj.getExample().equals(example)) {
            String exampleRequest = Validation.sentenceValidation(example);
            if(exampleRequest != null) {
                wordObj.setExample(exampleRequest);
                wasUpdated = true;
            }
        }

        if(!wordObj.getTranslation().equals(translation)) {
            String translationRequest = Validation.oneStepValidation(translation, Validation.JSON_PATTERN);
            if(translationRequest != null) {
                wordObj.setTranslation(translationRequest);
                wasUpdated = true;
            }
        }

        if(wasUpdated) {
            wordDAO.save(wordObj);
        }
    }

    @PostMapping("/word/add")
    public void addWord(@RequestParam String word,
                              @RequestParam String partOfSpeech,
                              @RequestParam String description,
                              @RequestParam String example,
                              @RequestParam String image,
                              @RequestParam String translation) {

        Word wordObj = new Word();
        String wordRequest = Validation.wordValidation(word);
        if(wordRequest != null) wordObj.setWord(wordRequest);

        PartOfSpeech partOfSpeechRequest = Validation.partOfSpeechValidation(partOfSpeech.toUpperCase());
        if(partOfSpeechRequest != null) wordObj.setPartOfSpeech(partOfSpeechRequest);

        String descriptionRequest = Validation.sentenceValidation(description);
        if(descriptionRequest != null) wordObj.setDescription(descriptionRequest);

        String exampleRequest = Validation.sentenceValidation(example);
        if(exampleRequest != null) wordObj.setExample(exampleRequest);

        String translationRequest = Validation.oneStepValidation(translation, Validation.JSON_PATTERN);
        if(translationRequest != null) wordObj.setTranslation(translationRequest);

        if(wordObj.getWord() != null && wordObj.getPartOfSpeech() != null
            && wordObj.getDescription() != null && wordObj.getExample() != null
            && wordObj.getTranslation() != null) {
                wordDAO.save(wordObj);
        }
    }

    @GetMapping("/words/get")
    public List<Word> getWords() {
        return wordDAO.findAll();
    }

    @PostMapping("/lib/{idLib}/add")
    public void addNewWordToLib(@PathVariable int idLib,
                                @RequestParam String word,
                                @RequestParam String partOfSpeech,
                                @RequestParam String description,
                                @RequestParam String example,
                                @RequestParam String image,
                                @RequestParam String translation) {
        addWord(word, partOfSpeech, description, example, image,translation);
        Word wordObj = wordDAO.findByWordAndPartOfSpeech(word, PartOfSpeech.valueOf(partOfSpeech.toUpperCase()));
        System.out.println(wordObj);
        if(libDAO.findById(idLib).isPresent()) {
            Lib lib = libDAO.findById(idLib).get();
            List <Word> words = lib.getWords();
            words.add(wordObj);
            lib.setWords(words);
            libDAO.save(lib);
        }
    }

    @PostMapping("/lib/{idLib}/{idWord}/add")
    public void addExistWordToLib(@PathVariable int idLib,
                                  @PathVariable int idWord) {
        if(libDAO.findById(idLib).isPresent() && wordDAO.findById(idWord).isPresent()) {
            Lib lib = libDAO.findById(idLib).get();
            Word wordObj = wordDAO.findById(idWord).get();
            List <Word> words = lib.getWords();
            words.add(wordObj);
            lib.setWords(words);
            libDAO.save(lib);
        }

    }

}
