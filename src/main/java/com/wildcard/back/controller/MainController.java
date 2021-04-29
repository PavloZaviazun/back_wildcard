package com.wildcard.back.controller;

import com.wildcard.back.models.Word;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MainController {

//    private VocabularyDAO vocabularyDAO;
//    private LibDAO libDAO;

//    @GetMapping("/set")
//    public void set() throws IOException {
//        ArrayList<String> ru = new ArrayList <>();
//        ArrayList<String> ua = new ArrayList <>();
//        vocabularyDAO.save(
//                new Vocabulary("Apple",
//                        PartOfSpeech.PREPOSITION,
//                        "This is fruit",
//                        "Apple could be red",
//                        null,
//                        new Translation(ru, ua)));
//    }

    @GetMapping("/get")
    public List <Word> get() {
//        return vocabularyDAO.findAll();
        return null;
    }

//    @GetMapping("/lib")
//    public List<Vocabulary> getSM() {
//        return libDAO.getLib();
//    }
}

