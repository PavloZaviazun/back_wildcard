package com.wildcard.back.dao;

import com.wildcard.back.models.CustomLib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface CustomLibDAO extends JpaRepository<CustomLib, Integer> {
    CustomLib findByUserId(int i);
}

