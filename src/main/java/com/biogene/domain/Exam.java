package com.biogene.domain;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.biogene.enums.ExamCategory;
import com.biogene.enums.ExamType;
import com.biogene.enums.SampleType;
import com.biogene.enums.ExamStatus;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private ExamCategory examCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ExamType examType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private SampleType sampleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ExamStatus status;

    @Column(nullable = false)
    private LocalDateTime examDate;

    @Column(nullable = false)
    private LocalDate resultDate;

    @Column(length = 100)
    private String analysisMethod;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(precision = 7, scale = 2)
    private BigDecimal examValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratory_id", nullable = false)
    private Laboratory laboratory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

}
