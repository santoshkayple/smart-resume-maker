package com.resume.builder.controller;

import com.resume.builder.dto.MatchScoreDTO;
import com.resume.builder.model.JobDescription;
import com.resume.builder.model.Resume;
import com.resume.builder.repository.JobDescriptionRepository;
import com.resume.builder.repository.ResumeRepository;
import com.resume.builder.service.MatchingService;
import com.resume.builder.service.OptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/optimization")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OptimizationController {
    
    private final MatchingService matchingService;
    private final OptimizationService optimizationService;
    private final ResumeRepository resumeRepository;
    private final JobDescriptionRepository jobDescriptionRepository;
    
    @PostMapping("/match")
    public ResponseEntity<?> matchResumeWithJob(
            @RequestParam Long resumeId,
            @RequestParam Long jobDescriptionId) {
        
        Resume resume = resumeRepository.findById(resumeId).orElse(null);
        JobDescription jobDescription = jobDescriptionRepository.findById(jobDescriptionId).orElse(null);
        
        if (resume == null || jobDescription == null) {
            return ResponseEntity.badRequest().body("Resume or Job Description not found");
        }
        
        MatchScoreDTO matchScore = matchingService.calculateMatch(resume, jobDescription);
        return ResponseEntity.ok(matchScore);
    }
    
    @PostMapping("/optimize")
    public ResponseEntity<?> optimizeResume(
            @RequestParam Long resumeId,
            @RequestParam Long jobDescriptionId) {
        
        Resume resume = resumeRepository.findById(resumeId).orElse(null);
        JobDescription jobDescription = jobDescriptionRepository.findById(jobDescriptionId).orElse(null);
        
        if (resume == null || jobDescription == null) {
            return ResponseEntity.badRequest().body("Resume or Job Description not found");
        }
        
        Resume optimized = optimizationService.optimizeResume(resume, jobDescription);
        Resume saved = resumeRepository.save(optimized);
        
        return ResponseEntity.ok(saved);
    }
}
