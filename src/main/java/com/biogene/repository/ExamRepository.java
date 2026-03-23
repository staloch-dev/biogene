package com.biogene.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biogene.domain.Exam;
import com.biogene.enums.ExamStatus;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByPatientId(Long patientId);

    List<Exam> findByLaboratoryId(Long laboratoryId);

    List<Exam> findByStatus(ExamStatus status);

    @Query("SELECT COUNT(e) FROM Exam e WHERE e.laboratory.id = :laboratoryId")
    long countByLaboratoryId(@Param("laboratoryId") Long laboratoryId);

    @Query("SELECT COUNT(e) FROM Exam e WHERE e.patient.id = :patientId")
    long countByPatientId(@Param("patientId") Long patientId);

}
