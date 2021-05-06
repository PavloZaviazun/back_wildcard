package com.wildcard.back.controller;

import com.wildcard.back.util.PartOfSpeech;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MainController {

    @GetMapping("/partsOfSpeech")
    public String[] getPartsOfSpeech() {
        PartOfSpeech[] values = PartOfSpeech.values();
        String[] array = new String[values.length];
        for(int i = 0; i < values.length; i++) {
            array[i] = String.valueOf(values[i]);
        }
        return array;
    }
}
