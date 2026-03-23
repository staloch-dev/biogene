package com.biogene.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.biogene.dto.ExamCatalogCategoryDTO;
import com.biogene.dto.ExamRequestDTO;
import com.biogene.dto.ExamResponseDTO;
import com.biogene.dto.LaboratoryRequestDTO;
import com.biogene.dto.LaboratoryResponseDTO;
import com.biogene.dto.PatientRequestDTO;
import com.biogene.dto.PatientResponseDTO;
import com.biogene.enums.ExamCategory;
import com.biogene.enums.ExamStatus;
import com.biogene.enums.ExamType;
import com.biogene.enums.SampleType;
import com.biogene.enums.Sex;
import com.biogene.exception.BusinessException;
import com.biogene.exception.ResourceNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("ExamService - Testes de Integração")
public class ExamIntegrationServiceTest {

    @Autowired
    private ExamService examService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private LaboratoryService laboratoryService;

    private Long patientId;
    private Long laboratoryId;

    @BeforeEach
    void setUp() {

        PatientResponseDTO patient = patientService.create(
                new PatientRequestDTO(
                        "12345678901",
                        "Johnny Test",
                        LocalDate.of(1994, 6, 21),
                        Sex.MASCULINO,
                        "Rua Porkbelly, 11, Casa dos Test, Porkbelly, São Paulo - SP",
                        "1134567890",
                        "johnny@porkbelly.com",
                        false, null, false, null));
        patientId = patient.id();

        LaboratoryResponseDTO laboratory = laboratoryService.create(
                new LaboratoryRequestDTO(
                        "12345678000123",
                        "BioGene Centro",
                        "Avenida Cruzeiro, 7, 3º Andar, Zona Norte, São Paulo - SP",
                        "1134567890"));
        laboratoryId = laboratory.id();
    }

    private ExamRequestDTO validRequest() {
        return new ExamRequestDTO(
                patientId,
                laboratoryId,
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

        ExamResponseDTO response = examService.create(validRequest());

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull().isPositive();
        assertThat(response.patientId()).isEqualTo(patientId);
        assertThat(response.laboratoryId()).isEqualTo(laboratoryId);
        assertThat(response.patientName()).isEqualTo("Johnny Test");
        assertThat(response.laboratoryName()).isEqualTo("BioGene Centro");
        assertThat(response.examCategory()).isEqualTo(ExamCategory.HEMATOLOGIA);
        assertThat(response.examType()).isEqualTo(ExamType.HEMOGRAMA);
        assertThat(response.sampleType()).isEqualTo(SampleType.SANGUE);
        assertThat(response.status()).isEqualTo(ExamStatus.AGENDADO);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando paciente não existe")
    void shouldThrowResourceNotFoundExceptionWhenPatientNotExists() {

        ExamRequestDTO invalidRequest = new ExamRequestDTO(
                9999L,
                laboratoryId,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                null,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null, null);

        assertThatThrownBy(() -> examService.create(invalidRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando laboratório não existe")
    void shouldThrowResourceNotFoundExceptionWhenLaboratoryNotExists() {

        ExamRequestDTO invalidRequest = new ExamRequestDTO(
                patientId,
                9999L,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                null,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null, null);

        assertThatThrownBy(() -> examService.create(invalidRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve usar amostra padrão do tipo de exame quando sampleType não é informado")
    void shouldUseDefaultSampleWhenSampleTypeIsNull() {

        ExamRequestDTO requestWithoutSample = new ExamRequestDTO(
                patientId,
                laboratoryId,
                ExamType.HEMOGRAMA,
                null,
                ExamStatus.AGENDADO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null, null);

        ExamResponseDTO response = examService.create(requestWithoutSample);

        assertThat(response).isNotNull();
        assertThat(response.sampleType()).isEqualTo(ExamType.HEMOGRAMA.getDefaultSample());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando amostra é incompatível com o tipo de exame")
    void shouldThrowBusinessExceptionWhenSampleTypeIsIncompatible() {

        ExamRequestDTO invalidRequest = new ExamRequestDTO(
                patientId,
                laboratoryId,
                ExamType.HEMOGRAMA,
                SampleType.URINA,
                null,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null, null);

        assertThatThrownBy(() -> examService.create(invalidRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("não é compatível");
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando resultDate não é posterior à examDate")
    void shouldThrowBusinessExceptionWhenResultDateIsNotAfterExamDate() {

        ExamRequestDTO invalidRequest = new ExamRequestDTO(
                patientId,
                laboratoryId,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                null,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 15),
                null, null, null);

        assertThatThrownBy(() -> examService.create(invalidRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("data de entrega");
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando resultado é informado na criação do exame")
    void shouldThrowBusinessExceptionWhenResultIsProvidedOnCreate() {

        ExamRequestDTO invalidRequest = new ExamRequestDTO(
                patientId,
                laboratoryId,
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
    }

    @Test
    @DisplayName("Deve retornar ExamResponseDTO quando exame existe")
    void shouldReturnResponseDTOWhenExamExists() {

        ExamResponseDTO created = examService.create(validRequest());

        ExamResponseDTO found = examService.findById(created.id());

        assertThat(found).isNotNull();
        assertThat(found.id()).isEqualTo(created.id());
        assertThat(found.examType()).isEqualTo(ExamType.HEMOGRAMA);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando exame não existe")
    void shouldThrowResourceNotFoundExceptionWhenExamNotExists() {

        assertThatThrownBy(() -> examService.findById(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve retornar exames quando filtrado por paciente")
    void shouldReturnExamsWhenFilteredByPatientId() {

        examService.create(validRequest());

        List<ExamResponseDTO> result = examService.findByPatientId(patientId);

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).patientName()).isEqualTo("Johnny Test");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao filtrar por paciente inexistente")
    void shouldThrowResourceNotFoundExceptionWhenFilteringByNonExistentPatient() {

        assertThatThrownBy(() -> examService.findByPatientId(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve retornar exames quando filtrado por laboratório")
    void shouldReturnExamsWhenFilteredByLaboratoryId() {

        examService.create(validRequest());

        List<ExamResponseDTO> result = examService.findByLaboratoryId(laboratoryId);

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).laboratoryName()).isEqualTo("BioGene Centro");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao filtrar por laboratório inexistente")
    void shouldThrowResourceNotFoundExceptionWhenFilteringByNonExistentLaboratory() {

        assertThatThrownBy(() -> examService.findByLaboratoryId(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve retornar exames quando filtrado por status")
    void shouldReturnExamsWhenFilteredByStatus() {

        examService.create(validRequest());

        List<ExamResponseDTO> result = examService.findByStatus(ExamStatus.AGENDADO);

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(ExamStatus.AGENDADO);
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
    @DisplayName("Deve retornar lista com todos os exames")
    void shouldReturnAllExams() {

        examService.create(validRequest());
        examService.create(new ExamRequestDTO(
                patientId, laboratoryId,
                ExamType.GLICEMIA,
                SampleType.PLASMA,
                ExamStatus.AGENDADO,
                LocalDateTime.of(2024, 1, 16, 9, 0),
                LocalDate.of(2024, 1, 18),
                null, null,
                new BigDecimal("35.00")));

        List<ExamResponseDTO> result = examService.findAll();

        assertThat(result).isNotNull().hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem exames")
    void shouldReturnEmptyListWhenNoExamsExist() {

        List<ExamResponseDTO> result = examService.findAll();

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar exame e retornar ExamResponseDTO com sucesso")
    void shouldUpdateExamAndReturnResponseDTOSuccessfully() {

        ExamResponseDTO created = examService.create(validRequest());

        ExamRequestDTO updateRequest = new ExamRequestDTO(
                patientId, laboratoryId,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                ExamStatus.COLETADO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                "Citometria de Fluxo",
                null,
                new BigDecimal("45.00"));

        ExamResponseDTO updated = examService.update(created.id(), updateRequest);

        assertThat(updated).isNotNull();
        assertThat(updated.id()).isEqualTo(created.id());
        assertThat(updated.status()).isEqualTo(ExamStatus.COLETADO);
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar exame CONCLUIDO")
    void shouldThrowBusinessExceptionWhenUpdatingConcludedExam() {

        ExamResponseDTO created = examService.create(validRequest());

        ExamRequestDTO concludeRequest = new ExamRequestDTO(
                patientId, laboratoryId,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                ExamStatus.CONCLUIDO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                "Citometria de Fluxo",
                "Hemoglobina: 14g/dL. Leucócitos: 8000/mm³. Normal.",
                new BigDecimal("45.00"));

        examService.update(created.id(), concludeRequest);

        assertThatThrownBy(() -> examService.update(created.id(), validRequest()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("concluído");
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar exame CANCELADO")
    void shouldThrowBusinessExceptionWhenUpdatingCancelledExam() {

        ExamResponseDTO created = examService.create(validRequest());

        ExamRequestDTO cancelRequest = new ExamRequestDTO(
                patientId, laboratoryId,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                ExamStatus.CANCELADO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null,
                new BigDecimal("45.00"));

        examService.update(created.id(), cancelRequest);

        assertThatThrownBy(() -> examService.update(created.id(), validRequest()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("cancelado");
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao concluir exame sem informar resultado")
    void shouldThrowBusinessExceptionWhenConcludingExamWithoutResult() {

        ExamResponseDTO created = examService.create(validRequest());

        ExamRequestDTO concludeWithoutResult = new ExamRequestDTO(
                patientId, laboratoryId,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                ExamStatus.CONCLUIDO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                null, null,
                new BigDecimal("45.00"));

        assertThatThrownBy(() -> examService.update(created.id(), concludeWithoutResult))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("resultado");
    }

    @Test
    @DisplayName("Deve deletar exame com sucesso")
    void shouldDeleteExamSuccessfully() {

        ExamResponseDTO created = examService.create(validRequest());

        examService.delete(created.id());

        assertThatThrownBy(() -> examService.findById(created.id()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar exame inexistente")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentExam() {

        assertThatThrownBy(() -> examService.delete(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao deletar exame CONCLUIDO")
    void shouldThrowBusinessExceptionWhenDeletingConcludedExam() {

        ExamResponseDTO created = examService.create(validRequest());

        ExamRequestDTO concludeRequest = new ExamRequestDTO(
                patientId, laboratoryId,
                ExamType.HEMOGRAMA,
                SampleType.SANGUE,
                ExamStatus.CONCLUIDO,
                LocalDateTime.of(2024, 1, 15, 8, 30),
                LocalDate.of(2024, 1, 17),
                "Citometria de Fluxo",
                "Hemoglobina: 14g/dL. Leucócitos: 8000/mm³. Normal.",
                new BigDecimal("45.00"));

        examService.update(created.id(), concludeRequest);

        assertThatThrownBy(() -> examService.delete(created.id()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("concluído");
    }

}
