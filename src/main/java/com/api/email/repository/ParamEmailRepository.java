package com.api.email.repository;

import com.api.email.entity.ParamEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParamEmailRepository extends JpaRepository<ParamEmailEntity, Integer> {

    @Query(value = """
            SELECT id, mailHost, port, useAuth, mailUser, password, isActive, isMain
            FROM tb_paramEmail
            WHERE isActive = true
            AND isMain = true
            """, nativeQuery = true)
    Optional<ParamEmailEntity> findActiveMainEmail();

}
