package com.biogene.mapper;

import org.springframework.stereotype.Component;

import com.biogene.domain.Laboratory;
import com.biogene.dto.LaboratoryRequestDTO;
import com.biogene.dto.LaboratoryResponseDTO;

@Component
public class LaboratoryMapper {

    public Laboratory toEntity(LaboratoryRequestDTO dto) {
        Laboratory laboratory = new Laboratory();
        laboratory.setCnpj(dto.cnpj());
        laboratory.setName(dto.name());
        laboratory.setAddress(dto.address());
        laboratory.setPhone(dto.phone());
        return laboratory;
    }

    public LaboratoryResponseDTO toResponseDTO(Laboratory laboratory) {
        return new LaboratoryResponseDTO(
                laboratory.getId(),
                laboratory.getCnpj(),
                laboratory.getName(),
                laboratory.getAddress(),
                laboratory.getPhone()
        );
    }

}
