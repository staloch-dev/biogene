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

import com.biogene.dto.PatientRequestDTO;
import com.biogene.dto.PatientResponseDTO;
import com.biogene.service.PatientService;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponseDTO> create(
            @Valid @RequestBody PatientRequestDTO request) {

        PatientResponseDTO response = patientService.create(request);

        return ResponseEntity
                .created(URI.create("/patients/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> findById(
            @PathVariable Long id) {

        PatientResponseDTO response = patientService.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PatientResponseDTO> findByCpf(
            @PathVariable String cpf) {

        PatientResponseDTO response = patientService.findByCpf(cpf);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> findAll(
            @RequestParam(required = false) String name) {

        List<PatientResponseDTO> response = (name != null && !name.isBlank())
                ? patientService.findByName(name)
                : patientService.findAll();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDTO request) {

        PatientResponseDTO response = patientService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        patientService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
