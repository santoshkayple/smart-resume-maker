    
    private List<String> extractActionVerbs(String text) {
        List<String> actionVerbs = Arrays.asList(
            "develop", "design", "implement", "manage", "lead", "coordinate", "analyze",
            "optimize", "improve", "create", "build", "maintain", "collaborate", "execute",
            "deliver", "establish", "achieve", "drive", "enhance", "streamline"
        );
        
        return actionVerbs.stream()
            .filter(verb -> text.toLowerCase().contains(verb))
            .collect(Collectors.toList());
    }
    
    private List<String> extractRequirements(String text) {
        List<String> requirements = new ArrayList<>();
        
        // Look for degree requirements
        if (text.toLowerCase().contains("bachelor") || text.toLowerCase().contains("bs")) {
            requirements.add("Bachelor's degree");
        }
        if (text.toLowerCase().contains("master") || text.toLowerCase().contains("ms")) {
            requirements.add("Master's degree");
        }
        
        // Look for experience years
        if (text.matches(".*\\d+\\+?\\s*years?.*(experience|exp).*")) {
            requirements.add("Years of experience");
        }
        
        return requirements;
    }
    
    public List<String> findMissingKeywords(String resume, String jobDescription) {
        List<String> jdKeywords = extractFromJobDescription(jobDescription);
        String lowerResume = resume.toLowerCase();
        
        return jdKeywords.stream()
            .filter(keyword -> !lowerResume.contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
}
