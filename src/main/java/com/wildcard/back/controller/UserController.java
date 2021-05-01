package com.wildcard.back.controller;

import com.wildcard.back.dao.UserDAO;
import com.wildcard.back.models.User;
import com.wildcard.back.models.Word;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private UserDAO userDAO;

    @PostMapping("/user/add")
    public void addUser(/*@RequestParam String login,
                        @RequestParam String password,
                        @RequestParam String email,
                        @RequestParam int nativeLang,
                        @RequestParam boolean isActive,
                        @RequestParam int roleId*/) {
        String login = "help4engineer";
        String password = "996659floreNAA!";
        String email = "help4engineer@gmail.com";

        User userObj = new User();
        String loginRequest = Validation.oneStepValidation(login, Validation.LOGIN_PATTERN);
        if(loginRequest != null) userObj.setLogin(loginRequest);

        String passwordRequest = Validation.oneStepValidation(password, Validation.PASSWORD_PATTERN);
        if(passwordRequest != null) userObj.setPassword(passwordRequest);

        String emailRequest = Validation.oneStepValidation(email, Validation.EMAIL_PATTERN);
        if(emailRequest != null) userObj.setEmail(emailRequest);

        System.out.println(userObj);

    }

    @GetMapping("/user/{id}/get")
    public User getUser(@PathVariable int id) {
        return null;
    }

    @PatchMapping("/user/{id}/update")
    public void updateUser(@PathVariable int id) {

    }

    @DeleteMapping("/user/{id}/delete")
    public void deleteUser(@PathVariable int id) {

    }

    @GetMapping("/users/get")
    public List <User> getUsers() {
        return null;
    }


}
