package com.resume.builder.service;

import com.resume.builder.dto.MatchScoreDTO;
import com.resume.builder.model.JobDescription;
import com.resume.builder.model.Resume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingService {

    private final JDAnalyzerService jdAnalyzerService;

    public MatchScoreDTO calculateMatch(Resume resume, JobDescription jd) {
        MatchScoreDTO matchScore = new MatchScoreDTO();
        
        // Extract keywords from JD
        Set<String> jdKeywords = jdAnalyzerService.extractAllKeywords(jd.getDescription());
        Set<String> jdSkills = extractSkillsSet(jd.getRequiredSkills());
        
        // Extract keywords from Resume
        String resumeText = buildResumeText(resume);
        Set<String> resumeKeywords = jdAnalyzerService.extractAllKeywords(resumeText);
        Set<String> resumeSkills = extractSkillsSet(resume.getSkills());
        
        // Calculate skills match
        double skillsMatch = calculateSetSimilarity(jdSkills, resumeSkills);
        matchScore.setSkillsMatchScore(skillsMatch * 100);
        
        // Calculate overall keyword match
        double keywordMatch = calculateSetSimilarity(jdKeywords, resumeKeywords);
        
        // Calculate experience match (based on keyword overlap in experience section)
        double experienceMatch = calculateExperienceMatch(resume.getExperience(), jd.getResponsibilities());
        matchScore.setExperienceMatchScore(experienceMatch * 100);
        
        // Calculate overall score (weighted average)
        double overallScore = (skillsMatch * 0.4) + (keywordMatch * 0.3) + (experienceMatch * 0.3);
        matchScore.setOverallScore(overallScore * 100);
        
        // Calculate ATS score (how well formatted and keyword-rich)
        double atsScore = calculateATSScore(resume, jdKeywords);
        matchScore.setAtsScore(atsScore);
        
        // Find matched and missing keywords
        Set<String> matched = new HashSet<>(jdSkills);
        matched.retainAll(resumeSkills);
        matchScore.setMatchedKeywords(new ArrayList<>(matched));
        
        Set<String> missing = new HashSet<>(jdSkills);
        missing.removeAll(resumeSkills);
        matchScore.setMissingKeywords(new ArrayList<>(missing));
        
        // Generate suggestions
        List<String> suggestions = generateSuggestions(matchScore, missing);
        matchScore.setSuggestions(suggestions);
        
        // Determine strength and weakness
        matchScore.setStrength(determineStrength(matchScore));
        matchScore.setWeakness(determineWeakness(matchScore));
        
        return matchScore;
    }

    private String buildResumeText(Resume resume) {
        return String.join(" ", 
            Optional.ofNullable(resume.getSummary()).orElse(""),
            Optional.ofNullable(resume.getExperience()).orElse(""),
            Optional.ofNullable(resume.getEducation()).orElse(""),
            Optional.ofNullable(resume.getSkills()).orElse(""),
            Optional.ofNullable(resume.getProjects()).orElse(""),
            Optional.ofNullable(resume.getCertifications()).orElse("")
        );
    }

    private Set<String> extractSkillsSet(String skillsText) {
        if (skillsText == null || skillsText.isEmpty()) {
            return new HashSet<>();
        }
        
        return Arrays.stream(skillsText.split("[,;\\n]"))
            .map(String::trim)
            .map(String::toLowerCase)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());
    }

    private double calculateSetSimilarity(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() || set2.isEmpty()) {
            return 0.0;
        }
        
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return (double) intersection.size() / union.size();
    }

    private double calculateExperienceMatch(String resumeExperience, String jdResponsibilities) {
        if (resumeExperience == null || jdResponsibilities == null) {
            return 0.0;
        }
        
        Set<String> expKeywords = jdAnalyzerService.extractAllKeywords(resumeExperience);
        Set<String> respKeywords = jdAnalyzerService.extractAllKeywords(jdResponsibilities);
        
        return calculateSetSimilarity(respKeywords, expKeywords);
    }

    private double calculateATSScore(Resume resume, Set<String> jdKeywords) {
        double score = 50.0; // Base score
        
        // Check for essential fields
        if (resume.getEmail() != null && !resume.getEmail().isEmpty()) score += 5;
        if (resume.getPhone() != null && !resume.getPhone().isEmpty()) score += 5;
        if (resume.getName() != null && !resume.getName().isEmpty()) score += 5;
        
        // Check for sections
        if (resume.getExperience() != null && !resume.getExperience().isEmpty()) score += 10;
        if (resume.getEducation() != null && !resume.getEducation().isEmpty()) score += 10;
        if (resume.getSkills() != null && !resume.getSkills().isEmpty()) score += 10;
        
        // Keyword density
        String resumeText = buildResumeText(resume).toLowerCase();
        long matchedKeywords = jdKeywords.stream()
            .filter(resumeText::contains)
            .count();
        
        double keywordDensity = jdKeywords.isEmpty() ? 0 : (double) matchedKeywords / jdKeywords.size();
        score += keywordDensity * 5;
        
        return Math.min(score, 100.0);
    }

    private List<String> generateSuggestions(MatchScoreDTO matchScore, Set<String> missingKeywords) {
        List<String> suggestions = new ArrayList<>();
        
        if (matchScore.getOverallScore() < 50) {
            suggestions.add("Your resume needs significant improvement to match this job description.");
        }
        
        if (matchScore.getSkillsMatchScore() < 60) {
            suggestions.add("Add more relevant technical skills mentioned in the job description.");
            if (!missingKeywords.isEmpty()) {
                suggestions.add("Missing key skills: " + 
                    missingKeywords.stream().limit(5).collect(Collectors.joining(", ")));
            }
        }
        
        if (matchScore.getExperienceMatchScore() < 60) {
            suggestions.add("Emphasize experiences that align with the job responsibilities.");
            suggestions.add("Use action verbs and quantify your achievements.");
        }
        
        if (matchScore.getAtsScore() < 70) {
            suggestions.add("Improve ATS compatibility by including more relevant keywords naturally.");
            suggestions.add("Ensure all standard sections (Experience, Education, Skills) are complete.");
        }
        
        if (matchScore.getOverallScore() >= 70) {
            suggestions.add("Your resume is a good match! Consider highlighting specific achievements.");
        }
        
        return suggestions;
    }

    private String determineStrength(MatchScoreDTO matchScore) {
        if (matchScore.getSkillsMatchScore() >= 70) {
            return "Strong technical skills alignment with job requirements";
        } else if (matchScore.getExperienceMatchScore() >= 70) {
            return "Relevant experience matches job responsibilities well";
        } else if (matchScore.getAtsScore() >= 80) {
            return "Well-formatted resume with good ATS compatibility";
        }
        return "Resume shows potential for this role";
    }

    private String determineWeakness(MatchScoreDTO matchScore) {
        if (matchScore.getSkillsMatchScore() < 50) {
            return "Limited technical skills match with job requirements";
        } else if (matchScore.getExperienceMatchScore() < 50) {
            return "Experience section needs better alignment with job responsibilities";
        } else if (matchScore.getAtsScore() < 60) {
            return "Resume may not pass ATS screening effectively";
        }
        return "Minor improvements needed for better match";
    }
}
