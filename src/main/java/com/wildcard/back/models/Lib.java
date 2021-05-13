package com.wildcard.back.models;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString(exclude = {"words", "users"})
public class Lib {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "word_lib",
            joinColumns = @JoinColumn (name = "lib_id"),
            inverseJoinColumns = @JoinColumn (name = "word_id"))
    @JsonIgnore
    private List <Word> words;
    @ManyToMany
    @JoinTable(name = "user_lib",
    joinColumns = @JoinColumn(name = "lib"),
    inverseJoinColumns = @JoinColumn(name = "user"))
    @JsonIgnore
    private List<User> users;
    private LocalDate createDate;
    private LocalDateTime updateDate;

    public Lib(String name) {
        this.name = name;
        createDate = LocalDate.now();
        updateDate = LocalDateTime.now();
    }
}
