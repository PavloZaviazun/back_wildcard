package com.wildcard.back.dao;

import com.wildcard.back.models.Word;
import com.wildcard.back.util.Constants;
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
    @Query(Constants.SEARCH_BY_WORD)
    Page<Word> searchByWord(@Param("word") String word, Pageable pageable);
    @Query(Constants.SEARCH_BY_LETTER)
    Page<Word> searchByLetter(@Param("letter") String letter, Pageable pageable);
    @Query(Constants.GET_RANDOM_WORDS)
    List<Word> getRandomWords();
}
