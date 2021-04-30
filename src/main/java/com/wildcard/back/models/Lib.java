package com.wildcard.back.models;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString(exclude = {"words"})
public class Lib {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "word_lib",
            joinColumns = @JoinColumn (name = "lib_id"),
            inverseJoinColumns = @JoinColumn (name = "word_id"))
    @JsonIgnore
    private List <Word> words;

    public Lib(String name) {
        this.name = name;
    }
}
