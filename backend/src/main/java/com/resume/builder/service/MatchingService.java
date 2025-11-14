    
    private double calculateATSScore(String resume, String jd) {
        List<String> jdKeywords = textAnalyzer.extractKeywords(jd);
        String lowerResume = resume.toLowerCase();
        
        long matchedCount = jdKeywords.stream()
            .filter(keyword -> lowerResume.contains(keyword.toLowerCase()))
            .count();
        
        return jdKeywords.isEmpty() ? 0.0 : (matchedCount * 100.0) / jdKeywords.size();
    }
    
    private List<String> generateSuggestions(MatchScoreDTO matchScore, List<String> missingKeywords) {
        List<String> suggestions = new ArrayList<>();
        
        if (matchScore.getSkillsMatchScore() < 50) {
            suggestions.add("Add more technical skills mentioned in the job description to your Skills section");
        }
        
        if (matchScore.getExperienceMatchScore() < 40) {
            suggestions.add("Tailor your experience descriptions to match job requirements more closely");
        }
        
        if (!missingKeywords.isEmpty()) {
            suggestions.add("Include these keywords in your resume: " + 
                String.join(", ", missingKeywords.stream().limit(5).collect(Collectors.toList())));
        }
        
        if (matchScore.getAtsScore() < 60) {
            suggestions.add("Improve ATS compatibility by using standard section headings and avoiding complex formatting");
        }
        
        suggestions.add("Use action verbs at the beginning of bullet points (e.g., Developed, Implemented, Led)");
        suggestions.add("Quantify your achievements with metrics and numbers where possible");
        
        return suggestions;
    }
    
    private String determineStrength(MatchScoreDTO matchScore) {
        if (matchScore.getSkillsMatchScore() >= 70) {
            return "Strong technical skills alignment with job requirements";
        } else if (matchScore.getExperienceMatchScore() >= 70) {
            return "Relevant work experience matching job description";
        } else if (matchScore.getAtsScore() >= 70) {
            return "Good ATS compatibility with proper keyword usage";
        }
        return "Resume shows potential for the role";
    }
    
    private String determineWeakness(MatchScoreDTO matchScore) {
        if (matchScore.getSkillsMatchScore() < 50) {
            return "Limited technical skills matching job requirements";
        } else if (matchScore.getExperienceMatchScore() < 40) {
            return "Experience descriptions need better alignment with job requirements";
        } else if (matchScore.getAtsScore() < 60) {
            return "Low ATS score - missing important keywords";
        }
        return "Consider adding more specific examples and metrics";
    }
}
