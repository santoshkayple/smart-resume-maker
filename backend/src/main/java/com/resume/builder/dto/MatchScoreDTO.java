package com.resume.builder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchScoreDTO {
    private Double overallScore;
    private Double skillsMatchScore;
    private Double experienceMatchScore;
    private Double atsScore;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private List<String> suggestions;
    private String strength;
    private String weakness;
}
