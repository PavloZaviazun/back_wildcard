package com.wildcard.back.service;

import com.wildcard.back.util.Constants;
import lombok.AllArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@AllArgsConstructor
public class QueryService {
    private EntityManager entityManager;

    public List <Integer> selectLibsId(int idWord) {
        Query query = entityManager.createNativeQuery(Constants.GET_LIB_ID_BY_WORD_ID);
        query.setParameter(1, idWord);
        return query.getResultList();
    }

    public List<Integer> selectWordsId(int id) {
        Query query = entityManager.createNativeQuery(Constants.GET_WORD_ID_BY_LIB_ID);
        query.setParameter(1, id);
        return query.getResultList();
    }
}
