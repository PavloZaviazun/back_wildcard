package com.wildcard.back.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wildcard.back.util.NativeLang;
import com.wildcard.back.util.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"libs", "authTokens", "customLibs"})
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private NativeLang nativeLang;
    private boolean isEnabled = false;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List <Role> roles = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set <AuthToken> authTokens = new HashSet <>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Set<CustomLib> customLibs = new HashSet<>(Arrays.asList(new CustomLib()));
    @ManyToMany
    @JoinTable(name = "user_lib",
    joinColumns = @JoinColumn(name = "user"),
    inverseJoinColumns = @JoinColumn(name = "lib"))
    @JsonIgnore
    private List<Lib> libs;

    public User(String password, String username, NativeLang nativeLang) {
        this.password = password;
        this.username = username;
        this.nativeLang = nativeLang;
    }

    @Override
    public Collection <? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
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

}
