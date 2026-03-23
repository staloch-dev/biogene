package com.biogene.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.biogene.enums.ExamStatus;
import com.biogene.enums.ExamType;
import com.biogene.enums.SampleType;

public record ExamRequestDTO(

        @NotNull(message = "O ID do paciente é obrigatório.")
        Long patientId,

        @NotNull(message = "O ID do laboratório é obrigatório.")
        Long laboratoryId,

        @NotNull(message = "O tipo do exame é obrigatório.")
        ExamType examType,

        SampleType sampleType,

        ExamStatus status,

        @NotNull(message = "A data e hora da coleta são obrigatórias.")
        LocalDateTime examDate,

        @NotNull(message = "A data de entrega do resultado é obrigatória.")
        @Future(message = "A data de entrega deve ser uma data futura.")
        LocalDate resultDate,

        @Size(max = 100, message = "O método de análise deve ter no máximo 100 caracteres.")
        String analysisMethod,

        String result,

        @DecimalMin(value = "0.00", message = "O valor do exame não pode ser negativo.")
        @Digits(integer = 5, fraction = 2,
                message = "O valor deve ter no máximo 5 dígitos inteiros e 2 decimais.")
        BigDecimal examValue

) {}
