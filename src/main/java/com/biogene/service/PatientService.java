package com.biogene.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.biogene.domain.Patient;
import com.biogene.dto.PatientRequestDTO;
import com.biogene.dto.PatientResponseDTO;
import com.biogene.exception.BusinessException;
import com.biogene.exception.DuplicateResourceException;
import com.biogene.exception.ResourceNotFoundException;
import com.biogene.mapper.PatientMapper;
import com.biogene.repository.ExamRepository;
import com.biogene.repository.PatientRepository;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final ExamRepository examRepository;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponseDTO create(PatientRequestDTO request) {
        if (patientRepository.existsByCpf(request.cpf())) {
            throw new DuplicateResourceException(
                    "O CPF '" + request.cpf() + "' já está cadastrado.");
        }

        validateAllergyDescription(request);
        validateMedicationDescription(request);

        Patient patient = patientMapper.toEntity(request);
        Patient saved = patientRepository.save(patient);
        return patientMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente com ID " + id + " não encontrado."));
        return patientMapper.toResponseDTO(patient);
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO findByCpf(String cpf) {
        Patient patient = patientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente com CPF '" + cpf + "' não encontrado."));
        return patientMapper.toResponseDTO(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDTO> findByName(String name) {
        return patientRepository.findByFullNameContainingIgnoreCase(name)
                .stream()
                .map(patientMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDTO> findAll() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public PatientResponseDTO update(Long id, PatientRequestDTO request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente com ID " + id + " não encontrado."));

        if (!patient.getCpf().equals(request.cpf()) &&
                patientRepository.existsByCpf(request.cpf())) {
            throw new DuplicateResourceException(
                    "O CPF '" + request.cpf() + "' já está cadastrado.");
        }

        validateAllergyDescription(request);
        validateMedicationDescription(request);

        patient.setCpf(request.cpf());
        patient.setFullName(request.fullName());
        patient.setBirthDate(request.birthDate());
        patient.setSex(request.sex());
        patient.setAddress(request.address());
        patient.setPhone(request.phone());
        patient.setEmail(request.email());
        patient.setHasAllergy(request.hasAllergy());
        patient.setAllergyDescription(request.allergyDescription());
        patient.setTakesMedication(request.takesMedication());
        patient.setMedicationDescription(request.medicationDescription());

        return patientMapper.toResponseDTO(patient);
    }

    @Transactional
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente com ID " + id + " não encontrado."));

        long examCount = examRepository.countByPatientId(id);
        if (examCount > 0) {
            throw new BusinessException(
                    "Não é possível excluir o paciente " + patient.getFullName() +
                            ". Existem " + examCount + " exame(s) vinculado(s).");
        }

        patientRepository.delete(patient);
    }

    private void validateAllergyDescription(PatientRequestDTO request) {
        if (Boolean.TRUE.equals(request.hasAllergy()) &&
                (request.allergyDescription() == null ||
                        request.allergyDescription().isBlank())) {
            throw new BusinessException(
                    "A descrição da alergia é obrigatória quando o paciente possui alergia.");
        }
    }

    private void validateMedicationDescription(PatientRequestDTO request) {
        if (Boolean.TRUE.equals(request.takesMedication()) &&
                (request.medicationDescription() == null ||
                        request.medicationDescription().isBlank())) {
            throw new BusinessException(
                    "A descrição do medicamento é obrigatória quando o paciente toma medicamento.");
        }
    }

}
