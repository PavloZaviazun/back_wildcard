package com.wildcard.back.controller;

import com.wildcard.back.dao.WordDAO;
import com.wildcard.back.models.PartOfSpeech;
import com.wildcard.back.models.Word;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WordController {

    private WordDAO wordDAO;

    @GetMapping("/lib/{id}/word/get")
    public List <Word> getLibWords(@PathVariable int id) {
        return wordDAO.getLibWords(id);
    }

    @GetMapping("/word/{id}/get")
    public Word getWord(@PathVariable int id) {
        return wordDAO.getOne(id);
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

        if(!wordObj.getWord().equals(word)) {
            if(Validation.wordValidation(word) != null) wordObj.setWord(word);
        }
        if(!wordObj.getPartOfSpeech().equals(PartOfSpeech.valueOf(partOfSpeech))) {
            PartOfSpeech value = Validation.partOfSpeechValidation(partOfSpeech.toUpperCase());
            if(value != null) wordObj.setPartOfSpeech(value);
        }



        PartOfSpeech value = PartOfSpeech.valueOf(partOfSpeech);
        if(!wordObj.getPartOfSpeech().equals(value)) wordObj.setWord(word);
    }

    @PostMapping("/word/add")
    public void addWord(/*@RequestParam String word,
                              @RequestParam String partOfSpeech,
                              @RequestParam String description,
                              @RequestParam String example,
                              @RequestParam String image,
                              @RequestParam String translation*/) {

        String word = "Her ";
        String partOfSpeech = "NOUN";
        String description = "her, pasha!";
        String example = "her, g pasha!";
        String translation = "{\"ru\":\"рус\", \"ua\":\"укр\"}";

        Word wordObj = new Word();
        if(Validation.wordValidation(word) != null) wordObj.setWord(word);

        PartOfSpeech value = Validation.partOfSpeechValidation(partOfSpeech.toUpperCase());
        if(value != null) wordObj.setPartOfSpeech(value);


        if(!Validation.checkValidation(description, Validation.SENTENCE_PATTERN)) {
            description = Validation.firstToTitleCase(description);
        }
        if(Validation.checkValidation(description, Validation.SENTENCE_PATTERN)) {
            wordObj.setDescription(description);
        }

        if(!Validation.checkValidation(example, Validation.SENTENCE_PATTERN)) {
            example = Validation.firstToTitleCase(example);
        }
        if(Validation.checkValidation(example, Validation.SENTENCE_PATTERN)) {
            wordObj.setExample(example);
        }

        if(Validation.checkValidation(translation, Validation.JSON_PATTERN)) {
            wordObj.setTranslation(translation);
        }
        System.out.println(wordObj);
        if(wordObj.getWord() != null && wordObj.getPartOfSpeech() != null
            && wordObj.getDescription() != null && wordObj.getExample() != null
            && wordObj.getTranslation() != null) {
                wordDAO.save(wordObj);
        }
    }


}
