package com.wildcard.back.controller;

import com.wildcard.back.dao.LibDAO;
import com.wildcard.back.models.Lib;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LibController {

    private LibDAO libDAO;

    @PostMapping("/lib/add")
    public void addLib(@RequestParam String name) {
        String nameRequest = Validation.sentenceValidation(name);
        if(nameRequest != null) libDAO.save(new Lib(nameRequest));
    }

    @GetMapping("/lib/{id}/get")
    public void getLib(@PathVariable int id) {

    }

    @GetMapping("/libs/get")
    public List <Lib> getLibs() {
        return libDAO.findAll();
    }


}
