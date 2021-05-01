package com.wildcard.back.dao;

import com.wildcard.back.models.Lib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface LibDAO extends JpaRepository<Lib, Integer> {
//    @Query("select lib_id from word_lib where word_id = :idWord")
//    public void getLibsOfWord(int idWord);
}
