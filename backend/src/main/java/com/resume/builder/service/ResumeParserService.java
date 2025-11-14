    
    private Resume extractResumeData(String text, String filename) {
        Resume resume = new Resume();
        resume.setOriginalFilename(filename);
        
        // Extract name (usually first line)
        String[] lines = text.split("\n");
        if (lines.length > 0) {
            resume.setName(lines[0].trim());
        }
        
        // Extract email
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        Matcher emailMatcher = emailPattern.matcher(text);
        if (emailMatcher.find()) {
            resume.setEmail(emailMatcher.group());
        }
        
        // Extract phone
        Pattern phonePattern = Pattern.compile("(\\+?\\d{1,3}[-.\\s]?)?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}");
        Matcher phoneMatcher = phonePattern.matcher(text);
        if (phoneMatcher.find()) {
            resume.setPhone(phoneMatcher.group());
        }
        
        // Extract LinkedIn
        Pattern linkedinPattern = Pattern.compile("linkedin\\.com/in/[a-zA-Z0-9-]+", Pattern.CASE_INSENSITIVE);
        Matcher linkedinMatcher = linkedinPattern.matcher(text);
        if (linkedinMatcher.find()) {
            resume.setLinkedin("https://" + linkedinMatcher.group());
        }
        
        // Extract GitHub
        Pattern githubPattern = Pattern.compile("github\\.com/[a-zA-Z0-9-]+", Pattern.CASE_INSENSITIVE);
        Matcher githubMatcher = githubPattern.matcher(text);
        if (githubMatcher.find()) {
            resume.setGithub("https://" + githubMatcher.group());
        }
        
        // Extract sections
        resume.setSummary(extractSection(text, "summary|objective|profile"));
        resume.setExperience(extractSection(text, "experience|work history|employment"));
        resume.setEducation(extractSection(text, "education|academic"));
        resume.setSkills(extractSection(text, "skills|technical skills|competencies"));
        resume.setProjects(extractSection(text, "projects|portfolio"));
        resume.setCertifications(extractSection(text, "certifications|certificates"));
        
        return resume;
    }
    
    private String extractSection(String text, String sectionPattern) {
        Pattern pattern = Pattern.compile(
            "(?i)(" + sectionPattern + ")\\s*[:\\n]([\\s\\S]*?)(?=\\n\\s*[A-Z][a-z]+\\s*[:\\n]|$)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return "";
    }
}
