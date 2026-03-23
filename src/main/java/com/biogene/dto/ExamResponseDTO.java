package com.biogene.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.biogene.enums.ExamCategory;
import com.biogene.enums.ExamStatus;
import com.biogene.enums.ExamType;
import com.biogene.enums.SampleType;


public record ExamResponseDTO(
        Long id,
        Long patientId,
        String patientName,
        Long laboratoryId,
        String laboratoryName,
        ExamCategory examCategory,
        ExamType examType,
        SampleType sampleType,
        ExamStatus status,
        LocalDateTime examDate,
        LocalDate resultDate,
        String analysisMethod,
        String result,
        BigDecimal examValue
) {}
