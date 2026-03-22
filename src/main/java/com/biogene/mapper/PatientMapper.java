package com.biogene.mapper;

import org.springframework.stereotype.Component;

import com.biogene.domain.Patient;
import com.biogene.dto.PatientRequestDTO;
import com.biogene.dto.PatientResponseDTO;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequestDTO dto) {
        Patient patient = new Patient();
        patient.setCpf(dto.cpf());
        patient.setFullName(dto.fullName());
        patient.setBirthDate(dto.birthDate());
        patient.setSex(dto.sex());
        patient.setAddress(dto.address());
        patient.setPhone(dto.phone());
        patient.setEmail(dto.email());
        patient.setHasAllergy(dto.hasAllergy());
        patient.setAllergyDescription(dto.allergyDescription());
        patient.setTakesMedication(dto.takesMedication());
        patient.setMedicationDescription(dto.medicationDescription());
        return patient;
    }

    public PatientResponseDTO toResponseDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getCpf(),
                patient.getFullName(),
                patient.getBirthDate(),
                patient.getSex(),
                patient.getAddress(),
                patient.getPhone(),
                patient.getEmail(),
                patient.isHasAllergy(),
                patient.getAllergyDescription(),
                patient.isTakesMedication(),
                patient.getMedicationDescription()
        );
    }

}
