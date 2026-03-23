package com.biogene.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.biogene.domain.Exam;
import com.biogene.domain.Laboratory;
import com.biogene.domain.Patient;
import com.biogene.dto.ExamCatalogCategoryDTO;
import com.biogene.dto.ExamRequestDTO;
import com.biogene.dto.ExamResponseDTO;
import com.biogene.enums.ExamCategory;
import com.biogene.enums.ExamStatus;
import com.biogene.enums.ExamType;
import com.biogene.enums.SampleType;
import com.biogene.enums.Sex;
import com.biogene.exception.BusinessException;
import com.biogene.exception.ResourceNotFoundException;
import com.biogene.mapper.ExamMapper;
import com.biogene.repository.ExamRepository;
import com.biogene.repository.LaboratoryRepository;
import com.biogene.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExamService - Testes Unitários")
class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ExamMapper examMapper;

    @InjectMocks
    private ExamService examService;

    private static final Long EXISTING_EXAM_ID = 1L;
    private static final Long EXISTING_LABORATORY_ID = 1L;
    private static final Long EXISTING_PATIENT_ID = 1L;
    private static final Long NON_EXISTING_EXAM_ID = 99L;
    private static final Long NON_EXISTING_LABORATORY_ID = 99L;
    private static final Long NON_EXISTING_PATIENT_ID = 99L;

    private Patient patient;
    private Laboratory laboratory;
    private Exam exam;
    private Exam unsavedExam;
    private ExamRequestDTO requestDTO;
    private ExamResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        patient = new Patient();
        patient.setId(EXISTING_PATIENT_ID);
        patient.setFullName("Johnny Test");
        patient.setBirthDate(LocalDate.of(1994, 6, 21));
        patient.setSex(Sex.MASCULINO);
        patient.setAddress("Rua Porkbelly, 11, Casa dos Test, Porkbelly, São Paulo - SP");
        patient.setPhone("1134567890");

        laboratory = new Laboratory();
        laboratory.setId(EXISTING_LABORATORY_ID);
        laboratory.setName("BioGene Centro");
        laboratory.setAddress("Avenida Cruzeiro, 7, 3º Andar, Zona Norte, São Paulo - SP");
        laboratory.setPhone("1134567890");

        requestDTO = new ExamRequestDTO(
                EXISTING_PATIENT_ID,
                EXISTING_LABORATORY_ID,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                ExamStatus.AGENDADO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                "Citometria de Fluxo",
                null,
                new BigDecimal("45.00"));

        unsavedExam = new Exam();
        unsavedExam.setPatient(patient);
        unsavedExam.setLaboratory(laboratory);
        unsavedExam.setExamCategory(ExamCategory.HEMATOLOGIA);
        unsavedExam.setExamType(ExamType.HEMOGRAMA);
        unsavedExam.setSampleType(SampleType.SANGUE);
        unsavedExam.setStatus(ExamStatus.AGENDADO);
        unsavedExam.setExamDate(LocalDateTime.of(2024, 1, 15, 8, 30));
        unsavedExam.setResultDate(LocalDate.of(2024, 1, 17));
        unsavedExam.setAnalysisMethod("Citometria de Fluxo");
        unsavedExam.setExamValue(new BigDecimal("45.00"));

        exam = new Exam();
        exam.setId(EXISTING_EXAM_ID);
        exam.setPatient(patient);
        exam.setLaboratory(laboratory);
        exam.setExamCategory(ExamCategory.HEMATOLOGIA);
        exam.setExamType(ExamType.HEMOGRAMA);
        exam.setSampleType(SampleType.SANGUE);
        exam.setStatus(ExamStatus.AGENDADO);
        exam.setExamDate(LocalDateTime.of(2024, 1, 15, 8, 30));
        exam.setResultDate(LocalDate.of(2024, 1, 17));
        exam.setAnalysisMethod("Citometria de Fluxo");
        exam.setExamValue(new BigDecimal("45.00"));

        responseDTO = new ExamResponseDTO(
                EXISTING_EXAM_ID,
                EXISTING_PATIENT_ID,
                "Johnny Test",
                EXISTING_LABORATORY_ID,
                "BioGene Centro",
                ExamCategory.HEMATOLOGIA,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                ExamStatus.AGENDADO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                "Citometria de Fluxo",
                null,
                new BigDecimal("45.00"));
    }

    @Test
    @DisplayName("Deve criar exame e retornar ExamResponseDTO com sucesso")
    void shouldCreateExamAndReturnResponseDTOSuccessfully() {

        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(EXISTING_LABORATORY_ID))
                .thenReturn(Optional.of(laboratory));
        when(examMapper.toEntity(any(ExamRequestDTO.class), any(Patient.class), any(Laboratory.class)))
                .thenReturn(unsavedExam);
        when(examRepository.save(any(Exam.class)))
                .thenReturn(exam);
        when(examMapper.toResponseDTO(any(Exam.class)))
                .thenReturn(responseDTO);

        ExamResponseDTO result = examService.create(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(EXISTING_EXAM_ID);
        assertThat(result.patientName()).isEqualTo("Johnny Test");
        assertThat(result.laboratoryName()).isEqualTo("BioGene Centro");
        assertThat(result.examType()).isEqualTo(ExamType.HEMOGRAMA);
        assertThat(result.examCategory()).isEqualTo(ExamCategory.HEMATOLOGIA);
        assertThat(result.status()).isEqualTo(ExamStatus.AGENDADO);

        verify(patientRepository, times(1)).findById(EXISTING_PATIENT_ID);
        verify(laboratoryRepository, times(1)).findById(EXISTING_LABORATORY_ID);
        verify(examRepository, times(1)).save(any(Exam.class));
        verify(examMapper, times(1)).toResponseDTO(any(Exam.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando paciente não existe")
    void shouldThrowResourceNotFoundExceptionWhenPatientNotExists() {

        when(patientRepository.findById(NON_EXISTING_PATIENT_ID))
                .thenReturn(Optional.empty());

        ExamRequestDTO invalidRequest = new ExamRequestDTO(
                NON_EXISTING_PATIENT_ID,
                EXISTING_LABORATORY_ID,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                null,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null, null);

        assertThatThrownBy(() -> examService.create(invalidRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(NON_EXISTING_PATIENT_ID));

        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando laboratório não existe")
    void shouldThrowResourceNotFoundExceptionWhenLaboratoryNotExists() {

        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(NON_EXISTING_LABORATORY_ID))
                .thenReturn(Optional.empty());

        ExamRequestDTO invalidRequest = new ExamRequestDTO(
                EXISTING_PATIENT_ID,
                NON_EXISTING_LABORATORY_ID,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                null,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null, null);

        assertThatThrownBy(() -> examService.create(invalidRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(NON_EXISTING_LABORATORY_ID));

        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    @DisplayName("Deve usar amostra padrão do tipo de exame quando sampleType não é informado")
    void shouldUseDefaultSampleWhenSampleTypeIsNull() {

        ExamRequestDTO requestWithoutSample = new ExamRequestDTO(
                EXISTING_PATIENT_ID,
                EXISTING_LABORATORY_ID,
                ExamType.HEMOGRAMA,
                null,
                ExamStatus.AGENDADO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null, null);

        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(EXISTING_LABORATORY_ID))
                .thenReturn(Optional.of(laboratory));
        when(examMapper.toEntity(any(ExamRequestDTO.class), any(Patient.class), any(Laboratory.class)))
                .thenReturn(unsavedExam);
        when(examRepository.save(any(Exam.class)))
                .thenReturn(exam);
        when(examMapper.toResponseDTO(any(Exam.class)))
                .thenReturn(responseDTO);

        examService.create(requestWithoutSample);

        ArgumentCaptor<Exam> examCaptor = ArgumentCaptor.forClass(Exam.class);
        verify(examRepository).save(examCaptor.capture());

        assertThat(examCaptor.getValue().getSampleType())
                .isEqualTo(ExamType.HEMOGRAMA.getDefaultSample());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando amostra é incompatível com o tipo de exame")
    void shouldThrowBusinessExceptionWhenSampleTypeIsIncompatible() {

        ExamRequestDTO requestWithIncompatibleSample = new ExamRequestDTO(
                EXISTING_PATIENT_ID,
                EXISTING_LABORATORY_ID,
                ExamType.HEMOGRAMA,
                SampleType.URINA,
                ExamStatus.AGENDADO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null, null);

        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(EXISTING_LABORATORY_ID))
                .thenReturn(Optional.of(laboratory));

        assertThatThrownBy(() -> examService.create(requestWithIncompatibleSample))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(SampleType.URINA + "' não é compatível com o exame '" + ExamType.HEMOGRAMA);

        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando resultDate não é posterior à examDate")
    void shouldThrowBusinessExceptionWhenResultDateIsNotAfterExamDate() {

        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(EXISTING_LABORATORY_ID))
                .thenReturn(Optional.of(laboratory));

        ExamRequestDTO invalidRequest = new ExamRequestDTO(
                EXISTING_PATIENT_ID,
                EXISTING_LABORATORY_ID,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                null,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 15),
                null, null, null);

        assertThatThrownBy(() -> examService.create(invalidRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("A data de entrega do resultado deve ser posterior à data de coleta");

        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando resultado é informado na criação do exame")
    void shouldThrowBusinessExceptionWhenResultIsProvidedOnCreate() {

        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(EXISTING_LABORATORY_ID))
                .thenReturn(Optional.of(laboratory));

        ExamRequestDTO invalidRequest = new ExamRequestDTO(
                EXISTING_PATIENT_ID,
                EXISTING_LABORATORY_ID,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                null,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null,
                "Não é possível informar o resultado ao criar o exame",
                null);

        assertThatThrownBy(() -> examService.create(invalidRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("resultado");

        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    @DisplayName("Deve retornar ExamResponseDTO quando exame existe")
    void shouldReturnResponseDTOWhenExamExists() {

        when(examRepository.findById(EXISTING_EXAM_ID))
                .thenReturn(Optional.of(exam));
        when(examMapper.toResponseDTO(any(Exam.class)))
                .thenReturn(responseDTO);

        ExamResponseDTO result = examService.findById(EXISTING_EXAM_ID);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(EXISTING_EXAM_ID);
        assertThat(result.examType()).isEqualTo(ExamType.HEMOGRAMA);

        verify(examRepository, times(1)).findById(EXISTING_EXAM_ID);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando exame não existe")
    void shouldThrowResourceNotFoundExceptionWhenExamNotExists() {

        when(examRepository.findById(NON_EXISTING_EXAM_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> examService.findById(NON_EXISTING_EXAM_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(NON_EXISTING_EXAM_ID));

        verify(examMapper, never()).toResponseDTO(any(Exam.class));
    }

    @Test
    @DisplayName("Deve retornar exames quando filtrado por paciente")
    void shouldReturnExamsWhenFilteredByPatientId() {

        when(patientRepository.existsById(EXISTING_PATIENT_ID))
                .thenReturn(true);
        when(examRepository.findByPatientId(EXISTING_PATIENT_ID))
                .thenReturn(List.of(exam));
        when(examMapper.toResponseDTO(exam))
                .thenReturn(responseDTO);

        List<ExamResponseDTO> result = examService.findByPatientId(EXISTING_PATIENT_ID);

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).patientName()).isEqualTo("Johnny Test");

        verify(examRepository, times(1)).findByPatientId(EXISTING_PATIENT_ID);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao filtrar por paciente inexistente")
    void shouldThrowResourceNotFoundExceptionWhenFilteringByNonExistentPatient() {

        when(patientRepository.existsById(NON_EXISTING_PATIENT_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> examService.findByPatientId(NON_EXISTING_PATIENT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(NON_EXISTING_PATIENT_ID));

        verify(examRepository, never()).findByPatientId(any());
    }

    @Test
    @DisplayName("Deve retornar exames quando filtrado por laboratório")
    void shouldReturnExamsWhenFilteredByLaboratoryId() {

        when(laboratoryRepository.existsById(EXISTING_LABORATORY_ID))
                .thenReturn(true);
        when(examRepository.findByLaboratoryId(EXISTING_LABORATORY_ID))
                .thenReturn(List.of(exam));
        when(examMapper.toResponseDTO(exam))
                .thenReturn(responseDTO);

        List<ExamResponseDTO> result = examService.findByLaboratoryId(EXISTING_LABORATORY_ID);

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).laboratoryName()).isEqualTo("BioGene Centro");

        verify(examRepository, times(1)).findByLaboratoryId(EXISTING_LABORATORY_ID);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao filtrar por laboratório inexistente")
    void shouldThrowResourceNotFoundExceptionWhenFilteringByNonExistentLaboratory() {

        when(laboratoryRepository.existsById(NON_EXISTING_LABORATORY_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> examService.findByLaboratoryId(NON_EXISTING_LABORATORY_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(NON_EXISTING_LABORATORY_ID));

        verify(examRepository, never()).findByLaboratoryId(any());
    }

    @Test
    @DisplayName("Deve retornar exames quando filtrado por status")
    void shouldReturnExamsWhenFilteredByStatus() {

        when(examRepository.findByStatus(ExamStatus.AGENDADO))
                .thenReturn(List.of(exam));
        when(examMapper.toResponseDTO(exam))
                .thenReturn(responseDTO);

        List<ExamResponseDTO> result = examService.findByStatus(ExamStatus.AGENDADO);

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(ExamStatus.AGENDADO);

        verify(examRepository, times(1)).findByStatus(ExamStatus.AGENDADO);
    }

    @Test
    @DisplayName("Deve retornar catálogo agrupado por categoria")
    void shouldReturnCatalogWithAllExamCategories() {

        List<ExamCatalogCategoryDTO> result = examService.findCatalog();

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.stream().map(ExamCatalogCategoryDTO::category))
                .contains(ExamCategory.HEMATOLOGIA, ExamCategory.BIOQUIMICA,
                        ExamCategory.IMUNOLOGIA, ExamCategory.HORMONAL);
        assertThat(result.stream().allMatch(c -> !c.exams().isEmpty())).isTrue();
    }

    @Test
    @DisplayName("Deve retornar lista de ExamResponseDTO quando existem exames")
    void shouldReturnListOfResponseDTOsWhenExamsExist() {

        Exam exam2 = new Exam();
        exam2.setId(2L);
        exam2.setPatient(patient);
        exam2.setLaboratory(laboratory);
        exam2.setExamCategory(ExamCategory.BIOQUIMICA);
        exam2.setExamType(ExamType.GLICEMIA);
        exam2.setStatus(ExamStatus.COLETADO);

        ExamResponseDTO responseDTO2 = new ExamResponseDTO(
                2L, EXISTING_PATIENT_ID, "Fabio Akita",
                EXISTING_LABORATORY_ID, "VitaLab Norte",
                ExamCategory.BIOQUIMICA,
                ExamType.GLICEMIA,
                SampleType.PLASMA,
                ExamStatus.COLETADO,
                LocalDateTime.of(2024, 1, 16, 9, 0),
                LocalDate.of(2024, 1, 18),
                null, null, new BigDecimal("35.00"));

        when(examRepository.findAll())
                .thenReturn(List.of(exam, exam2));
        when(examMapper.toResponseDTO(exam))
                .thenReturn(responseDTO);
        when(examMapper.toResponseDTO(exam2))
                .thenReturn(responseDTO2);

        List<ExamResponseDTO> result = examService.findAll();

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).examType()).isEqualTo(ExamType.HEMOGRAMA);
        assertThat(result.get(1).examType()).isEqualTo(ExamType.GLICEMIA);

        verify(examMapper, times(2)).toResponseDTO(any(Exam.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem exames")
    void shouldReturnEmptyListWhenNoExamsExist() {

        when(examRepository.findAll()).thenReturn(List.of());

        List<ExamResponseDTO> result = examService.findAll();

        assertThat(result).isNotNull().isEmpty();

        verify(examMapper, never()).toResponseDTO(any(Exam.class));
    }

    @Test
    @DisplayName("Deve atualizar exame e retornar ExamResponseDTO com sucesso")
    void shouldUpdateExamAndReturnResponseDTOSuccessfully() {

        when(examRepository.findById(EXISTING_EXAM_ID))
                .thenReturn(Optional.of(exam));
        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(EXISTING_LABORATORY_ID))
                .thenReturn(Optional.of(laboratory));
        when(examMapper.toResponseDTO(any(Exam.class)))
                .thenReturn(responseDTO);

        ExamResponseDTO result = examService.update(EXISTING_EXAM_ID, requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(EXISTING_EXAM_ID);

        verify(examRepository, times(1)).findById(EXISTING_EXAM_ID);
        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar exame CONCLUIDO")
    void shouldThrowBusinessExceptionWhenUpdatingConcludedExam() {

        exam.setStatus(ExamStatus.CONCLUIDO);

        when(examRepository.findById(EXISTING_EXAM_ID))
                .thenReturn(Optional.of(exam));
        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(EXISTING_LABORATORY_ID))
                .thenReturn(Optional.of(laboratory));

        assertThatThrownBy(() -> examService.update(EXISTING_EXAM_ID, requestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("concluído");

        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar exame CANCELADO")
    void shouldThrowBusinessExceptionWhenUpdatingCancelledExam() {

        exam.setStatus(ExamStatus.CANCELADO);

        when(examRepository.findById(EXISTING_EXAM_ID))
                .thenReturn(Optional.of(exam));
        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(EXISTING_LABORATORY_ID))
                .thenReturn(Optional.of(laboratory));

        assertThatThrownBy(() -> examService.update(EXISTING_EXAM_ID, requestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("cancelado");

        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao concluir exame sem informar resultado")
    void shouldThrowBusinessExceptionWhenConcludingExamWithoutResult() {

        when(examRepository.findById(EXISTING_EXAM_ID))
                .thenReturn(Optional.of(exam));
        when(patientRepository.findById(EXISTING_PATIENT_ID))
                .thenReturn(Optional.of(patient));
        when(laboratoryRepository.findById(EXISTING_LABORATORY_ID))
                .thenReturn(Optional.of(laboratory));

        ExamRequestDTO concludeWithoutResult = new ExamRequestDTO(
                EXISTING_PATIENT_ID,
                EXISTING_LABORATORY_ID,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                ExamStatus.CONCLUIDO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null,
                new BigDecimal("45.00"));

        assertThatThrownBy(() -> examService.update(EXISTING_EXAM_ID, concludeWithoutResult))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("resultado");

        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    @DisplayName("Deve deletar exame com sucesso")
    void shouldDeleteExamSuccessfully() {

        when(examRepository.findById(EXISTING_EXAM_ID))
                .thenReturn(Optional.of(exam));

        examService.delete(EXISTING_EXAM_ID);

        verify(examRepository, times(1)).delete(exam);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar exame inexistente")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentExam() {

        when(examRepository.findById(NON_EXISTING_EXAM_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> examService.delete(NON_EXISTING_EXAM_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(NON_EXISTING_EXAM_ID));

        verify(examRepository, never()).delete(any(Exam.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao deletar exame CONCLUIDO")
    void shouldThrowBusinessExceptionWhenDeletingConcludedExam() {

        exam.setStatus(ExamStatus.CONCLUIDO);
        exam.setResult("Hemoglobina: 14g/dL. Leucócitos: 8000/mm³. Normal.");

        when(examRepository.findById(EXISTING_EXAM_ID))
                .thenReturn(Optional.of(exam));

        assertThatThrownBy(() -> examService.delete(EXISTING_EXAM_ID))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("concluído");

        verify(examRepository, never()).delete(any(Exam.class));
    }

}
