package com.resume.builder.controller;

import com.resume.builder.model.Resume;
import com.resume.builder.model.JobDescription;
import com.resume.builder.repository.ResumeRepository;
import com.resume.builder.repository.JobDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final ResumeRepository resumeRepository;
    private final JobDescriptionRepository jobDescriptionRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activePage", "home");
        model.addAttribute("pageTitle", "Home");
        return "home";
    }

    @GetMapping("/resumes")
    public String listResumes(Model model) {
        model.addAttribute("activePage", "resumes");
        model.addAttribute("pageTitle", "My Resumes");
        model.addAttribute("resumes", resumeRepository.findAll());
        return "resumes-list";
    }

    @GetMapping("/upload")
    public String uploadResume(Model model) {
        model.addAttribute("activePage", "upload");
        model.addAttribute("pageTitle", "Upload Resume");
        return "resume-upload";
    }

    @GetMapping("/resume/new")
    public String createNewResume(Model model) {
        Resume resume = new Resume();
        resume.setName("");
        resume.setEmail("");
        resume.setPhone("");
        resume.setLinkedIn("");
        resume.setGithub("");
        resume.setSummary("");
        resume.setExperience("[]");
        resume.setEducation("[]");
        resume.setSkills("");
        resume.setProjects("[]");
        resume.setCertifications("");
        
        Resume saved = resumeRepository.save(resume);
        return "redirect:/resume/" + saved.getId();
    }

    @GetMapping("/resume/{id}")
    public String viewResume(@PathVariable Long id, Model model) {
        return resumeRepository.findById(id)
            .map(resume -> {
                model.addAttribute("activePage", "resumes");
                model.addAttribute("pageTitle", "Edit Resume");
                model.addAttribute("resume", resume);
                return "resume-builder";
            })
            .orElse("redirect:/resumes");
    }

    @GetMapping("/job-descriptions")
    public String listJobDescriptions(Model model) {
        model.addAttribute("activePage", "job-descriptions");
        model.addAttribute("pageTitle", "Job Descriptions");
        model.addAttribute("jobDescriptions", jobDescriptionRepository.findAll());
        return "job-descriptions";
    }

    @GetMapping("/matching")
    public String matching(Model model) {
        model.addAttribute("activePage", "matching");
        model.addAttribute("pageTitle", "Resume Matching");
        model.addAttribute("resumes", resumeRepository.findAll());
        model.addAttribute("jobDescriptions", jobDescriptionRepository.findAll());
        return "matching";
    }
}
