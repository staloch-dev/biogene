package com.biogene.service;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.biogene.dto.PatientRequestDTO;
import com.biogene.dto.PatientResponseDTO;
import com.biogene.enums.Sex;
import com.biogene.exception.BusinessException;
import com.biogene.exception.DuplicateResourceException;
import com.biogene.exception.ResourceNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("PatientService - Testes de Integração")
public class PatientIntegrationServiceTest {

    @Autowired
    private PatientService patientService;

    private PatientRequestDTO validRequest() {
        return new PatientRequestDTO(
                "12345678901",
                "Johnny Test",
                LocalDate.of(1994, 6, 21),
                Sex.MASCULINO,
                "Rua Porkbelly, 11, Casa dos Test, Porkbelly, São Paulo - SP",
                "1134567890",
                "johnny@porkbelly.com",
                false,
                null,
                false,
                null
        );
    }

    @Test
    @DisplayName("Deve criar paciente e retornar PatientResponseDTO com sucesso")
    void shouldCreatePatientAndReturnResponseDTOSuccessfully() {

        PatientResponseDTO response = patientService.create(validRequest());

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull().isPositive();
        assertThat(response.fullName()).isEqualTo("Johnny Test");
        assertThat(response.cpf()).isEqualTo("12345678901");
        assertThat(response.sex()).isEqualTo(Sex.MASCULINO);
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException quando CPF já está cadastrado")
    void shouldThrowDuplicateResourceExceptionWhenCpfAlreadyExists() {

        patientService.create(validRequest());

        assertThatThrownBy(() -> patientService.create(validRequest()))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("12345678901");
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando hasAllergy=true sem descrição")
    void shouldThrowBusinessExceptionWhenHasAllergyWithoutDescription() {

        PatientRequestDTO invalidRequest = new PatientRequestDTO(
                "12345678901",
                "Johnny Test",
                LocalDate.of(1994, 6, 21),
                Sex.MASCULINO,
                "Rua Porkbelly, 11, Casa dos Test, Porkbelly, São Paulo - SP",
                "1134567890",
                "johnny@porkbelly.com",
                true,
                null,
                false,
                null
        );

        assertThatThrownBy(() -> patientService.create(invalidRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("alergia");
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando takesMedication=true sem descrição")
    void shouldThrowBusinessExceptionWhenTakesMedicationWithoutDescription() {

        PatientRequestDTO invalidRequest = new PatientRequestDTO(
                "12345678901",
                "Johnny Test",
                LocalDate.of(1994, 6, 21),
                Sex.MASCULINO,
                "Rua Porkbelly, 11, Casa dos Test, Porkbelly, São Paulo - SP",
                "1134567890",
                "johnny@porkbelly.com",
                false,
                null,
                true,
                null
        );

        assertThatThrownBy(() -> patientService.create(invalidRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("medicamento");
    }

    @Test
    @DisplayName("Deve retornar PatientResponseDTO quando paciente existe")
    void shouldReturnResponseDTOWhenPatientExists() {

        PatientResponseDTO created = patientService.create(validRequest());

        PatientResponseDTO found = patientService.findById(created.id());

        assertThat(found).isNotNull();
        assertThat(found.id()).isEqualTo(created.id());
        assertThat(found.fullName()).isEqualTo("Johnny Test");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando paciente não existe")
    void shouldThrowResourceNotFoundExceptionWhenPatientNotExists() {

        assertThatThrownBy(() -> patientService.findById(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve retornar PatientResponseDTO quando CPF existe")
    void shouldReturnResponseDTOWhenCpfExists() {

        patientService.create(validRequest());

        PatientResponseDTO found = patientService.findByCpf("12345678901");

        assertThat(found).isNotNull();
        assertThat(found.cpf()).isEqualTo("12345678901");
        assertThat(found.fullName()).isEqualTo("Johnny Test");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando CPF não existe")
    void shouldThrowResourceNotFoundExceptionWhenCpfNotExists() {

        assertThatThrownBy(() -> patientService.findByCpf("00000000000"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("00000000000");
    }

    @Test
    @DisplayName("Deve retornar pacientes quando nome corresponde à busca")
    void shouldReturnPatientsWhenNameMatches() {

        patientService.create(validRequest());
        patientService.create(new PatientRequestDTO(
                "98765432100",
                "Fabio Akita",
                LocalDate.of(1977, 3, 10),
                Sex.MASCULINO,
                "Rua Codeminer, 42, Vila Madalena, São Paulo - SP",
                "1198765432",
                "akita@codeminer42.com",
                false, null, false, null
        ));

        List<PatientResponseDTO> result = patientService.findByName("Johnny");

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).fullName()).isEqualTo("Johnny Test");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum paciente corresponde ao nome")
    void shouldReturnEmptyListWhenNoPatientMatchesName() {

        patientService.create(validRequest());

        List<PatientResponseDTO> result = patientService.findByName("Inexistente");

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Deve retornar lista com todos os pacientes")
    void shouldReturnAllPatients() {

        patientService.create(validRequest());
        patientService.create(new PatientRequestDTO(
                "98765432100",
                "Fabio Akita",
                LocalDate.of(1977, 3, 10),
                Sex.MASCULINO,
                "Rua Codeminer, 42, Vila Madalena, São Paulo - SP",
                "1198765432",
                "akita@codeminer42.com",
                false, null, false, null
        ));

        List<PatientResponseDTO> result = patientService.findAll();

        assertThat(result).isNotNull().hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem pacientes")
    void shouldReturnEmptyListWhenNoPatientsExist() {

        List<PatientResponseDTO> result = patientService.findAll();

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar paciente e retornar PatientResponseDTO com sucesso")
    void shouldUpdatePatientAndReturnResponseDTOSuccessfully() {

        PatientResponseDTO created = patientService.create(validRequest());

        PatientRequestDTO updateRequest = new PatientRequestDTO(
                "12345678901",
                "Johnny Test Atualizado",
                LocalDate.of(1994, 6, 21),
                Sex.MASCULINO,
                "Praia de Ipanema, 462, Ipanema, Rio de Janeiro - RJ",
                "2199998888",
                "johnny.novo@porkbelly.com",
                false, null, false, null
        );

        PatientResponseDTO updated = patientService.update(created.id(), updateRequest);

        assertThat(updated).isNotNull();
        assertThat(updated.id()).isEqualTo(created.id());
        assertThat(updated.fullName()).isEqualTo("Johnny Test Atualizado");
        assertThat(updated.address()).isEqualTo("Praia de Ipanema, 462, Ipanema, Rio de Janeiro - RJ");
        assertThat(updated.phone()).isEqualTo("2199998888");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao atualizar paciente inexistente")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentPatient() {

        assertThatThrownBy(() -> patientService.update(9999L, validRequest()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve deletar paciente com sucesso")
    void shouldDeletePatientSuccessfully() {

        PatientResponseDTO created = patientService.create(validRequest());

        patientService.delete(created.id());

        assertThatThrownBy(() -> patientService.findById(created.id()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar paciente inexistente")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentPatient() {

        assertThatThrownBy(() -> patientService.delete(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

}
