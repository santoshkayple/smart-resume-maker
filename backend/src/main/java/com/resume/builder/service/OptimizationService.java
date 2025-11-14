    
    private String enhanceBulletPoint(String bullet) {
        String trimmed = bullet.trim();
        if (trimmed.isEmpty()) return trimmed;
        
        // Action verbs to use
        List<String> actionVerbs = Arrays.asList(
            "Developed", "Implemented", "Led", "Managed", "Designed", "Optimized",
            "Improved", "Created", "Established", "Delivered", "Achieved", "Drove"
        );
        
        // Check if starts with action verb
        boolean startsWithActionVerb = actionVerbs.stream()
            .anyMatch(verb -> trimmed.toLowerCase().startsWith(verb.toLowerCase()));
        
        if (!startsWithActionVerb && !trimmed.startsWith("•") && !trimmed.startsWith("-")) {
            // Add action verb if missing
            return "• Developed " + trimmed;
        }
        
        if (!trimmed.startsWith("•") && !trimmed.startsWith("-")) {
            return "• " + trimmed;
        }
        
        return trimmed;
    }
    
    private String optimizeSkills(String originalSkills, List<String> missingKeywords) {
        Set<String> skills = new LinkedHashSet<>();
        
        // Add original skills
        if (originalSkills != null && !originalSkills.isEmpty()) {
            Arrays.stream(originalSkills.split("[,\n]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(skills::add);
        }
        
        // Add missing keywords (limit to avoid stuffing)
        missingKeywords.stream()
            .limit(5)
            .forEach(skills::add);
        
        return String.join(", ", skills);
    }
    
    private String buildResumeText(Resume resume) {
        StringBuilder text = new StringBuilder();
        if (resume.getSummary() != null) text.append(resume.getSummary()).append(" ");
        if (resume.getExperience() != null) text.append(resume.getExperience()).append(" ");
        if (resume.getSkills() != null) text.append(resume.getSkills()).append(" ");
        if (resume.getEducation() != null) text.append(resume.getEducation()).append(" ");
        return text.toString();
    }
}
