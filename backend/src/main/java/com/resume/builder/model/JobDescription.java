package com.resume.builder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_descriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_title")
    private String jobTitle;
    
    @Column(name = "company_name")
    private String companyName;
    
    @Column(length = 10000)
    private String description;
    
    @Column(length = 3000)
    private String requiredSkills;
    
    @Column(length = 2000)
    private String preferredSkills;
    
    @Column(length = 2000)
    private String responsibilities;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
