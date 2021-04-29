package com.wildcard.back.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.IOException;
import java.io.StringWriter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String word;
    private PartOfSpeech partOfSpeech;
    private String description;
    private String example;
    private String image;
    private String translation;

    public Word(String word, PartOfSpeech partOfSpeech, String description, String example, String image, String translation) throws IOException {
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.description = description;
        this.example = example;
        this.image = image;
        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, translation);
        this.translation = sw.toString();
    }

}
