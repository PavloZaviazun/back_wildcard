package com.wildcard.back.controller;

import com.wildcard.back.dao.LibDAO;
import com.wildcard.back.models.Lib;
import com.wildcard.back.models.Word;
import com.wildcard.back.service.QueryService;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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
    public void addLib(@RequestParam String name) {
        String nameRequest = Validation.sentenceValidation(name);
        if(nameRequest != null) libDAO.save(new Lib(nameRequest));
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

    @PatchMapping("/lib/{id}/update")
    public void updateLib(@PathVariable int id,
                          @RequestParam String name) {
        Lib libObj = libDAO.getOne(id);
        if(!libObj.getName().equals(name)) {
            String nameRequest = Validation.sentenceValidation(name);
            if(nameRequest != null) {
                libObj.setName(nameRequest);
                libDAO.save(libObj);
            }
        }
    }

    @GetMapping("/libs/get")
    public List <Lib> getLibs() {
        return libDAO.findAll();
    }
}
