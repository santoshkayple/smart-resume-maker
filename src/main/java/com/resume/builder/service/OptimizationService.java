package com.resume.builder.service;

import com.resume.builder.dto.OptimizationSuggestionDTO;
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
public class OptimizationService {

    private final JDAnalyzerService jdAnalyzerService;
    private final AIContentGeneratorService aiContentGenerator;

    public Resume optimizeResume(Resume resume, JobDescription jd) {
        Resume optimized = new Resume();
        
        // Copy basic info
        optimized.setId(resume.getId());
        optimized.setName(resume.getName());
        optimized.setEmail(resume.getEmail());
        optimized.setPhone(resume.getPhone());
        optimized.setLinkedIn(resume.getLinkedIn());
        optimized.setGithub(resume.getGithub());
        optimized.setEducation(resume.getEducation());
        optimized.setTemplate(resume.getTemplate());
        
        // Use AI if enabled, otherwise use rule-based optimization
        if (aiContentGenerator.isAiEnabled()) {
            log.info("Using AI to optimize resume");
            
            // AI-generated summary
            String optimizedSummary = aiContentGenerator.generateProfessionalSummary(
                resume.getSummary(),
                jd.getDescription(),
                resume.getSkills()
            );
            optimized.setSummary(optimizedSummary);
            
            // AI-generated experience
            String optimizedExperience = aiContentGenerator.generateWorkExperience(
                resume.getExperience(),
                jd.getDescription(),
                jd.getJobTitle()
            );
            optimized.setExperience(optimizedExperience);
            
            // AI-generated skills
            String optimizedSkills = aiContentGenerator.generateSkillsSection(
                resume.getSkills(),
                jd.getDescription()
            );
            optimized.setSkills(optimizedSkills);
            
        } else {
            log.info("AI not enabled, using rule-based optimization");
            // Use existing rule-based methods
            Set<String> jdKeywords = jdAnalyzerService.extractAllKeywords(jd.getDescription());
            Set<String> jdSkills = extractSkillsSet(jd.getRequiredSkills());
            
            optimized.setSummary(optimizeSummary(resume.getSummary(), jd, jdKeywords));
            optimized.setSkills(optimizeSkills(resume.getSkills(), jdSkills));
            optimized.setExperience(optimizeExperience(resume.getExperience(), jdKeywords, jd));
        }
        
        // Keep other sections as is
        optimized.setProjects(resume.getProjects());
        optimized.setCertifications(resume.getCertifications());
        
        return optimized;
    }

    public OptimizationSuggestionDTO generateOptimizationSuggestions(Resume resume, JobDescription jd) {
        OptimizationSuggestionDTO suggestions = new OptimizationSuggestionDTO();
        
        if (aiContentGenerator.isAiEnabled()) {
            // Generate AI-powered suggestions
            suggestions.setOptimizedSummary(
                aiContentGenerator.generateProfessionalSummary(resume.getSummary(), jd.getDescription(), resume.getSkills())
            );
            suggestions.setOptimizedExperience(
                aiContentGenerator.generateWorkExperience(resume.getExperience(), jd.getDescription(), jd.getJobTitle())
            );
            suggestions.setOptimizedSkills(
                aiContentGenerator.generateSkillsSection(resume.getSkills(), jd.getDescription())
            );
            
            // Count how many new keywords AI added
            Set<String> originalSkills = extractSkillsSet(resume.getSkills());
            Set<String> optimizedSkills = extractSkillsSet(suggestions.getOptimizedSkills());
            optimizedSkills.removeAll(originalSkills);
            suggestions.setAddedKeywords(optimizedSkills.size());
            
        } else {
            // Use rule-based suggestions
            Set<String> jdKeywords = jdAnalyzerService.extractAllKeywords(jd.getDescription());
            Set<String> jdSkills = extractSkillsSet(jd.getRequiredSkills());
            
            suggestions.setOptimizedSummary(optimizeSummary(resume.getSummary(), jd, jdKeywords));
            suggestions.setOptimizedSkills(optimizeSkills(resume.getSkills(), jdSkills));
            suggestions.setOptimizedExperience(optimizeExperience(resume.getExperience(), jdKeywords, jd));
            
            Map<String, String> bulletImprovements = generateBulletPointImprovements(resume.getExperience());
            suggestions.setBulletPointImprovements(bulletImprovements);
            
            Set<String> resumeSkills = extractSkillsSet(resume.getSkills());
            Set<String> addedSkills = new HashSet<>(jdSkills);
            addedSkills.removeAll(resumeSkills);
            suggestions.setAddedKeywords(addedSkills.size());
        }
        
        return suggestions;
    }

    // Rule-based optimization methods (fallback)
    private String optimizeSummary(String originalSummary, JobDescription jd, Set<String> jdKeywords) {
        if (originalSummary == null || originalSummary.isEmpty()) {
            return generateDefaultSummary(jd);
        }
        
        StringBuilder optimized = new StringBuilder(originalSummary);
        
        String jobTitle = jd.getJobTitle();
        if (jobTitle != null && !originalSummary.toLowerCase().contains(jobTitle.toLowerCase())) {
            optimized.insert(0, "Motivated " + jobTitle + " professional. ");
        }
        
        Set<String> summaryWords = jdAnalyzerService.extractAllKeywords(originalSummary);
        List<String> missingKeywords = jdKeywords.stream()
            .filter(kw -> !summaryWords.contains(kw))
            .limit(3)
            .collect(Collectors.toList());
        
        if (!missingKeywords.isEmpty()) {
            optimized.append(" Experienced in ").append(String.join(", ", missingKeywords)).append(".");
        }
        
        return optimized.toString();
    }

    private String generateDefaultSummary(JobDescription jd) {
        return String.format("Experienced professional seeking %s position. " +
            "Strong background in relevant technologies and proven track record of delivering results.", 
            jd.getJobTitle() != null ? jd.getJobTitle() : "the");
    }

    private String optimizeSkills(String originalSkills, Set<String> jdSkills) {
        Set<String> currentSkills = extractSkillsSet(originalSkills);
        Set<String> optimizedSkills = new HashSet<>(currentSkills);
        optimizedSkills.addAll(jdSkills);
        
        return optimizedSkills.stream()
            .sorted()
            .collect(Collectors.joining(", "));
    }

    private String optimizeExperience(String originalExperience, Set<String> jdKeywords, JobDescription jd) {
        if (originalExperience == null || originalExperience.isEmpty()) {
            return originalExperience;
        }
        
        String improved = improveBulletPoints(originalExperience);
        
        Set<String> expKeywords = jdAnalyzerService.extractAllKeywords(originalExperience);
        List<String> missingKeywords = jdKeywords.stream()
            .filter(kw -> !expKeywords.contains(kw))
            .limit(5)
            .collect(Collectors.toList());
        
        if (!missingKeywords.isEmpty()) {
            improved += "\n• Demonstrated expertise in " + String.join(", ", missingKeywords);
        }
        
        return improved;
    }

    private String improveBulletPoints(String experience) {
        String[] lines = experience.split("\n");
        StringBuilder improved = new StringBuilder();
        
        String[] actionVerbs = {
            "Developed", "Implemented", "Designed", "Led", "Managed", "Created",
            "Optimized", "Improved", "Collaborated", "Architected", "Delivered"
        };
        
        Random random = new Random();
        
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;
            
            if (!startsWithActionVerb(trimmed)) {
                String verb = actionVerbs[random.nextInt(actionVerbs.length)];
                trimmed = "• " + verb + " " + trimmed.toLowerCase();
            }
            
            improved.append(trimmed).append("\n");
        }
        
        return improved.toString();
    }

    private boolean startsWithActionVerb(String text) {
        String[] actionVerbs = {
            "developed", "implemented", "designed", "led", "managed", "created",
            "built", "achieved", "increased", "reduced", "improved", "optimized"
        };
        
        String lower = text.toLowerCase();
        for (String verb : actionVerbs) {
            if (lower.startsWith(verb) || lower.startsWith("• " + verb)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, String> generateBulletPointImprovements(String experience) {
        Map<String, String> improvements = new HashMap<>();
        
        if (experience == null) return improvements;
        
        String[] lines = experience.split("\n");
        int count = 0;
        
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || count >= 5) continue;
            
            String improved = improveSingleBullet(trimmed);
            if (!improved.equals(trimmed)) {
                improvements.put(trimmed, improved);
                count++;
            }
        }
        
        return improvements;
    }

    private String improveSingleBullet(String bullet) {
        bullet = bullet.replaceFirst("^[•\\-*]\\s*", "");
        
        if (!startsWithActionVerb(bullet)) {
            bullet = "Developed " + bullet;
        }
        
        if (!containsNumbers(bullet)) {
            bullet += " resulting in measurable improvements";
        }
        
        return "• " + bullet;
    }

    private boolean containsNumbers(String text) {
        return text.matches(".*\\d+.*");
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
}
