package com.wildcard.back.util;

import lombok.AllArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@AllArgsConstructor
public class QueryService {
    private EntityManager entityManager;

    public List <Integer> selectLibId(int idWord) {
        Query query = entityManager.createNativeQuery("SELECT lib_id from word_lib where word_id = ?");
        query.setParameter(1, idWord);
        return query.getResultList();
    }

    public void selectWordId(int id) {
        Query query = entityManager.createNativeQuery("SELECT word_id FROM word_lib WHERE lib_id = ?");
        query.setParameter(1, id);
        List<Integer> resultList = query.getResultList();
    }
}
