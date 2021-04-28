package com.wildcard.back.controller;

import com.wildcard.back.dao.VocabularyDAO;
import com.wildcard.back.models.Vocabulary;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LibController {

    private VocabularyDAO vocabularyDAO;

    @GetMapping("/lib/{id}/vocabulary/get")
    public List <Vocabulary> getLibVocabulary(@PathVariable int id) {
        return vocabularyDAO.getLibWords(id);
    }

}
