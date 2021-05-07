package com.wildcard.back.dao;

import com.wildcard.back.models.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface AuthTokenDAO extends JpaRepository<AuthToken, Integer> {
    AuthToken findByToken(String token);
}
