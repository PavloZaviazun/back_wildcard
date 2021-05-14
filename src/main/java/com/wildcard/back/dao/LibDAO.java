package com.wildcard.back.dao;

import com.wildcard.back.models.Lib;
import com.wildcard.back.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface LibDAO extends JpaRepository<Lib, Integer> {
    @Query(Constants.SEARCH_LIB)
    List <Lib> searchLib(@Param("name") String name);
    @Query(Constants.GET_LIBS_WITH_PAGINATION)
    Page <Lib> getLibsWP(Pageable pageable);
    @Query(Constants.FIND_ONE_LIB)
    Lib findLib(@Param("name") String name);
}
