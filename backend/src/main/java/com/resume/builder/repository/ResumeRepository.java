package com.resume.builder.repository;

import com.resume.builder.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByEmail(String email);
    List<Resume> findAllByOrderByUpdatedAtDesc();
}
