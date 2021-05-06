package com.wildcard.back.dao;

import com.wildcard.back.models.Word;
import com.wildcard.back.util.PartOfSpeech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface WordDAO extends JpaRepository<Word, Integer> {
    public Word findByWordAndPartOfSpeech(String word, PartOfSpeech partOfSpeech);
    public List <PartOfSpeech> getPartsOfSpeech(String word);
}
