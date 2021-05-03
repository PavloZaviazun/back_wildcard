package com.wildcard.back.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class CustomLib {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String favLib;
    @ManyToOne
    private User user;
}
