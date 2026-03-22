package com.biogene.service;

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

import com.biogene.domain.Laboratory;
import com.biogene.dto.LaboratoryRequestDTO;
import com.biogene.dto.LaboratoryResponseDTO;
import com.biogene.exception.ResourceNotFoundException;
import com.biogene.mapper.LaboratoryMapper;
import com.biogene.repository.LaboratoryRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("LaboratoryService - Testes Unitários")
class LaboratoryServiceTest {

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @Mock
    private LaboratoryMapper laboratoryMapper;

    @InjectMocks
    private LaboratoryService laboratoryService;

    private Laboratory laboratory;
    private LaboratoryRequestDTO requestDTO;
    private LaboratoryResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new LaboratoryRequestDTO(
                "12345678000123",
                "BioGene Centro",
                "Avenida Cruzeiro, 7, 3º Andar, Zona Norte, São Paulo - SP",
                "1134567890"
        );

        laboratory = new Laboratory();
        laboratory.setId(1L);
        laboratory.setCnpj("12345678000123");
        laboratory.setName("BioGene Centro");
        laboratory.setAddress("Avenida Cruzeiro, 7, 3º Andar, Zona Norte, São Paulo - SP");
        laboratory.setPhone("1134567890");

        responseDTO = new LaboratoryResponseDTO(
                1L,
                "12345678000123",
                "BioGene Centro",
                "Avenida Cruzeiro, 7, 3º Andar, Zona Norte, São Paulo - SP",
                "1134567890"
        );
    }

    @Test
    @DisplayName("Deve criar laboratório e retornar LaboratoryResponseDTO com sucesso")
    void shouldCreateLaboratoryAndReturnResponseDTOSuccessfully() {

        when(laboratoryMapper.toEntity(any(LaboratoryRequestDTO.class)))
                .thenReturn(laboratory);
        when(laboratoryRepository.save(any(Laboratory.class)))
                .thenReturn(laboratory);
        when(laboratoryMapper.toResponseDTO(any(Laboratory.class)))
                .thenReturn(responseDTO);

        LaboratoryResponseDTO result = laboratoryService.create(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("BioGene Centro");

        verify(laboratoryMapper, times(1)).toEntity(any(LaboratoryRequestDTO.class));
        verify(laboratoryRepository, times(1)).save(any(Laboratory.class));
        verify(laboratoryMapper, times(1)).toResponseDTO(any(Laboratory.class));
    }

    @Test
    @DisplayName("Deve retornar LaboratoryResponseDTO quando laboratório existe")
    void shouldReturnResponseDTOWhenLaboratoryExists() {

        when(laboratoryRepository.findById(1L))
                .thenReturn(Optional.of(laboratory));
        when(laboratoryMapper.toResponseDTO(any(Laboratory.class)))
                .thenReturn(responseDTO);

        LaboratoryResponseDTO result = laboratoryService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("BioGene Centro");

        verify(laboratoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando laboratório não existe")
    void shouldThrowResourceNotFoundExceptionWhenLaboratoryNotExists() {

        when(laboratoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> laboratoryService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(laboratoryMapper, never()).toResponseDTO(any(Laboratory.class));
    }

    @Test
    @DisplayName("Deve retornar laboratórios quando nome corresponde à busca")
    void shouldReturnLaboratoriesWhenNameMatches() {

        when(laboratoryRepository.findByNameContainingIgnoreCase("BioGene"))
                .thenReturn(List.of(laboratory));
        when(laboratoryMapper.toResponseDTO(laboratory))
                .thenReturn(responseDTO);

        List<LaboratoryResponseDTO> result = laboratoryService.findByName("BioGene");

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("BioGene Centro");

        verify(laboratoryRepository, times(1)).findByNameContainingIgnoreCase("BioGene");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum laboratório corresponde ao nome")
    void shouldReturnEmptyListWhenNoLaboratoryMatchesName() {

        when(laboratoryRepository.findByNameContainingIgnoreCase(anyString()))
                .thenReturn(List.of());

        List<LaboratoryResponseDTO> result = laboratoryService.findByName("Inexistente");

        assertThat(result).isNotNull().isEmpty();

        verify(laboratoryMapper, never()).toResponseDTO(any(Laboratory.class));
    }

    @Test
    @DisplayName("Deve retornar lista de LaboratoryResponseDTO quando existem laboratórios")
    void shouldReturnListOfResponseDTOsWhenLaboratoriesExist() {

        Laboratory lab = new Laboratory();
        lab.setId(2L);
        lab.setCnpj("98765432000100");
        lab.setName("VitaLab Norte");
        lab.setAddress("Setor Bancário Sul, Quadra 2, Bloco E, Asa Sul, Brasília - DF");
        lab.setPhone("6133334444");

        LaboratoryResponseDTO responseDTO2 = new LaboratoryResponseDTO(
                2L, "98765432000100", "VitaLab Norte",
                "Setor Bancário Sul, Quadra 2, Bloco E, Asa Sul, Brasília - DF", "6133334444"
        );

        when(laboratoryRepository.findAll())
                .thenReturn(List.of(laboratory, lab));
        when(laboratoryMapper.toResponseDTO(laboratory))
                .thenReturn(responseDTO);
        when(laboratoryMapper.toResponseDTO(lab))
                .thenReturn(responseDTO2);

        List<LaboratoryResponseDTO> result = laboratoryService.findAll();

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("BioGene Centro");
        assertThat(result.get(1).name()).isEqualTo("VitaLab Norte");

        verify(laboratoryMapper, times(2)).toResponseDTO(any(Laboratory.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem laboratórios")
    void shouldReturnEmptyListWhenNoLaboratoriesExist() {

        when(laboratoryRepository.findAll()).thenReturn(List.of());

        List<LaboratoryResponseDTO> result = laboratoryService.findAll();

        assertThat(result).isNotNull().isEmpty();

        verify(laboratoryMapper, never()).toResponseDTO(any(Laboratory.class));
    }

    @Test
    @DisplayName("Deve atualizar laboratório e retornar LaboratoryResponseDTO com sucesso")
    void shouldUpdateLaboratoryAndReturnResponseDTOSuccessfully() {

        LaboratoryRequestDTO updateRequest = new LaboratoryRequestDTO(
                "12345678000123",
                "BioGene Centro Atualizado",
                "Praia de Ipanema, 462, Ipanema, Rio de Janeiro - RJ",
                "2199998888"
        );

        LaboratoryResponseDTO updatedResponse = new LaboratoryResponseDTO(
                1L, "12345678000123", "BioGene Centro Atualizado",
                "Praia de Ipanema, 462, Ipanema, Rio de Janeiro - RJ", "2199998888"
        );

        when(laboratoryRepository.findById(1L))
                .thenReturn(Optional.of(laboratory));
        when(laboratoryMapper.toResponseDTO(any(Laboratory.class)))
                .thenReturn(updatedResponse);

        LaboratoryResponseDTO result = laboratoryService.update(1L, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("BioGene Centro Atualizado");

        verify(laboratoryRepository, times(1)).findById(1L);
        verify(laboratoryRepository, never()).save(any(Laboratory.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao atualizar laboratório inexistente")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentLaboratory() {

        when(laboratoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> laboratoryService.update(99L, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(laboratoryRepository, never()).save(any(Laboratory.class));
    }

    @Test
    @DisplayName("Deve deletar laboratório com sucesso")
    void shouldDeleteLaboratorySuccessfully() {

        when(laboratoryRepository.findById(1L))
                .thenReturn(Optional.of(laboratory));

        laboratoryService.delete(1L);

        verify(laboratoryRepository, times(1)).delete(laboratory);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar laboratório inexistente")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentLaboratory() {

        when(laboratoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> laboratoryService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(laboratoryRepository, never()).delete(any(Laboratory.class));
    }

}
