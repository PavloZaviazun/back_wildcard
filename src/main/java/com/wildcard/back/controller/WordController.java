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
        boolean wasUpdated = false;

        if(!wordObj.getWord().equals(word)) {
            String wordRequest = Validation.wordValidation(word);
            if(wordRequest != null) {
                wordObj.setWord(wordRequest);
                wasUpdated = true;
            }
        }

        if(!wordObj.getPartOfSpeech().equals(PartOfSpeech.valueOf(partOfSpeech))) {
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
            String translationRequest = Validation.translationValidation(translation);
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
    public void addWord(/*@RequestParam String word,
                              @RequestParam String partOfSpeech,
                              @RequestParam String description,
                              @RequestParam String example,
                              @RequestParam String image,
                              @RequestParam String translation*/) {

        String word = "Her ";
        String partOfSpeech = "NOUN";
        String description = " her, pasha!";
        String example = "her, g pasha!";
        String translation = "{\"ru\":\"рус\", \"ua\":\"укр\"}";

        Word wordObj = new Word();
        String wordRequest = Validation.wordValidation(word);
        if(wordRequest != null) wordObj.setWord(wordRequest);

        PartOfSpeech partOfSpeechRequest = Validation.partOfSpeechValidation(partOfSpeech.toUpperCase());
        if(partOfSpeechRequest != null) wordObj.setPartOfSpeech(partOfSpeechRequest);

        String descriptionRequest = Validation.sentenceValidation(description);
        if(descriptionRequest != null) wordObj.setDescription(descriptionRequest);

        String exampleRequest = Validation.sentenceValidation(example);
        if(exampleRequest != null) wordObj.setExample(exampleRequest);

        String translationRequest = Validation.translationValidation(translation);
        if(translationRequest != null) wordObj.setTranslation(translationRequest);

        if(wordObj.getWord() != null && wordObj.getPartOfSpeech() != null
            && wordObj.getDescription() != null && wordObj.getExample() != null
            && wordObj.getTranslation() != null) {
                wordDAO.save(wordObj);
        }
    }


}
