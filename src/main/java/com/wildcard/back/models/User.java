package com.wildcard.back.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wildcard.back.util.NativeLang;
import com.wildcard.back.util.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"libs"})
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;
    private String password;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private NativeLang nativeLang;
    private boolean isEnabled = true;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List <Role> role = Arrays.asList(Role.ROLE_USER);
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set <AuthToken> authTokens = new HashSet <>();
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<CustomLib> customLibs;
    @ManyToMany
    @JoinTable(name = "user_lib",
    joinColumns = @JoinColumn(name = "user"),
    inverseJoinColumns = @JoinColumn(name = "lib"))
    @JsonIgnore
    private List<Lib> libs;

    public User(String password, String email, NativeLang nativeLang) {
        this.password = password;
        this.email = email;
        this.nativeLang = nativeLang;
    }

    @Override
    public Collection <? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email.split("@")[0];
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
