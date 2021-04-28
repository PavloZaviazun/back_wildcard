package com.wildcard.back.dao;

import com.wildcard.back.models.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface VocabularyDAO extends JpaRepository<Vocabulary, Integer> {
    @Query("SELECT v FROM Lib l JOIN LibWord lw on l.id = lw.libId JOIN Vocabulary v on lw.wordId = v.id WHERE l.id = :id")
    List <Vocabulary> getLibWords(@Param("id") int id);
}
