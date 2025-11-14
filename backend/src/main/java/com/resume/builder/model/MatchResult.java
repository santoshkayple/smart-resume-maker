package com.resume.builder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
    
    @ManyToOne
    @JoinColumn(name = "job_description_id")
    private JobDescription jobDescription;
    
    @Column(name = "overall_score")
    private Double overallScore;
    
    @Column(name = "skills_match_score")
    private Double skillsMatchScore;
    
    @Column(name = "experience_match_score")
    private Double experienceMatchScore;
    
    @Column(length = 2000)
    private String missingKeywords;
    
    @Column(length = 3000)
    private String suggestions;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
