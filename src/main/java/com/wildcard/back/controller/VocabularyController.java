package com.wildcard.back.controller;

import com.wildcard.back.dao.VocabularyDAO;
import com.wildcard.back.models.PartOfSpeech;
import com.wildcard.back.models.Vocabulary;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PostRemove;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class VocabularyController {

    private VocabularyDAO vocabularyDAO;

    @GetMapping("/lib/{id}/vocabulary/get")
    public List <Vocabulary> getLibVocabulary(@PathVariable int id) {
        return vocabularyDAO.getLibWords(id);
    }

    @GetMapping("/vocabulary/{id}/get")
    public Vocabulary getVocabulary(@PathVariable int id) {
        return vocabularyDAO.getOne(id);
    }

    @DeleteMapping("/vocabulary/{id}/delete")
    public void deleteVocabulary(@PathVariable int id) {

    }


    @PostMapping("/vocabulary/add")
    public void addVocabulary(/*@RequestParam String word,
                              @RequestParam String partOfSpeech,
                              @RequestParam String description,
                              @RequestParam String example,
                              @RequestParam String image,
                              @RequestParam String translation*/) {
        Vocabulary vocabulary = new Vocabulary();

        String word = "Her ";
        String partOfSpeech = "NOUN";
        String description = "her, pasha!";
        String example = "her, g pasha!";
        String translation = "{\"ru\":\"рус\", \"ua\":\"укр\"}";

        if(!Validation.checkValidation(word, Validation.WORD_PATTERN)) {
            word = Validation.firstToTitleCase(word);
        }
        if(!Validation.checkValidation(word, Validation.WORD_PATTERN)) {
            word = Validation.lastsToLowerCase(word);
        }
        if(Validation.checkValidation(word, Validation.WORD_PATTERN)) {
            vocabulary.setWord(word);
        }

        switch(partOfSpeech.toUpperCase()) {
            case "NOUN" : vocabulary.setPartOfSpeech(PartOfSpeech.NOUN);
                break;
            case "PRONOUN" : vocabulary.setPartOfSpeech(PartOfSpeech.PRONOUN);
                break;
            case "VERB" : vocabulary.setPartOfSpeech(PartOfSpeech.VERB);
                break;
            case "ADVERB" : vocabulary.setPartOfSpeech(PartOfSpeech.ADVERB);
                break;
            case "ADJECTIVE" : vocabulary.setPartOfSpeech(PartOfSpeech.ADJECTIVE);
                break;
            case "PREPOSITION" : vocabulary.setPartOfSpeech(PartOfSpeech.PREPOSITION);
            default :
                System.out.println("Inappropriate part of speech");
        }

        if(!Validation.checkValidation(description, Validation.SENTENCE_PATTERN)) {
            description = Validation.firstToTitleCase(description);
        }
        if(Validation.checkValidation(description, Validation.SENTENCE_PATTERN)) {
            vocabulary.setDescription(description);
        }

        if(!Validation.checkValidation(example, Validation.SENTENCE_PATTERN)) {
            example = Validation.firstToTitleCase(example);
        }
        if(Validation.checkValidation(example, Validation.SENTENCE_PATTERN)) {
            vocabulary.setExample(example);
        }

        if(Validation.checkValidation(translation, Validation.JSON_PATTERN)) {
            vocabulary.setTranslation(translation);
        }

        if(vocabulary.getWord() != null && vocabulary.getPartOfSpeech() != null
            && vocabulary.getDescription() != null && vocabulary.getExample() != null) {
                vocabularyDAO.save(vocabulary);
        }
    }


}
