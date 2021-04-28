package com.wildcard.back.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;
    private String password;
    private String email;
    // TODO Is it in right way?
    private NativeLanguage nativeLang;
    private boolean isActive;
    private int roleId;

    public User(String login, String password, String email, NativeLanguage nativeLang, boolean isActive, int roleId) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.nativeLang = nativeLang;
        this.isActive = isActive;
        this.roleId = roleId;
    }
}
