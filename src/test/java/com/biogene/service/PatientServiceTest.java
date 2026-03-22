package com.biogene.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.biogene.domain.Patient;
import com.biogene.dto.PatientRequestDTO;
import com.biogene.dto.PatientResponseDTO;
import com.biogene.enums.Sex;
import com.biogene.exception.BusinessException;
import com.biogene.exception.DuplicateResourceException;
import com.biogene.exception.ResourceNotFoundException;
import com.biogene.mapper.PatientMapper;
import com.biogene.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("PatientService - Testes Unitários")
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientRequestDTO requestDTO;
    private PatientResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new PatientRequestDTO(
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

        patient = new Patient();
        patient.setId(1L);
        patient.setCpf("12345678901");
        patient.setFullName("Johnny Test");
        patient.setBirthDate(LocalDate.of(1994, 6, 21));
        patient.setSex(Sex.MASCULINO);
        patient.setAddress("Rua Porkbelly, 11, Casa dos Test, Porkbelly, São Paulo - SP");
        patient.setPhone("1134567890");
        patient.setEmail("johnny@porkbelly.com");
        patient.setHasAllergy(false);
        patient.setTakesMedication(false);

        responseDTO = new PatientResponseDTO(
                1L,
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

        when(patientRepository.existsByCpf(anyString()))
                .thenReturn(false);
        when(patientMapper.toEntity(any(PatientRequestDTO.class)))
                .thenReturn(patient);
        when(patientRepository.save(any(Patient.class)))
                .thenReturn(patient);
        when(patientMapper.toResponseDTO(any(Patient.class)))
                .thenReturn(responseDTO);

        PatientResponseDTO result = patientService.create(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.fullName()).isEqualTo("Johnny Test");
        assertThat(result.cpf()).isEqualTo("12345678901");

        verify(patientRepository, times(1)).save(any(Patient.class));
        verify(patientMapper, times(1)).toEntity(any(PatientRequestDTO.class));
        verify(patientMapper, times(1)).toResponseDTO(any(Patient.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException quando CPF já está cadastrado")
    void shouldThrowDuplicateResourceExceptionWhenCpfAlreadyExists() {

        when(patientRepository.existsByCpf(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> patientService.create(requestDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("12345678901");

        verify(patientRepository, never()).save(any(Patient.class));
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

        when(patientRepository.existsByCpf(anyString()))
                .thenReturn(false);

        assertThatThrownBy(() -> patientService.create(invalidRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("alergia");

        verify(patientRepository, never()).save(any(Patient.class));
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

        when(patientRepository.existsByCpf(anyString()))
                .thenReturn(false);

        assertThatThrownBy(() -> patientService.create(invalidRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("medicamento");

        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Deve retornar PatientResponseDTO quando paciente existe")
    void shouldReturnResponseDTOWhenPatientExists() {

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(any(Patient.class)))
                .thenReturn(responseDTO);

        PatientResponseDTO result = patientService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.fullName()).isEqualTo("Johnny Test");

        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando paciente não existe")
    void shouldThrowResourceNotFoundExceptionWhenPatientNotExists() {

        when(patientRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(patientMapper, never()).toResponseDTO(any(Patient.class));
    }

    @Test
    @DisplayName("Deve retornar PatientResponseDTO quando CPF existe")
    void shouldReturnResponseDTOWhenCpfExists() {

        when(patientRepository.findByCpf("12345678901"))
                .thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(any(Patient.class)))
                .thenReturn(responseDTO);

        PatientResponseDTO result = patientService.findByCpf("12345678901");

        assertThat(result).isNotNull();
        assertThat(result.cpf()).isEqualTo("12345678901");

        verify(patientRepository, times(1)).findByCpf("12345678901");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando CPF não existe")
    void shouldThrowResourceNotFoundExceptionWhenCpfNotExists() {

        when(patientRepository.findByCpf(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findByCpf("00000000000"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("00000000000");

        verify(patientMapper, never()).toResponseDTO(any(Patient.class));
    }

    @Test
    @DisplayName("Deve retornar pacientes quando nome corresponde à busca")
    void shouldReturnPatientsWhenNameMatches() {

        when(patientRepository.findByFullNameContainingIgnoreCase("Johnny"))
                .thenReturn(List.of(patient));
        when(patientMapper.toResponseDTO(patient))
                .thenReturn(responseDTO);

        List<PatientResponseDTO> result = patientService.findByName("Johnny");

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).fullName()).isEqualTo("Johnny Test");

        verify(patientRepository, times(1)).findByFullNameContainingIgnoreCase("Johnny");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum paciente corresponde ao nome")
    void shouldReturnEmptyListWhenNoPatientMatchesName() {

        when(patientRepository.findByFullNameContainingIgnoreCase(anyString()))
                .thenReturn(List.of());

        List<PatientResponseDTO> result = patientService.findByName("Inexistente");

        assertThat(result).isNotNull().isEmpty();

        verify(patientMapper, never()).toResponseDTO(any(Patient.class));
    }

    @Test
    @DisplayName("Deve retornar lista de PatientResponseDTO quando existem pacientes")
    void shouldReturnListOfResponseDTOsWhenPatientsExist() {

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setFullName("Fabio Akita");

        PatientResponseDTO responseDTO2 = new PatientResponseDTO(
                2L, "98765432100", "Fabio Akita",
                LocalDate.of(1977, 3, 10), Sex.MASCULINO,
                "Rua Codeminer, 42, Vila Madalena, São Paulo - SP",
                "1198765432", "akita@codeminer42.com",
                false, null, false, null
        );

        when(patientRepository.findAll())
                .thenReturn(List.of(patient, patient2));
        when(patientMapper.toResponseDTO(patient))
                .thenReturn(responseDTO);
        when(patientMapper.toResponseDTO(patient2))
                .thenReturn(responseDTO2);

        List<PatientResponseDTO> result = patientService.findAll();

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).fullName()).isEqualTo("Johnny Test");
        assertThat(result.get(1).fullName()).isEqualTo("Fabio Akita");

        verify(patientMapper, times(2)).toResponseDTO(any(Patient.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem pacientes")
    void shouldReturnEmptyListWhenNoPatientsExist() {

        when(patientRepository.findAll()).thenReturn(List.of());

        List<PatientResponseDTO> result = patientService.findAll();

        assertThat(result).isNotNull().isEmpty();

        verify(patientMapper, never()).toResponseDTO(any(Patient.class));
    }

    @Test
    @DisplayName("Deve atualizar paciente e retornar PatientResponseDTO com sucesso")
    void shouldUpdatePatientAndReturnResponseDTOSuccessfully() {

        PatientRequestDTO updateRequest = new PatientRequestDTO(
                "12345678901",
                "Johnny Test Atualizado",
                LocalDate.of(1994, 6, 21),
                Sex.MASCULINO,
                "Praia de Ipanema, 462, Ipanema, Rio de Janeiro - RJ",
                "2199998888",
                "johnny.novo@porkbelly.com",
                false,
                null,
                false,
                null
        );

        PatientResponseDTO updatedResponse = new PatientResponseDTO(
                1L, "12345678901", "Johnny Test Atualizado",
                LocalDate.of(1994, 6, 21), Sex.MASCULINO,
                "Praia de Ipanema, 462, Ipanema, Rio de Janeiro - RJ",
                "2199998888", "johnny.novo@porkbelly.com",
                false, null, false, null
        );

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(any(Patient.class)))
                .thenReturn(updatedResponse);

        PatientResponseDTO result = patientService.update(1L, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo("Johnny Test Atualizado");

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException ao atualizar com CPF já existente")
    void shouldThrowDuplicateResourceExceptionWhenUpdatingWithExistingCpf() {

        PatientRequestDTO updateRequest = new PatientRequestDTO(
                "99999999999",
                "Johnny Test",
                LocalDate.of(1994, 6, 21),
                Sex.MASCULINO,
                "Rua Porkbelly, 11, Casa dos Test, Porkbelly, São Paulo - SP",
                "1134567890",
                "johnny@porkbelly.com",
                false, null, false, null
        );

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));
        when(patientRepository.existsByCpf("99999999999"))
                .thenReturn(true);

        assertThatThrownBy(() -> patientService.update(1L, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("99999999999");

        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao atualizar paciente inexistente")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentPatient() {

        when(patientRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.update(99L, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Deve deletar paciente com sucesso")
    void shouldDeletePatientSuccessfully() {

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        patientService.delete(1L);

        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar paciente inexistente")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentPatient() {

        when(patientRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(patientRepository, never()).delete(any(Patient.class));
    }

}
