        
        // Add Summary
        if (resume.getSummary() != null && !resume.getSummary().isEmpty()) {
            addSection(document, "PROFESSIONAL SUMMARY", resume.getSummary());
        }
        
        // Add Experience
        if (resume.getExperience() != null && !resume.getExperience().isEmpty()) {
            addSection(document, "WORK EXPERIENCE", resume.getExperience());
        }
        
        // Add Education
        if (resume.getEducation() != null && !resume.getEducation().isEmpty()) {
            addSection(document, "EDUCATION", resume.getEducation());
        }
        
        // Add Skills
        if (resume.getSkills() != null && !resume.getSkills().isEmpty()) {
            addSection(document, "SKILLS", resume.getSkills());
        }
        
        // Add Projects
        if (resume.getProjects() != null && !resume.getProjects().isEmpty()) {
            addSection(document, "PROJECTS", resume.getProjects());
        }
        
        // Add Certifications
        if (resume.getCertifications() != null && !resume.getCertifications().isEmpty()) {
            addSection(document, "CERTIFICATIONS", resume.getCertifications());
        }
        
        document.close();
        return baos.toByteArray();
    }
    
    private void addSection(Document document, String title, String content) throws IOException {
        // Section title
        Paragraph sectionTitle = new Paragraph(title)
            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(14)
            .setMarginTop(10);
        document.add(sectionTitle);
        
        // Horizontal line
        document.add(new Paragraph("_____________________________________________________")
            .setMarginTop(-5)
            .setMarginBottom(5));
        
        // Section content
        Paragraph contentPara = new Paragraph(content)
            .setFontSize(11)
            .setMarginBottom(10);
        document.add(contentPara);
    }
}
