package com.biogene.mapper;

import org.springframework.stereotype.Component;

import com.biogene.domain.Exam;
import com.biogene.domain.Laboratory;
import com.biogene.domain.Patient;
import com.biogene.dto.ExamRequestDTO;
import com.biogene.dto.ExamResponseDTO;

@Component
public class ExamMapper {

    public Exam toEntity(ExamRequestDTO dto, Patient patient, Laboratory laboratory) {
        Exam exam = new Exam();
        exam.setPatient(patient);
        exam.setLaboratory(laboratory);
        exam.setExamType(dto.examType());
        exam.setExamDate(dto.examDate());
        exam.setResultDate(dto.resultDate());
        exam.setAnalysisMethod(dto.analysisMethod());
        exam.setResult(dto.result());
        exam.setExamValue(dto.examValue());
        return exam;
    }

    public ExamResponseDTO toResponseDTO(Exam exam) {
        return new ExamResponseDTO(
                exam.getId(),
                exam.getPatient().getId(),
                exam.getPatient().getFullName(),
                exam.getLaboratory().getId(),
                exam.getLaboratory().getName(),
                exam.getExamCategory(),
                exam.getExamType(),
                exam.getSampleType(),
                exam.getStatus(),
                exam.getExamDate(),
                exam.getResultDate(),
                exam.getAnalysisMethod(),
                exam.getResult(),
                exam.getExamValue()
        );
    }

}
