package com.wildcard.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildcard.back.dao.*;
import com.wildcard.back.models.*;
import com.wildcard.back.service.MailService;
import com.wildcard.back.util.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@RestController
public class UserController {
    private UserDAO userDAO;
    private WordDAO wordDAO;
    private LibDAO libDAO;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;
    CustomLibDAO customLibDAO;

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String nativeLang) {
        if (userDAO.findByEmail(email) != null) return Constants.USER_EXISTS_ALREADY;
        User userObj = new User();
        String passwordRequest = Validation.oneStepValidation(password, Constants.PASSWORD_PATTERN);
        if (passwordRequest == null) return Constants.PASSWORD_DOESNT_FIT;
        else userObj.setPassword(passwordEncoder.encode(passwordRequest));
        //TODO запретить повторную регистрацию
        String emailRequest = Validation.oneStepValidation(email, Constants.EMAIL_PATTERN);
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
        return "Successful logination";
    }

    @GetMapping("/logout")
    public String logout(Principal principal) {
        System.out.println(principal);
        return "success logout";
    }

    @GetMapping("/activate/{id}")
    public String activateUser(@PathVariable int id) {
        if (userDAO.findById(id).isPresent()) {
            User user = userDAO.findById(id).get();
            if (user.isEnabled()) return Constants.USER_IS_ENABLED_ALREADY;
            user.setEnabled(true);
            user.setRoles(Arrays.asList(Role.ROLE_USER));
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
            String emailRequest = Validation.oneStepValidation(email, Constants.EMAIL_PATTERN);
            if (emailRequest != null) {
                user.setEmail(email);
                user.setUsername(email);
                userDAO.save(user);
                return Constants.USER_UPDATE_EMAIL;
            }
        }
        return Constants.USER_NOT_UPDATE_EMAIL;
    }

    @PatchMapping("/user/{id}/adminUpdate")
    public String adminUpdateUser(@PathVariable int id,
                                  @RequestParam String role,
                                  @RequestParam (value = "isEnabled", required = false) boolean isEnabled) {
        User userObj = userDAO.getOne(id);
        boolean wasUpdated = false;

        if (!userObj.getRoles().get(0).equals(Role.valueOf(role))) {
            if (!userObj.getRoles().get(0).toString().equals(role.toUpperCase())) {
                Role roleRequest = Validation.roleValidation(role);
                if (roleRequest != null) {
                    List <Role> roles = userObj.getRoles();
                    roles.set(0, Role.valueOf(role.toUpperCase()));
                    userObj.setRoles(roles);
                    wasUpdated = true;
                }
            }
        }

        if (userObj.isEnabled() != isEnabled) {
            userObj.setEnabled(isEnabled);
            wasUpdated = true;
        }

        if (wasUpdated) {
            userDAO.save(userObj);
            return Constants.USER_UPDATE;
        }
        return Constants.USER_NOT_UPDATE;
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
            String emailRequest = null;
            if (email != null && !email.isEmpty())
                emailRequest = Validation.oneStepValidation(email, Constants.EMAIL_PATTERN);
            if (emailRequest == null) return Constants.EMAIL_DOESNT_FIT;
            mailService.sendEmailToChangeMail(email, id);
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
        if (principal == null) return new User();
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
        return userDAO.getUsersWP(PageRequest.of(page, 2));
    }

    @PostMapping("/user/customlib/word/add")
    public String addToUserCustomLib(Principal principal, @RequestBody String ident) {
        //TODO Change approach @RequestBody
        int id = 0;
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(ident);
        if(matcher.find()) id = Integer.parseInt(matcher.group());

        // {"name": "Favourite", "listOfWords": []}
        int userId = userDAO.findUserByUsername(principal.getName()).getId();
        CustomLib customLib = customLibDAO.findByUserId(userId);
        ObjectMapper obj = new ObjectMapper();
        try {
            FavLib favLib = obj.readValue(customLib.getCustomLib(), FavLib.class);
            List<Integer> listOfWords = favLib.getListOfWords();
            if (wordDAO.findById(id).isPresent()) {
                for (Integer wordId : listOfWords) {
                    if (wordId == id) return Constants.WORD_IS_PRESENT_IN_FAV;
                }
                listOfWords.add(id);
                customLib.setCustomLib(favLib.toString());
                customLibDAO.save(customLib);
                return Constants.WORD_ADDED_TO_FAV;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Constants.WORD_NOT_ADDED_TO_FAV;
    }

    @PostMapping("/user/customlib/add/newword")
    public String addToUserCustLibNewWord(Principal principal,
                                          @RequestParam String word,
                                          @RequestParam String partOfSpeech,
                                          @RequestParam String description,
                                          @RequestParam String example,
                                          @RequestParam String translationRu,
                                          @RequestParam String translationUa) throws IOException {
        int userId = userDAO.findUserByUsername(principal.getName()).getId();
        CustomLib customLib = customLibDAO.findByUserId(userId);
        ObjectMapper obj = new ObjectMapper();
        Word wordObj = new Word();

        String wordRequest = null;
        if (word != null && !word.isEmpty()) wordRequest = Validation.wordValidation(word);
        if (wordRequest == null) return Constants.WORD_DOESNT_FIT;
        wordObj.setWord(wordRequest);

        PartOfSpeech partOfSpeechRequest = Validation.partOfSpeechValidation(partOfSpeech.toUpperCase());
        if (partOfSpeechRequest == null) return Constants.PART_OF_SPEECH_DOESNT_FIT;
        wordObj.setPartOfSpeech(partOfSpeechRequest);

        String descriptionRequest = null;
        if (description != null && !description.isEmpty())
            descriptionRequest = Validation.sentenceValidation(description);
        if (descriptionRequest == null) return Constants.DESCRIPTION_DOESNT_FIT;
        wordObj.setDescription(descriptionRequest);

        String exampleRequest = null;
        if (example != null && !example.isEmpty()) exampleRequest = Validation.sentenceValidation(example);
        if (exampleRequest == null) return Constants.EXAMPLE_DOESNT_FIT;
        wordObj.setExample(exampleRequest);

        Translation translationObj = new Translation(translationRu, translationUa);
        String translation = translationObj.toString();

        String translationRequest = null;
        if (translation != null && !translation.isEmpty())
            translationRequest = Validation.oneStepValidation(translation, Constants.JSON_PATTERN);
        if (translationRequest == null) return Constants.TRANSLATION_DOESNT_FIT;
        wordObj.setTranslation(translationRequest);

        if (wordObj.getWord() != null && wordObj.getPartOfSpeech() != null
                && wordObj.getDescription() != null && wordObj.getExample() != null
                && wordObj.getTranslation() != null) {
            wordDAO.save(wordObj);
            try {
                FavLib favLib = obj.readValue(customLib.getCustomLib(), FavLib.class);
                List<Integer> listOfWords = favLib.getListOfWords();
                Word added = wordDAO.findByWordAndPartOfSpeech(wordObj.getWord(), wordObj.getPartOfSpeech());
                listOfWords.add(added.getId());
                customLib.setCustomLib(favLib.toString());
                customLibDAO.save(customLib);
                return Constants.WORD_ADDED_TO_FAV;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return Constants.WORD_NOT_ADDED_TO_FAV;
    }

    @DeleteMapping("/user/customlib/word/{id}/delete")
    public String deleteFromUserCustomLib(Principal principal, @PathVariable int id) {
        int userId = userDAO.findUserByUsername(principal.getName()).getId();
        CustomLib customLib = customLibDAO.findByUserId(userId);
        ObjectMapper obj = new ObjectMapper();
        try {
            FavLib favLib = obj.readValue(customLib.getCustomLib(), FavLib.class);
            List<Integer> listOfWords = favLib.getListOfWords();
            if (!listOfWords.contains(id)) return Constants.WORD_IS_ABSENT_IN_FAV;
            listOfWords.remove(Integer.valueOf(id));
            customLib.setCustomLib(favLib.toString());
            customLibDAO.save(customLib);
            return Constants.WORD_DELETED_FROM_FAV;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Constants.WORD_NOT_DELETED_FROM_FAV;
    }

    @GetMapping("/user/customlib/get/approved/{isApproved}/page/{page}")
    public PagedListHolder<Word> getUserCustomLib(Principal principal,
                                                  @PathVariable int page,
                                                  @PathVariable boolean isApproved) {
        int userId = userDAO.findUserByUsername(principal.getName()).getId();
        CustomLib customLib = customLibDAO.findByUserId(userId);
        ObjectMapper obj = new ObjectMapper();
        List<Word> favWords = new ArrayList<>();
        try {
            FavLib favLib = obj.readValue(customLib.getCustomLib(), FavLib.class);
            List<Integer> listOfWords = favLib.getListOfWords();
            List<Integer> newListOfWords = favLib.getListOfWords();
            for (int a = 0; a < listOfWords.size(); a++) {
                int wordId = listOfWords.get(a);
                if (wordDAO.findById(wordId).isPresent()) {
                    Word word = wordDAO.findById(wordId).get();
                    if(!isApproved) {
                        favWords.add(word);
                    } else if(!word.isApproved()) {
                        favWords.add(word);
                    }
                } else {
                    newListOfWords.remove(Integer.valueOf(wordId));
                }
            }
            favLib.setListOfWords(newListOfWords);
            customLib.setCustomLib(favLib.toString());
            customLibDAO.save(customLib);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        PagedListHolder<Word> pageHolder = new PagedListHolder<>(favWords);
        pageHolder.setPageSize(3);
        pageHolder.setPage(page);
        return pageHolder;
    }

    @GetMapping("/user/customlibids/get")
    public List<Integer> getUserCustomLibIds(Principal principal) {
        int userId = userDAO.findUserByUsername(principal.getName()).getId();
        CustomLib customLib = customLibDAO.findByUserId(userId);
        ObjectMapper obj = new ObjectMapper();
        List<Integer> favWords = new ArrayList<>();
        try {
            FavLib favLib = obj.readValue(customLib.getCustomLib(), FavLib.class);
            List<Integer> listOfWords = favLib.getListOfWords();
            List<Integer> newListOfWords = favLib.getListOfWords();
            for (int a = 0; a < listOfWords.size(); a++) {
                int wordId = listOfWords.get(a);
                if (wordDAO.findById(wordId).isPresent()) {
                    favWords.add(wordDAO.findById(wordId).get().getId());
                } else {
                    newListOfWords.remove(Integer.valueOf(wordId));
                }
            }
            favLib.setListOfWords(newListOfWords);
            customLib.setCustomLib(favLib.toString());
            customLibDAO.save(customLib);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return favWords;
    }

    @PostMapping("/user/favlib/{id}/add")
    public String addUserLib(Principal principal, @PathVariable int id) {
        User user = userDAO.findUserByUsername(principal.getName());
        List<Lib> libs = user.getLibs();
        if (libDAO.findById(id).isPresent()) {
            Lib lib = libDAO.findById(id).get();
            libs.add(lib);
            user.setLibs(libs);
            userDAO.save(user);
            return Constants.LIB_ADDED_TO_FAV;
        }
        return Constants.LIB_NOT_ADDED_TO_FAV;
    }

    @DeleteMapping("/user/favlib/{id}/delete")
    public String deleteUserLib(Principal principal, @PathVariable int id) {
        User user = userDAO.findUserByUsername(principal.getName());
        List<Lib> libs = user.getLibs();
        if (libDAO.findById(id).isPresent()) {
            Lib lib = libDAO.findById(id).get();
            libs.remove(lib);
            user.setLibs(libs);
            userDAO.save(user);
            return Constants.LIB_DELETED_FROM_FAV;
        }
        return Constants.LIB_NOT_DELETED_FROM_FAV;
    }

    @GetMapping("/user/favlibs/get")
    public List<Lib> getUserLibs(Principal principal) {
        User user = userDAO.findUserByUsername(principal.getName());
        return user.getLibs();
    }
}
