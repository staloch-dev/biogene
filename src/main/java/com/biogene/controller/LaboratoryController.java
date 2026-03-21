package com.biogene.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.biogene.dto.LaboratoryRequestDTO;
import com.biogene.dto.LaboratoryResponseDTO;
import com.biogene.service.LaboratoryService;

@RestController
@RequestMapping("/laboratories")
@RequiredArgsConstructor
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    @PostMapping
    public ResponseEntity<LaboratoryResponseDTO> create(
            @Valid @RequestBody LaboratoryRequestDTO request) {

        LaboratoryResponseDTO response = laboratoryService.create(request);

        return ResponseEntity
                .created(URI.create("/laboratories/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> findById(
            @PathVariable Long id) {

        LaboratoryResponseDTO response = laboratoryService.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LaboratoryResponseDTO>> findAll(
            @RequestParam(required = false) String name) {

        List<LaboratoryResponseDTO> response = (name != null && !name.isBlank())
                ? laboratoryService.findByName(name)
                : laboratoryService.findAll();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LaboratoryRequestDTO request) {

        LaboratoryResponseDTO response = laboratoryService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        laboratoryService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
