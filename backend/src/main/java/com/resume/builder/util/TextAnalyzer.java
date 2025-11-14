    
    public List<String> extractSkills(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<String> commonSkills = Arrays.asList(
            "java", "python", "javascript", "react", "angular", "vue", "spring boot", "node.js",
            "sql", "mysql", "postgresql", "mongodb", "aws", "azure", "docker", "kubernetes",
            "git", "jenkins", "ci/cd", "agile", "scrum", "rest api", "microservices", "html",
            "css", "typescript", "c++", "c#", "php", "ruby", "golang", "rust", "swift",
            "machine learning", "ai", "data science", "tensorflow", "pytorch", "pandas",
            "leadership", "communication", "problem solving", "teamwork", "project management"
        );
        
        String lowerText = text.toLowerCase();
        return commonSkills.stream()
            .filter(lowerText::contains)
            .collect(Collectors.toList());
    }
    
    public double calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0.0;
        }
        
        Set<String> words1 = new HashSet<>(Arrays.asList(
            text1.toLowerCase().split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(
            text2.toLowerCase().split("\\s+")));
        
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);
        
        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);
        
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
}
