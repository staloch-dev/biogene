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

import com.biogene.dto.ExamCatalogCategoryDTO;
import com.biogene.dto.ExamRequestDTO;
import com.biogene.dto.ExamResponseDTO;
import com.biogene.enums.ExamStatus;
import com.biogene.service.ExamService;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ExamResponseDTO> create(
            @Valid @RequestBody ExamRequestDTO request) {

        ExamResponseDTO response = examService.create(request);

        return ResponseEntity
                .created(URI.create("/exams/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(examService.findById(id));
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<ExamCatalogCategoryDTO>> findCatalog() {
        return ResponseEntity.ok(examService.findCatalog());
    }

    @GetMapping
    public ResponseEntity<List<ExamResponseDTO>> findAll(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long laboratoryId,
            @RequestParam(required = false) ExamStatus status) {

        List<ExamResponseDTO> response;

        if (patientId != null) {
            response = examService.findByPatientId(patientId);
        } else if (laboratoryId != null) {
            response = examService.findByLaboratoryId(laboratoryId);
        } else if (status != null) {
            response = examService.findByStatus(status);
        } else {
            response = examService.findAll();
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ExamRequestDTO request) {

        return ResponseEntity.ok(examService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        examService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
