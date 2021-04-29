package com.wildcard.back.dao;

import com.wildcard.back.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface WordDAO extends JpaRepository<Word, Integer> {
    @Query("SELECT w FROM Lib l JOIN LibWord lw on l.id = lw.libId JOIN Word w on lw.wordId = w.id WHERE l.id = :id")
    List <Word> getLibWords(@Param("id") int id);
}
