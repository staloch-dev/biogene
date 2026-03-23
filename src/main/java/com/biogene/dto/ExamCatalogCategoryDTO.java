package com.biogene.dto;

import java.util.List;

import com.biogene.enums.ExamCategory;

public record ExamCatalogCategoryDTO(
        ExamCategory category,
        List<ExamCatalogTypeDTO> exams
) {}
