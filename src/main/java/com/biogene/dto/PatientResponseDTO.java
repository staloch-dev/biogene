package com.biogene.dto;

import java.time.LocalDate;

import com.biogene.enums.Sex;

public record PatientResponseDTO(
        Long id,
        String cpf,
        String fullName,
        LocalDate birthDate,
        Sex sex,
        String address,
        String phone,
        String email,
        boolean hasAllergy,
        String allergyDescription,
        boolean takesMedication,
        String medicationDescription
) {}
