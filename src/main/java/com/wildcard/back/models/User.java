package com.wildcard.back.models;

import com.wildcard.back.util.NativeLang;
import com.wildcard.back.util.Role;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private NativeLang nativeLang;
    private boolean isActive = false;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List <Role> role = Arrays.asList(Role.ROLE_USER);
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set <AuthToken> authTokens = new HashSet <>();

    public User(String password, String email, NativeLang nativeLang) {
        this.password = password;
        this.email = email;
        this.nativeLang = nativeLang;
    }
}
