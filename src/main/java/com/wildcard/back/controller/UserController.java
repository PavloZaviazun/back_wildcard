package com.wildcard.back.controller;

import com.wildcard.back.dao.UserDAO;
import com.wildcard.back.models.User;
import com.wildcard.back.util.NativeLang;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class UserController {
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public void register(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String nativeLang) {
        User userObj = new User();
        String passwordRequest = Validation.oneStepValidation(password, Validation.PASSWORD_PATTERN);
        if(passwordRequest != null) userObj.setPassword(passwordEncoder.encode(passwordRequest));

        String emailRequest = Validation.oneStepValidation(email, Validation.EMAIL_PATTERN);
        if(emailRequest != null) {
            userObj.setEmail(emailRequest);
            userObj.setUsername(emailRequest);
        }

        NativeLang nativeLangRequest = Validation.nativeLangValidation(nativeLang);
        if(nativeLangRequest != null) userObj.setNativeLang(nativeLangRequest);

        if(userObj.getEmail() != null && userObj.getUsername() != null
                && userObj.getPassword() != null && userObj.getNativeLang() != null) {
            userDAO.save(userObj);

        }
    }

    @PostMapping("/login")
    public void login() {

    }

    @PatchMapping("/user/{id}/update")
    public void updateUser(@PathVariable int id,
                           @RequestParam String email,
                           @RequestParam String nativeLang,
                           @RequestParam String login) {

        User userObj = userDAO.getOne(id);
        boolean wasUpdated = false;

        //TODO temporary Email == username
        if(!userObj.getUsername().equals(login)) {
            String loginRequest = Validation.oneStepValidation(login, Validation.EMAIL_PATTERN);
            if(loginRequest != null) {
                userObj.setUsername(loginRequest);
                wasUpdated = true;
            }
        }

        if(!userObj.getEmail().equals(email)) {
            String emailRequest = Validation.oneStepValidation(email, Validation.EMAIL_PATTERN);
            if (emailRequest != null) {
                userObj.setEmail(emailRequest);
                wasUpdated = true;
            }
        }

        if(!userObj.getNativeLang().equals(NativeLang.valueOf(nativeLang))) {
            NativeLang nativeLangRequest = Validation.nativeLangValidation(nativeLang);
            if(nativeLangRequest != null) {
                userObj.setNativeLang(nativeLangRequest);
                wasUpdated = true;
            }
        }

        if(wasUpdated) {
            userDAO.save(userObj);
        }
    }

    @GetMapping("/user/{id}/get")
    public User getUser(@PathVariable int id) {
        if(userDAO.findById(id).isPresent()) {
            return userDAO.findById(id).get();
        }
        return new User();
    }


    @DeleteMapping("/user/{id}/delete")
    public void deleteUser(@PathVariable int id) {
        userDAO.deleteById(id);
    }

    @GetMapping("/users/get/page/{page}")
    public Page <User> getUsers(@PathVariable int page) {
        return userDAO.getUsersWP(PageRequest.of(page, 20));
    }

}
