package com.biogene.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.biogene.domain.Laboratory;
import com.biogene.dto.LaboratoryRequestDTO;
import com.biogene.dto.LaboratoryResponseDTO;
import com.biogene.exception.ResourceNotFoundException;
import com.biogene.mapper.LaboratoryMapper;
import com.biogene.repository.LaboratoryRepository;

@Service
@RequiredArgsConstructor
public class LaboratoryService {

    private final LaboratoryRepository laboratoryRepository;
    private final LaboratoryMapper laboratoryMapper;

    @Transactional
    public LaboratoryResponseDTO create(LaboratoryRequestDTO request) {
        Laboratory laboratory = laboratoryMapper.toEntity(request);
        Laboratory saved = laboratoryRepository.save(laboratory);
        return laboratoryMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public LaboratoryResponseDTO findById(Long id) {
        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Laboratório com ID " + id + " não encontrado."));
        return laboratoryMapper.toResponseDTO(laboratory);
    }

    @Transactional(readOnly = true)
    public List<LaboratoryResponseDTO> findByName(String name) {
        return laboratoryRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(laboratoryMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LaboratoryResponseDTO> findAll() {
        return laboratoryRepository.findAll()
                .stream()
                .map(laboratoryMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public LaboratoryResponseDTO update(Long id, LaboratoryRequestDTO request) {
        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Laboratório com ID " + id + " não encontrado."));
        laboratory.setCnpj(request.cnpj());
        laboratory.setName(request.name());
        laboratory.setAddress(request.address());
        laboratory.setPhone(request.phone());
        return laboratoryMapper.toResponseDTO(laboratory);
    }

    @Transactional
    public void delete(Long id) {
        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Laboratório com ID " + id + " não encontrado."));
        laboratoryRepository.delete(laboratory);
    }

}
