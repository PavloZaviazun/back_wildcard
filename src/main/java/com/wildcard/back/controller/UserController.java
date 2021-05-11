package com.wildcard.back.controller;

import com.wildcard.back.dao.UserDAO;
import com.wildcard.back.models.User;
import com.wildcard.back.service.MailService;
import com.wildcard.back.util.Constants;
import com.wildcard.back.util.NativeLang;
import com.wildcard.back.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Locale;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class UserController {
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;

    @PostMapping("/register")
    public String register(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String nativeLang) {
        if (userDAO.findByEmail(email) != null) return Constants.USER_EXISTS_ALREADY;
        User userObj = new User();
        String passwordRequest = Validation.oneStepValidation(password, Validation.PASSWORD_PATTERN);
        if (passwordRequest == null) return Constants.PASSWORD_DOESNT_FIT;
        else userObj.setPassword(passwordEncoder.encode(passwordRequest));
        //TODO запретить повторную регистрацию
        String emailRequest = Validation.oneStepValidation(email, Validation.EMAIL_PATTERN);
        if (emailRequest == null) return Constants.EMAIL_DOESNT_FIT;
        else {
            userObj.setEmail(emailRequest);
            userObj.setUsername(emailRequest);
        }

        NativeLang nativeLangRequest = Validation.nativeLangValidation(nativeLang);
        if (nativeLangRequest != null) userObj.setNativeLang(nativeLangRequest);

        if (userObj.getEmail() != null && userObj.getUsername() != null
                && userObj.getPassword() != null && userObj.getNativeLang() != null) {
            userDAO.save(userObj);
            User byEmail = userDAO.findByEmail(email);
            mailService.sendEmailToEnableUser(email, byEmail.getId());
        }
        return Constants.USER_ENABLE_REGISTRATION;
    }

    @PostMapping("/login")
    public String login() {
        return "Successfull logination";
    }

    @GetMapping("/activate/{id}")
    public String activateUser(@PathVariable int id) {
        if (userDAO.findById(id).isPresent()) {
            User user = userDAO.findById(id).get();
            if (user.isEnabled()) return Constants.USER_IS_ENABLED_ALREADY;
            user.setEnabled(true);
            userDAO.save(user);
            return Constants.USER_IS_ENABLED;
        }
        return Constants.ENABLE_MISTAKE;
    }

    @GetMapping("/update/{id}/newemail/{email}")
    public String updateUserEmail(@PathVariable int id,
                                  @PathVariable String email) {
        if (userDAO.findById(id).isPresent()) {
            User user = userDAO.findById(id).get();
            String emailRequest = Validation.oneStepValidation(email, Validation.EMAIL_PATTERN);
            if (emailRequest != null) {
                user.setEmail(email);
                user.setUsername(email);
                userDAO.save(user);
                return Constants.USER_UPDATE_EMAIL;
            }
        }
        return Constants.USER_NOT_UPDATE_EMAIL;
    }

    @PatchMapping("/user/{id}/update")
    public String updateUser(@PathVariable int id,
                           @RequestParam String email,
                           @RequestParam String nativeLang) {

        User userObj = userDAO.getOne(id);
        boolean wasUpdated = false;

//        //TODO temporary Email == username
//        if (!userObj.getUsername().equals(login)) {
//            String loginRequest = Validation.oneStepValidation(login, Validation.EMAIL_PATTERN);
//            if (loginRequest != null) {
//                userObj.setUsername(loginRequest);
//                wasUpdated = true;
//            }
//        }

        if (!userObj.getEmail().equals(email)) {
            String emailRequest = Validation.oneStepValidation(email, Validation.EMAIL_PATTERN);
            if (emailRequest == null) return Constants.EMAIL_DOESNT_FIT;
            else {
                mailService.sendEmailToChangeMail(email, id);
            }
        }

        if (!userObj.getNativeLang().equals(NativeLang.valueOf(nativeLang))) {
            if (!userObj.getNativeLang().toString().equals(nativeLang.toUpperCase())) {
                NativeLang nativeLangRequest = Validation.nativeLangValidation(nativeLang);
                if (nativeLangRequest != null) {
                    userObj.setNativeLang(NativeLang.valueOf(nativeLang.toUpperCase()));
                    wasUpdated = true;
                }
            }
        }

        if (wasUpdated) {
            userDAO.save(userObj);
            return Constants.USER_UPDATE;
        }
        return Constants.USER_NOT_UPDATE;
    }

    @GetMapping("/user/get")
    public User getUser(Principal principal) {
        return userDAO.findUserByUsername(principal.getName());
    }

    @GetMapping("/user/{id}/get")
    public User getUser(@PathVariable int id) {
        if (userDAO.findById(id).isPresent()) {
            return userDAO.findById(id).get();
        }
        return new User();
    }

    @DeleteMapping("/user/{id}/delete")
    public void deleteUser(@PathVariable int id) {
        userDAO.deleteById(id);
    }

    @GetMapping("/users/get/page/{page}")
    public Page<User> getUsers(@PathVariable int page) {
        return userDAO.getUsersWP(PageRequest.of(page, 20));
    }

    @GetMapping("/user/add/word/{id}")
    public String addToUserFavLib(Principal principal, @PathVariable int id) {
        //insert into user_fav_lib (id_user, fav_lib)
        //values  (1, '{"name": "favourite", "listOfWords": []}');
        User user = userDAO.findUserByUsername(principal.getName());
        return "";

    }

}
