package com.resume.builder.controller;

import com.resume.builder.model.JobDescription;
import com.resume.builder.repository.JobDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-descriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobDescriptionController {
    
    private final JobDescriptionRepository jobDescriptionRepository;
    
    @PostMapping
    public ResponseEntity<JobDescription> createJobDescription(@RequestBody JobDescription jobDescription) {
        JobDescription saved = jobDescriptionRepository.save(jobDescription);
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping
    public ResponseEntity<List<JobDescription>> getAllJobDescriptions() {
        return ResponseEntity.ok(jobDescriptionRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<JobDescription> getJobDescriptionById(@PathVariable Long id) {
        return jobDescriptionRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJobDescription(@PathVariable Long id) {
        if (jobDescriptionRepository.existsById(id)) {
            jobDescriptionRepository.deleteById(id);
            return ResponseEntity.ok().body("Job description deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
