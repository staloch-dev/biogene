package com.biogene.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biogene.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByCpf(String cpf);

    List<Patient> findByFullNameContainingIgnoreCase(String name);

    boolean existsByCpf(String cpf);

}
