package com.wildcard.back.models;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CustomLib {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String favLib;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
}
