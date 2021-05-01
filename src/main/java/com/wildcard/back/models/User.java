package com.wildcard.back.models;

import com.wildcard.back.util.NativeLang;
import lombok.*;

import javax.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private NativeLang nativeLang;
    private boolean isActive = false;
    private int roleId;

    public User(String login, String password, String email, NativeLang nativeLang, int roleId) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.nativeLang = nativeLang;
        this.roleId = roleId;
    }
}
