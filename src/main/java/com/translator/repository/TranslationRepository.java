package com.translator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.translator.entity.TranslationEntity;

@Repository
public interface TranslationRepository
        extends JpaRepository<TranslationEntity, Long> {
}