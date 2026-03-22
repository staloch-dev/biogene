package com.biogene.service;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.biogene.dto.LaboratoryRequestDTO;
import com.biogene.dto.LaboratoryResponseDTO;
import com.biogene.exception.ResourceNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("LaboratoryService - Testes de Integração")
public class LaboratoryIntegrationServiceTest {

    @Autowired
    private LaboratoryService laboratoryService;

    private LaboratoryRequestDTO validRequest() {
        return new LaboratoryRequestDTO(
                "12345678000123",
                "BioGene Centro",
                "Avenida Cruzeiro, 7, 3º Andar, Zona Norte, São Paulo - SP",
                "1134567890"
        );
    }

    @Test
    @DisplayName("Deve criar laboratório e retornar LaboratoryResponseDTO com sucesso")
    void shouldCreateLaboratoryAndReturnResponseDTOSuccessfully() {

        LaboratoryResponseDTO response = laboratoryService.create(validRequest());

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull().isPositive();
        assertThat(response.name()).isEqualTo("BioGene Centro");
        assertThat(response.cnpj()).isEqualTo("12345678000123");
    }

    @Test
    @DisplayName("Deve retornar LaboratoryResponseDTO quando laboratório existe")
    void shouldReturnResponseDTOWhenLaboratoryExists() {

        LaboratoryResponseDTO created = laboratoryService.create(validRequest());

        LaboratoryResponseDTO found = laboratoryService.findById(created.id());

        assertThat(found).isNotNull();
        assertThat(found.id()).isEqualTo(created.id());
        assertThat(found.name()).isEqualTo("BioGene Centro");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando laboratório não existe")
    void shouldThrowResourceNotFoundExceptionWhenLaboratoryNotExists() {

        assertThatThrownBy(() -> laboratoryService.findById(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve retornar laboratórios quando nome corresponde à busca")
    void shouldReturnLaboratoriesWhenNameMatches() {

        laboratoryService.create(validRequest());
        laboratoryService.create(new LaboratoryRequestDTO(
                "98765432000100",
                "VitaLab Norte",
                "Setor Bancário Sul, Quadra 2, Bloco E, Asa Sul, Brasília - DF",
                "6133334444"
        ));

        List<LaboratoryResponseDTO> result = laboratoryService.findByName("BioGene");

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("BioGene Centro");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum laboratório corresponde ao nome")
    void shouldReturnEmptyListWhenNoLaboratoryMatchesName() {

        laboratoryService.create(validRequest());

        List<LaboratoryResponseDTO> result = laboratoryService.findByName("Inexistente");

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Deve retornar lista com todos os laboratórios")
    void shouldReturnAllLaboratories() {

        laboratoryService.create(validRequest());
        laboratoryService.create(new LaboratoryRequestDTO(
                "98765432000100",
                "VitaLab Norte",
                "Setor Bancário Sul, Quadra 2, Bloco E, Asa Sul, Brasília - DF",
                "6133334444"
        ));

        List<LaboratoryResponseDTO> result = laboratoryService.findAll();

        assertThat(result).isNotNull().hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem laboratórios")
    void shouldReturnEmptyListWhenNoLaboratoriesExist() {

        List<LaboratoryResponseDTO> result = laboratoryService.findAll();

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar laboratório e retornar LaboratoryResponseDTO com sucesso")
    void shouldUpdateLaboratoryAndReturnResponseDTOSuccessfully() {

        LaboratoryResponseDTO created = laboratoryService.create(validRequest());

        LaboratoryRequestDTO updateRequest = new LaboratoryRequestDTO(
                "12345678000123",
                "BioGene Centro Atualizado",
                "Praia de Ipanema, 462, Ipanema, Rio de Janeiro - RJ",
                "2199998888"
        );

        LaboratoryResponseDTO updated = laboratoryService.update(created.id(), updateRequest);

        assertThat(updated).isNotNull();
        assertThat(updated.id()).isEqualTo(created.id());
        assertThat(updated.name()).isEqualTo("BioGene Centro Atualizado");
        assertThat(updated.address()).isEqualTo("Praia de Ipanema, 462, Ipanema, Rio de Janeiro - RJ");
        assertThat(updated.phone()).isEqualTo("2199998888");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao atualizar laboratório inexistente")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentLaboratory() {

        assertThatThrownBy(() -> laboratoryService.update(9999L, validRequest()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Deve deletar laboratório com sucesso")
    void shouldDeleteLaboratorySuccessfully() {

        LaboratoryResponseDTO created = laboratoryService.create(validRequest());

        laboratoryService.delete(created.id());

        assertThatThrownBy(() -> laboratoryService.findById(created.id()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar laboratório inexistente")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentLaboratory() {

        assertThatThrownBy(() -> laboratoryService.delete(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

}
