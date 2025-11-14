package com.resume.builder.repository;

import com.resume.builder.model.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
    List<MatchResult> findByResumeIdOrderByCreatedAtDesc(Long resumeId);
}
