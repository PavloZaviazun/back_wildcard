package com.wildcard.back.dao;

import com.wildcard.back.models.Lib;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface LibDAO extends JpaRepository<Lib, Integer> {
    @Query("SELECT l FROM Lib l WHERE l.name LIKE %:name%")
    List <Lib> searchLib(@Param("name") String name);
    @Query("SELECT l FROM Lib l ORDER BY l.name")
    Page <Lib> getLibsWP(Pageable pageable);
}
