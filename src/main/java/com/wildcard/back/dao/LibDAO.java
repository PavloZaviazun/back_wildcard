package com.wildcard.back.dao;

import com.wildcard.back.models.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface LibDAO extends JpaRepository<Vocabulary, Integer> {
    @Query("SELECT v FROM Lib l JOIN LibWord lw on l.id = lw.libId JOIN Vocabulary v on lw.wordId = v.id WHERE l.name = 'IELTS'")
    List<Vocabulary> getLib();
}
