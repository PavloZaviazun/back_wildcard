package com.wildcard.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildcard.back.dao.LibDAO;
import com.wildcard.back.models.Lib;
import com.wildcard.back.service.QueryService;
import com.wildcard.back.util.Constants;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class LibController {

    private LibDAO libDAO;
    private EntityManager entityManager;

    @GetMapping("/searchLib/{name}")
    public List<Lib> searchLib(@PathVariable String name) {
        return libDAO.searchLib(name.toLowerCase());
    }

    @GetMapping("libs/{idWord}")
    public List <Lib> getLibsOfWord(@PathVariable int idWord) {
        List<Integer> resultList = new QueryService(entityManager).selectLibsId(idWord);
        List<Lib> list = new ArrayList <>();
        for(Integer el : resultList) {
            if(libDAO.findById(el).isPresent()) {
                list.add(libDAO.findById(el).get());
            }
        }
        return list;
    }

    @PostMapping("/lib/add")
    public String addLib(@RequestParam String name) {
        String nameRequest = null;
        if(name != null && !name.isEmpty()) nameRequest = Validation.sentenceValidation(name);
        if(nameRequest == null) return Constants.LIB_NAME_DOESNT_FIT;
        libDAO.save(new Lib(nameRequest));
        return Constants.LIB_SAVE_SUCCESS;
    }

    @GetMapping("/lib/{id}/get")
    public Lib getLib(@PathVariable int id) {
        if(libDAO.findById(id).isPresent()) {
            return libDAO.findById(id).get();
        }
        return new Lib();
    }

    @DeleteMapping("/lib/{id}/delete")
    public void deleteLib(@PathVariable int id) {
        libDAO.deleteById(id);
    }

    @PostMapping("/lib/{id}/update")
    public String updateLib(@PathVariable int id,
                          @RequestParam String name) {
        Lib libObj = libDAO.getOne(id);
        if(!libObj.getName().equals(name)) {
            String nameRequest = null;
            if(name != null && !name.isEmpty()) nameRequest = Validation.sentenceValidation(name);
            if(nameRequest == null) return Constants.LIB_UPDATE_UNSUCCESS;
            libObj.setName(nameRequest);
            libObj.setUpdateDate(LocalDateTime.now());
            libDAO.save(libObj);
        }
        return Constants.LIB_UPDATE_SUCCESS;
    }

    @GetMapping("/libs/get")
    public List <Lib> getLibs() {
        return libDAO.findAll()
                .stream()
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/libs/get/page/{page}")
    public Page <Lib> getLibsWP(@PathVariable int page) {
        return libDAO.getLibsWP(PageRequest.of(page, 3));
    }

}
