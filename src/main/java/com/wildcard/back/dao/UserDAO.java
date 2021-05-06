package com.wildcard.back.dao;

import com.wildcard.back.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface UserDAO extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);
}
