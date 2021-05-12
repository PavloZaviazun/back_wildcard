package com.wildcard.back.controller;

import com.wildcard.back.service.MailService;
import com.wildcard.back.util.NativeLang;
import com.wildcard.back.util.PartOfSpeech;
import com.wildcard.back.util.Role;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
public class MainController {
    private MailService mailService;

    @GetMapping("/partsOfSpeech")
    public String[] getPartsOfSpeech() {
        PartOfSpeech[] values = PartOfSpeech.values();
        String[] array = new String[values.length];
        for(int i = 0; i < values.length; i++) {
            array[i] = String.valueOf(values[i]);
        }
        return array;
    }

    @GetMapping("/langs")
    public String[] getAllLang() {
        NativeLang[] values = NativeLang.values();
        String[] array = new String[values.length];
        for (int i = 0; i < values.length; i++){
            array[i] = String.valueOf(values[i]);
        }
        return array;
    }

    @PostMapping("/feedback")
    public String sendFeedback(@RequestParam String email,
                               @RequestParam String theme,
                               @RequestParam String message) {
        mailService.sendFeedback(email, theme, message);
        return "Ваше повідомлення отримано, дякуємо за зворотній зв'язок";
    }

    @GetMapping("/roles")
    public String[] getRoles() {
        Role[] values = Role.values();
        String[] array = new String[values.length];
        for(int i = 0; i < values.length; i++) {
            array[i] = String.valueOf(values[i]);
        }
        return array;
    }
}
