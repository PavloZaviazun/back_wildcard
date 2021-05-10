package com.wildcard.back.dao;

import com.wildcard.back.models.User;
import com.wildcard.back.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface UserDAO extends JpaRepository<User, Integer> {
    User findUserByUsername(String s);
    @Query(Constants.GET_USERS_WITH_PAGINATION)
    Page <User> getUsersWP(Pageable pageable);
    User findByEmail(String email);
}
