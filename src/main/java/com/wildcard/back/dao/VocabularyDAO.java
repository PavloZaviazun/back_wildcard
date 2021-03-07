package com.wildcard.back.dao;

import com.wildcard.back.models.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface VocabularyDAO extends JpaRepository<Vocabulary, Integer> {
}
