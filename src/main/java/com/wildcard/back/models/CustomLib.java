package com.wildcard.back.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
//@EqualsAndHashCode

public class CustomLib {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String customLib;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private User user;

    public CustomLib() {
        this.customLib = "{\"name\": \"Custom\", \"listOfWords\": []}";
    }

    @Override
    public String toString() {
        return "CustomLib{" +
                "id=" + id +
                ", customLib='" + customLib + '\'' +
                ", userId=" + user.getId() +
                '}';
    }
}
