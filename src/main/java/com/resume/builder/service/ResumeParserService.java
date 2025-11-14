package com.resume.builder.service;

import com.resume.builder.model.Resume;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ResumeParserService {

    public Resume parseResume(MultipartFile file) throws IOException {
        String text = extractTextFromFile(file);
        return parseTextToResume(text, file.getOriginalFilename());
    }

    private String extractTextFromFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        
        if (filename == null) {
            throw new IllegalArgumentException("File must have a name");
        }

        if (filename.endsWith(".pdf")) {
            return extractTextFromPDF(file);
        } else if (filename.endsWith(".docx")) {
            return extractTextFromDocx(file);
        } else if (filename.endsWith(".txt")) {
            return new String(file.getBytes());
        } else {
            throw new IllegalArgumentException("Only PDF, DOCX, and TXT files are supported");
        }
    }

    private String extractTextFromPDF(MultipartFile file) throws IOException {
        // PDFBox 3.x uses Loader.loadPDF instead of PDDocument.load
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractTextFromDocx(MultipartFile file) throws IOException {
        StringBuilder text = new StringBuilder();
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                text.append(paragraph.getText()).append("\n");
            }
        }
        return text.toString();
    }

    private Resume parseTextToResume(String text, String filename) {
        Resume resume = new Resume();
        resume.setOriginalFilename(filename);

        // Extract email
        String email = extractEmail(text);
        resume.setEmail(email);

        // Extract phone
        String phone = extractPhone(text);
        resume.setPhone(phone);

        // Extract name (usually first line)
        String name = extractName(text);
        resume.setName(name);

        // Extract LinkedIn
        String linkedin = extractLinkedIn(text);
        resume.setLinkedIn(linkedin);

        // Extract GitHub
        String github = extractGitHub(text);
        resume.setGithub(github);

        // Extract sections
        resume.setSummary(extractSection(text, "summary", "objective", "profile"));
        resume.setExperience(extractSection(text, "experience", "work experience", "employment"));
        resume.setEducation(extractSection(text, "education", "academic", "qualifications"));
        resume.setSkills(extractSection(text, "skills", "technical skills", "competencies"));
        resume.setProjects(extractSection(text, "projects", "portfolio"));
        resume.setCertifications(extractSection(text, "certifications", "certificates", "licenses"));

        return resume;
    }

    private String extractEmail(String text) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : "";
    }

    private String extractPhone(String text) {
        Pattern pattern = Pattern.compile("(\\+?\\d{1,3}[-.\\s]?)?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : "";
    }

    private String extractName(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && line.length() < 50 && line.matches("[A-Za-z\\s]+")) {
                return line;
            }
        }
        return "";
    }

    private String extractLinkedIn(String text) {
        Pattern pattern = Pattern.compile("(https?://)?(www\\.)?linkedin\\.com/in/[a-zA-Z0-9-]+");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : "";
    }

    private String extractGitHub(String text) {
        Pattern pattern = Pattern.compile("(https?://)?(www\\.)?github\\.com/[a-zA-Z0-9-]+");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : "";
    }

    private String extractSection(String text, String... sectionNames) {
        String lowerText = text.toLowerCase();
        
        for (String sectionName : sectionNames) {
            int startIndex = lowerText.indexOf(sectionName);
            if (startIndex != -1) {
                // Find the end of this section (next section or end of text)
                int endIndex = findNextSectionStart(lowerText, startIndex + sectionName.length());
                
                if (endIndex == -1) {
                    endIndex = text.length();
                }
                
                String section = text.substring(startIndex, endIndex).trim();
                // Remove the section header
                section = section.replaceFirst("(?i)" + sectionName + "[:\\s]*", "").trim();
                
                return section;
            }
        }
        
        return "";
    }

    private int findNextSectionStart(String text, int fromIndex) {
        String[] commonSections = {
            "summary", "objective", "profile", "experience", "work experience",
            "education", "skills", "projects", "certifications", "awards", "languages"
        };
        
        int minIndex = -1;
        
        for (String section : commonSections) {
            int index = text.indexOf(section, fromIndex);
            if (index != -1 && (minIndex == -1 || index < minIndex)) {
                minIndex = index;
            }
        }
        
        return minIndex;
    }
}
