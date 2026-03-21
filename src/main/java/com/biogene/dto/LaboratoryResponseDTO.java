package com.biogene.dto;

public record LaboratoryResponseDTO(
        Long id,
        String cnpj,
        String name,
        String address,
        String phone
) {}
