package com.wildcard.back.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class MainService {
    private MainService() {}

    public static void saveImage(MultipartFile image) {
        try {
            image.transferTo(new File(System.getProperty("user.home") + File.separator + "cardImages"
                    + File.separator + image.getOriginalFilename()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
