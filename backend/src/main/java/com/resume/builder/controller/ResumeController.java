    
    @GetMapping("/{id}")
    public ResponseEntity<?> getResumeById(@PathVariable Long id) {
        return resumeRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateResume(@PathVariable Long id, @RequestBody ResumeDTO resumeDTO) {
        return resumeRepository.findById(id)
            .map(resume -> {
                resume.setName(resumeDTO.getName());
                resume.setEmail(resumeDTO.getEmail());
                resume.setPhone(resumeDTO.getPhone());
                resume.setLinkedin(resumeDTO.getLinkedin());
                resume.setGithub(resumeDTO.getGithub());
                resume.setSummary(resumeDTO.getSummary());
                resume.setExperience(resumeDTO.getExperience());
                resume.setEducation(resumeDTO.getEducation());
                resume.setSkills(resumeDTO.getSkills());
                resume.setProjects(resumeDTO.getProjects());
                resume.setCertifications(resumeDTO.getCertifications());
                resume.setTemplate(resumeDTO.getTemplate());
                
                Resume updated = resumeRepository.save(resume);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable Long id) {
        if (resumeRepository.existsById(id)) {
            resumeRepository.deleteById(id);
            return ResponseEntity.ok().body("Resume deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Long id) {
        return resumeRepository.findById(id)
            .map(resume -> {
                try {
                    byte[] pdfBytes = pdfGeneratorService.generateResumePDF(resume);
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", 
                        resume.getName() + "_Resume.pdf");
                    
                    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
                } catch (IOException e) {
                    return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
