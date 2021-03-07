package com.wildcard.back.controller;

import com.wildcard.back.dao.VocabularyDAO;
import com.wildcard.back.models.PartOfSpeech;
import com.wildcard.back.models.Translation;
import com.wildcard.back.models.Vocabulary;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MainController {

    private VocabularyDAO vocabularyDAO;

    @GetMapping("/set")
    public void set() throws IOException {
        ArrayList<String> ru = new ArrayList <>();
        ArrayList<String> ua = new ArrayList <>();
        vocabularyDAO.save(
                new Vocabulary("Apple",
                        PartOfSpeech.PREPOSITION,
                        "This is fruit",
                        "Apple could be red",
                        null,
                        new Translation(ru, ua)));
    }

    @GetMapping("/get")
    public List <Vocabulary> get() {
        return vocabularyDAO.findAll();
    }
}

