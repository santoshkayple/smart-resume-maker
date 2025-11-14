package com.resume.builder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String phone;
    private String linkedIn;
    private String github;
    
    @Column(length = 2000)
    private String summary;
    
    @Column(length = 5000)
    private String experience;
    
    @Column(length = 3000)
    private String education;
    
    @Column(length = 2000)
    private String skills;
    
    @Column(length = 2000)
    private String projects;
    
    @Column(length = 1000)
    private String certifications;
    
    @Column(name = "original_filename")
    private String originalFilename;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    private String template;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
