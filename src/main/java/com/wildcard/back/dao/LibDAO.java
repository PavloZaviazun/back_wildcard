package com.wildcard.back.dao;

import com.wildcard.back.models.Lib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface LibDAO extends JpaRepository<Lib, Integer> {
}
