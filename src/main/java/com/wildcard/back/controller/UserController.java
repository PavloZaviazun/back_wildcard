package com.wildcard.back.controller;

import com.wildcard.back.dao.UserDAO;
import com.wildcard.back.models.User;
import com.wildcard.back.util.NativeLang;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth/register")
    public void register(@RequestParam String username,
                         @RequestParam String password) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(username);
        userDAO.save(user);
    }

    @PostMapping("/auth/login")
    public void login() {

    }

    @PostMapping("/user/add")
    public void addUser(@RequestParam String password,
                        @RequestParam String email,
                        @RequestParam String nativeLang) {
        User userObj = new User();

        String passwordRequest = Validation.oneStepValidation(password, Validation.PASSWORD_PATTERN);
        if(passwordRequest != null) userObj.setPassword(passwordRequest);

        String emailRequest = Validation.oneStepValidation(email, Validation.EMAIL_PATTERN);
        if(emailRequest != null) {
            userObj.setEmail(emailRequest);
            //TODO emails can be different but logins will be the same
            userObj.setUsername(email.split("@")[0]);
        }

        NativeLang nativeLangRequest = Validation.nativeLangValidation(nativeLang);
        if(nativeLangRequest != null) userObj.setNativeLang(nativeLangRequest);
        System.out.println(userObj);
        if(userObj.getEmail() != null && userObj.getUsername() != null
            && userObj.getPassword() != null && userObj.getNativeLang() != null) {
            userDAO.save(userObj);
        }
    }

    @PatchMapping("/user/{id}/update")
    public void updateUser(@PathVariable int id,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String nativeLang,
                           @RequestParam String login) {

        User userObj = userDAO.getOne(id);
        boolean wasUpdated = false;

        if(!userObj.getUsername().equals(login)) {
            String loginRequest = Validation.oneStepValidation(login, Validation.LOGIN_PATTERN);
            if(loginRequest != null) {
                userObj.setUsername(loginRequest);
                wasUpdated = true;
            }
        }

        if(!userObj.getPassword().equals(password)) {
            String passwordRequest = Validation.oneStepValidation(password, Validation.PASSWORD_PATTERN);
            if(passwordRequest != null) {
                userObj.setPassword(passwordRequest);
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

    @GetMapping("/users/get")
    public List <User> getUsers() {
        return userDAO.findAll();
    }


}
