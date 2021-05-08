package com.wildcard.back.dao;

import com.wildcard.back.models.Word;
import com.wildcard.back.util.PartOfSpeech;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface WordDAO extends JpaRepository<Word, Integer> {
    Word findByWordAndPartOfSpeech(String word, PartOfSpeech partOfSpeech);
    List<Word> findByWord(String word);
    @Query("SELECT w FROM Word w WHERE w.word LIKE %:word%")
    List<Word> searchByWord(@Param("word") String word);
    @Query("SELECT w FROM Word w WHERE w.word LIKE :letter% ORDER BY w.word")
    Page<Word> searchByLetter(@Param("letter") String letter, Pageable pageable);
}
