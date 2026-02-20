package com.translator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "translations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String inputData;

    private String inputType;

    @Lob
    private String fhirOutput;

    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();
}