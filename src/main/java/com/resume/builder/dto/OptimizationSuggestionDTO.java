package com.resume.builder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationSuggestionDTO {
    private String optimizedSummary;
    private String optimizedExperience;
    private String optimizedSkills;
    private Map<String, String> bulletPointImprovements;
    private Integer addedKeywords;
}
