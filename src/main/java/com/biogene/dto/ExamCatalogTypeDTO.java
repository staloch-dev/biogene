package com.biogene.dto;

import java.util.List;

import com.biogene.enums.ExamType;
import com.biogene.enums.SampleType;

public record ExamCatalogTypeDTO(
        ExamType examType,
        SampleType defaultSample,
        List<SampleType> compatibleSamples
) {}
