package com.wildcard.back;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import net.minidev.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

@SpringBootApplication
public class BackApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(BackApplication.class, args);


    }

}
