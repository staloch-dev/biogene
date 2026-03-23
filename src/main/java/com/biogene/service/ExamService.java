package com.biogene.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.biogene.domain.Exam;
import com.biogene.domain.Laboratory;
import com.biogene.domain.Patient;
import com.biogene.dto.ExamCatalogCategoryDTO;
import com.biogene.dto.ExamCatalogTypeDTO;
import com.biogene.dto.ExamRequestDTO;
import com.biogene.dto.ExamResponseDTO;
import com.biogene.enums.ExamCategory;
import com.biogene.enums.ExamStatus;
import com.biogene.enums.ExamType;
import com.biogene.enums.SampleType;
import com.biogene.exception.BusinessException;
import com.biogene.exception.ResourceNotFoundException;
import com.biogene.mapper.ExamMapper;
import com.biogene.repository.ExamRepository;
import com.biogene.repository.LaboratoryRepository;
import com.biogene.repository.PatientRepository;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final PatientRepository patientRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final ExamMapper examMapper;

    @Transactional
    public ExamResponseDTO create(ExamRequestDTO request) {

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente com ID " + request.patientId() + " não encontrado."));

        Laboratory laboratory = laboratoryRepository.findById(request.laboratoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Laboratório com ID " + request.laboratoryId() + " não encontrado."));

        validateResultDate(request);

        if (request.result() != null && !request.result().isBlank()) {
            throw new BusinessException(
                    "Não é possível informar o resultado ao criar o exame. " +
                            "Use o endpoint de atualização quando o exame estiver concluído.");
        }

        SampleType sampleType = resolveSampleType(request);

        Exam exam = examMapper.toEntity(request, patient, laboratory);
        exam.setExamCategory(request.examType().getCategory());
        exam.setSampleType(sampleType);
        exam.setStatus(ExamStatus.AGENDADO);

        Exam saved = examRepository.save(exam);

        return examMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public ExamResponseDTO findById(Long id) {

        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exame com ID " + id + " não encontrado."));

        return examMapper.toResponseDTO(exam);
    }

    @Transactional(readOnly = true)
    public List<ExamResponseDTO> findAll() {

        return examRepository.findAll()
                .stream()
                .map(examMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExamResponseDTO> findByPatientId(Long patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Paciente com ID " + patientId + " não encontrado.");
        }

        return examRepository.findByPatientId(patientId)
                .stream()
                .map(examMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExamResponseDTO> findByLaboratoryId(Long laboratoryId) {

        if (!laboratoryRepository.existsById(laboratoryId)) {
            throw new ResourceNotFoundException(
                    "Laboratório com ID " + laboratoryId + " não encontrado.");
        }

        return examRepository.findByLaboratoryId(laboratoryId)
                .stream()
                .map(examMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExamResponseDTO> findByStatus(ExamStatus status) {

        return examRepository.findByStatus(status)
                .stream()
                .map(examMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExamCatalogCategoryDTO> findCatalog() {
        return Arrays.stream(ExamCategory.values())
                .map(this::toCatalogCategoryDTO)
                .filter(catalog -> !catalog.exams().isEmpty())
                .toList();
    }

    @Transactional
    public ExamResponseDTO update(Long id, ExamRequestDTO request) {

        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exame com ID " + id + " não encontrado."));

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente com ID " + request.patientId() + " não encontrado."));

        Laboratory laboratory = laboratoryRepository.findById(request.laboratoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Laboratório com ID " + request.laboratoryId() + " não encontrado."));

        validateResultDate(request);

        if (ExamStatus.CONCLUIDO.equals(exam.getStatus())) {
            throw new BusinessException(
                    "Não é possível alterar um exame já concluído.");
        }

        if (ExamStatus.CANCELADO.equals(exam.getStatus())) {
            throw new BusinessException(
                    "Não é possível alterar um exame cancelado.");
        }

        if (ExamStatus.CONCLUIDO.equals(request.status()) &&
                (request.result() == null || request.result().isBlank())) {
            throw new BusinessException(
                    "Não é possível concluir o exame sem informar o resultado.");
        }

        SampleType sampleType = resolveSampleType(request);

        exam.setPatient(patient);
        exam.setLaboratory(laboratory);
        exam.setExamCategory(request.examType().getCategory());
        exam.setExamType(request.examType());
        exam.setSampleType(sampleType);
        exam.setExamDate(request.examDate());
        exam.setResultDate(request.resultDate());
        exam.setAnalysisMethod(request.analysisMethod());
        exam.setResult(request.result());
        exam.setStatus(request.status());
        exam.setExamValue(request.examValue());

        return examMapper.toResponseDTO(exam);
    }

    @Transactional
    public void delete(Long id) {

        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exame com ID " + id + " não encontrado."));

        if (ExamStatus.CONCLUIDO.equals(exam.getStatus())) {
            throw new BusinessException(
                    "Não é possível excluir um exame já concluído. " +
                            "Use o cancelamento se necessário.");
        }

        examRepository.delete(exam);
    }

    private void validateResultDate(ExamRequestDTO request) {
        if (!request.resultDate().isAfter(request.examDate().toLocalDate())) {
            throw new BusinessException(
                    "A data de entrega do resultado deve ser posterior à data de coleta.");
        }
    }

    private SampleType resolveSampleType(ExamRequestDTO request) {
        if (request.sampleType() == null) {
            return request.examType().getDefaultSample();
        }
        if (!request.examType().getCompatibleSamples().contains(request.sampleType())) {
            throw new BusinessException(
                    "A amostra '" + request.sampleType() + "' não é compatível com o exame '"
                            + request.examType() + "'.");
        }
        return request.sampleType();
    }

    private ExamCatalogCategoryDTO toCatalogCategoryDTO(ExamCategory category) {
        List<ExamCatalogTypeDTO> items = Arrays.stream(ExamType.values())
                .filter(type -> type.getCategory() == category)
                .map(type -> new ExamCatalogTypeDTO(
                        type,
                        type.getDefaultSample(),
                        type.getCompatibleSamples()))
                .toList();
        return new ExamCatalogCategoryDTO(category, items);
    }

}
