package com.resume.builder.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AIContentGeneratorService {

    private final OpenAiService openAiService;
    private final String model;
    private final boolean aiEnabled;

    public AIContentGeneratorService(
            @Value("${openai.api.key:}") String apiKey,
            @Value("${openai.model:gpt-3.5-turbo}") String model) {
        this.model = model;
        this.aiEnabled = apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your-openai-api-key-here");
        
        if (aiEnabled) {
            this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
            log.info("AI Content Generator enabled with model: {}", model);
        } else {
            this.openAiService = null;
            log.warn("AI Content Generator disabled - no valid API key provided");
        }
    }

    /**
     * Generate professional summary based on job description and user's background
     */
    public String generateProfessionalSummary(String existingSummary, String jobDescription, String skills) {
        if (!aiEnabled) {
            return generateFallbackSummary(existingSummary, jobDescription, skills);
        }

        try {
            String prompt = String.format(
                "You are a professional resume writer. Generate a compelling professional summary (3-4 sentences) for a resume.\n\n" +
                "Job Description:\n%s\n\n" +
                "Candidate's Skills: %s\n\n" +
                "Existing Summary (if any): %s\n\n" +
                "Generate a professional summary that:\n" +
                "1. Highlights relevant experience and skills matching the job\n" +
                "2. Uses keywords from the job description\n" +
                "3. Is concise and impactful\n" +
                "4. Shows value proposition to employer\n\n" +
                "Return ONLY the summary text, no additional explanation.",
                jobDescription, skills, existingSummary != null ? existingSummary : "None"
            );

            return callOpenAI(prompt);
        } catch (Exception e) {
            log.error("Error generating summary with AI", e);
            return generateFallbackSummary(existingSummary, jobDescription, skills);
        }
    }

    /**
     * Generate or enhance work experience bullet points based on JD
     */
    public String generateWorkExperience(String existingExperience, String jobDescription, String jobTitle) {
        if (!aiEnabled) {
            return generateFallbackExperience(existingExperience, jobDescription);
        }

        try {
            String prompt = String.format(
                "You are a professional resume writer. Enhance or create work experience bullet points.\n\n" +
                "Target Job: %s\n\n" +
                "Job Description:\n%s\n\n" +
                "Existing Experience:\n%s\n\n" +
                "Instructions:\n" +
                "1. Create 4-5 strong bullet points that align with the job description\n" +
                "2. Start each bullet with powerful action verbs (Developed, Led, Implemented, etc.)\n" +
                "3. Include quantifiable achievements where possible (percentages, numbers)\n" +
                "4. Use keywords from the job description\n" +
                "5. Keep each bullet point to 1-2 lines\n\n" +
                "Format: Return bullet points starting with '• ', one per line.\n" +
                "Return ONLY the bullet points, no additional explanation.",
                jobTitle, jobDescription, existingExperience != null ? existingExperience : "No existing experience provided"
            );

            return callOpenAI(prompt);
        } catch (Exception e) {
            log.error("Error generating experience with AI", e);
            return generateFallbackExperience(existingExperience, jobDescription);
        }
    }

    /**
     * Generate skills section optimized for ATS
     */
    public String generateSkillsSection(String existingSkills, String jobDescription) {
        if (!aiEnabled) {
            return generateFallbackSkills(existingSkills, jobDescription);
        }

        try {
            String prompt = String.format(
                "You are a professional resume writer. Generate an optimized skills section.\n\n" +
                "Job Description:\n%s\n\n" +
                "Existing Skills: %s\n\n" +
                "Instructions:\n" +
                "1. Extract all technical skills mentioned in the job description\n" +
                "2. Combine with existing skills (no duplicates)\n" +
                "3. Organize by categories if possible (Programming, Tools, Frameworks, etc.)\n" +
                "4. Format as comma-separated list or categorized sections\n\n" +
                "Return ONLY the skills text, no additional explanation.",
                jobDescription, existingSkills != null ? existingSkills : "None"
            );

            return callOpenAI(prompt);
        } catch (Exception e) {
            log.error("Error generating skills with AI", e);
            return generateFallbackSkills(existingSkills, jobDescription);
        }
    }

    /**
     * Generate complete resume content from scratch based on JD
     */
    public String generateCompleteResume(String jobDescription, String name, String email, String phone) {
        if (!aiEnabled) {
            return "AI is not enabled. Please configure OpenAI API key.";
        }

        try {
            String prompt = String.format(
                "You are a professional resume writer. Create a complete resume tailored to this job.\n\n" +
                "Candidate Name: %s\n" +
                "Email: %s\n" +
                "Phone: %s\n\n" +
                "Job Description:\n%s\n\n" +
                "Create a complete resume with:\n" +
                "1. Professional Summary (3-4 sentences)\n" +
                "2. Work Experience (3-4 relevant positions with bullet points)\n" +
                "3. Skills section\n" +
                "4. Education\n\n" +
                "Format in clear sections. Make it ATS-friendly and keyword-optimized.\n" +
                "Return ONLY the resume content, no additional explanation.",
                name, email, phone, jobDescription
            );

            return callOpenAI(prompt);
        } catch (Exception e) {
            log.error("Error generating complete resume with AI", e);
            return "Error generating resume: " + e.getMessage();
        }
    }

    /**
     * Call OpenAI API
     */
    private String callOpenAI(String prompt) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("user", prompt));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .temperature(0.7)
                .maxTokens(1000)
                .build();

        String response = openAiService.createChatCompletion(request)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();

        return response.trim();
    }

    /**
     * Fallback methods when AI is not enabled
     */
    private String generateFallbackSummary(String existingSummary, String jobDescription, String skills) {
        if (existingSummary != null && !existingSummary.isEmpty()) {
            return existingSummary + " Experienced professional with skills in " + skills + ".";
        }
        return "Motivated professional seeking opportunities to leverage expertise in " + skills + 
               " and contribute to organizational success.";
    }

    private String generateFallbackExperience(String existingExperience, String jobDescription) {
        if (existingExperience != null && !existingExperience.isEmpty()) {
            return existingExperience;
        }
        return "• Developed and implemented solutions aligned with business requirements\n" +
               "• Collaborated with cross-functional teams to deliver projects\n" +
               "• Utilized modern technologies and best practices\n" +
               "• Contributed to process improvements and optimization";
    }

    private String generateFallbackSkills(String existingSkills, String jobDescription) {
        if (existingSkills != null && !existingSkills.isEmpty()) {
            return existingSkills;
        }
        return "Java, Spring Boot, JavaScript, SQL, Problem Solving, Team Collaboration";
    }

    public boolean isAiEnabled() {
        return aiEnabled;
    }
}
