package com.wildcard.back.controller;

import com.wildcard.back.dao.UserDAO;
import com.wildcard.back.models.User;
import com.wildcard.back.service.MailService;
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
    public void register(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String nativeLang) {
        User userObj = new User();
        String passwordRequest = Validation.oneStepValidation(password, Validation.PASSWORD_PATTERN);
        if (passwordRequest != null) userObj.setPassword(passwordEncoder.encode(passwordRequest));
        //TODO запретить повторную регистрацию
        String emailRequest = Validation.oneStepValidation(email, Validation.EMAIL_PATTERN);
        if (emailRequest != null) {
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
    }

    @PostMapping("/login")
    public void login() {

    }

    @GetMapping("/activate/{id}")
    public String activateUser(@PathVariable int id) {
        User user = userDAO.getOne(id);
        if (user.isEnabled()) return "Вы уже активировали учетную запись!";
        user.setEnabled(true);
        userDAO.save(user);
        return "Ваш аккаунт активирован, спасибо за регистрацию!";
    }

    @GetMapping("/update/{id}/newemail/{email}")
    public String updateUserEmail(@PathVariable int id,
                                  @PathVariable String email) {
        User user = userDAO.getOne(id);
        user.setEmail(email);
        userDAO.save(user);
        return "Ваша почта успешно изменена!";
    }

    @PatchMapping("/user/{id}/update")
    public void updateUser(@PathVariable int id,
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
            if (emailRequest != null) {
                mailService.sendEmailToChangeMail(email, id);
//                userObj.setEmail(emailRequest);
//                wasUpdated = true;
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
        }
    }

    @GetMapping("/user/get")
    @ResponseBody
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

}
