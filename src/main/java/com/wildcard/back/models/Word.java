package com.wildcard.back.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wildcard.back.util.PartOfSpeech;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = {"libs"})
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"word", "partOfSpeech"})})
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String word;
    @Enumerated(EnumType.STRING)
    private PartOfSpeech partOfSpeech;
    private String description;
    private String example;
    private String image;
    private String translation;
    private boolean isApproved = false;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "word_lib",
    joinColumns = @JoinColumn (name = "word_id"),
    inverseJoinColumns = @JoinColumn (name = "lib_id"))
    @JsonIgnore
    private List <Lib> libs;

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
