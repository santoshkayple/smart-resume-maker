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

        // Extract sections and convert to JSON format for frontend
        String summary = extractSection(text, "summary", "objective", "profile", "professional summary");
        if (summary.isEmpty()) {
            summary = "Experienced developer with expertise in backend development, Spring Boot, and cloud technologies.";
        }
        resume.setSummary(summary);
        
        String experienceText = extractSection(text, "experience", "work experience", "employment", "work history");
        resume.setExperience(convertToJsonArray(experienceText, "experience"));
        
        String educationText = extractSection(text, "education", "academic", "qualifications", "academic background");
        resume.setEducation(convertToJsonArray(educationText, "education"));
        
        String projectsText = extractSection(text, "projects", "portfolio", "personal projects");
        resume.setProjects(convertToJsonArray(projectsText, "projects"));
        
        resume.setSkills(extractSection(text, "technical skills", "skills", "competencies", "technologies", "expertise"));
        resume.setCertifications(extractSection(text, "certifications", "certificates", "licenses", "credentials"));

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
            // Look for the section name at the start of a line (with possible whitespace)
            Pattern pattern = Pattern.compile("(?m)^\\s*" + Pattern.quote(sectionName) + "\\s*$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            
            if (matcher.find()) {
                int startIndex = matcher.end();
                
                // Find the end of this section (next section or end of text)
                int endIndex = findNextSectionStart(lowerText, startIndex);
                
                if (endIndex == -1) {
                    endIndex = text.length();
                }
                
                String section = text.substring(startIndex, endIndex).trim();
                
                return section;
            }
        }
        
        return "";
    }

    private int findNextSectionStart(String text, int fromIndex) {
        String[] commonSections = {
            "summary", "objective", "profile", "professional summary",
            "experience", "work experience", "employment", "work history",
            "education", "academic", "qualifications", "academic background",
            "technical skills", "skills", "competencies", "technologies",
            "projects", "portfolio", "personal projects",
            "certifications", "certificates", "licenses", "credentials",
            "awards", "achievements", "honors",
            "languages", "language proficiency"
        };
        
        int minIndex = -1;
        
        for (String section : commonSections) {
            // Look for section headers at the start of a line
            Pattern pattern = Pattern.compile("(?m)^\\s*" + Pattern.quote(section) + "\\s*$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            
            if (matcher.find(fromIndex)) {
                int index = matcher.start();
                if (index > fromIndex && (minIndex == -1 || index < minIndex)) {
                    minIndex = index;
                }
            }
        }
        
        return minIndex;
    }
    
    private String convertToJsonArray(String text, String type) {
        if (text == null || text.trim().isEmpty()) {
            return "[]";
        }
        
        StringBuilder json = new StringBuilder("[");
        
        if ("education".contains(type)) {
            // Split by university names or degree patterns
            String[] entries = text.split("(?=\\n[A-Z][a-zA-Z\\s]+University|(?m)^[A-Z][a-zA-Z\\s]+in\\s+)");
            for (int i = 0; i < entries.length && i < 5; i++) {
                String entry = entries[i].trim();
                if (entry.length() > 10) {
                    if (i > 0) json.append(",");
                    json.append("{")
                        .append("\"degree\":\"").append(escapeJson(extractLine(entry, 0))).append("\",")
                        .append("\"field\":\"\",")
                        .append("\"institution\":\"").append(escapeJson(extractInstitution(entry))).append("\",")
                        .append("\"year\":\"").append(escapeJson(extractYear(entry))).append("\"")
                        .append("}");
                }
            }
        } else if ("experience".contains(type)) {
            // Split by job titles or company patterns
            String[] entries = text.split("(?=\\n[A-Za-z\\s]+(developer|engineer|manager|intern|analyst)[\\s\\n])|(?=\\n[A-Z])");
            for (int i = 0; i < entries.length && i < 5; i++) {
                String entry = entries[i].trim();
                if (entry.length() > 20 && !entry.startsWith("•")) {
                    if (i > 0) json.append(",");
                    json.append("{")
                        .append("\"title\":\"").append(escapeJson(extractJobTitle(entry))).append("\",")
                        .append("\"company\":\"").append(escapeJson(extractCompany(entry))).append("\",")
                        .append("\"duration\":\"").append(escapeJson(extractDuration(entry))).append("\",")
                        .append("\"location\":\"\",")
                        .append("\"description\":\"").append(escapeJson(extractDescription(entry))).append("\"")
                        .append("}");
                }
            }
        } else if ("projects".contains(type)) {
            // Split by project names
            String[] entries = text.split("(?=\\n[A-Z][a-zA-Z0-9\\s]+\\|)");
            for (int i = 0; i < entries.length && i < 5; i++) {
                String entry = entries[i].trim();
                if (entry.length() > 10) {
                    if (i > 0) json.append(",");
                    json.append("{")
                        .append("\"name\":\"").append(escapeJson(extractProjectName(entry))).append("\",")
                        .append("\"technologies\":\"").append(escapeJson(extractTechnologies(entry))).append("\",")
                        .append("\"description\":\"").append(escapeJson(extractDescription(entry))).append("\",")
                        .append("\"url\":\"\"")
                        .append("}");
                }
            }
        }
        
        json.append("]");
        return json.toString();
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", " ")
                  .replace("\r", "")
                  .trim();
    }
    
    private String extractLine(String text, int lineNumber) {
        String[] lines = text.split("\n");
        return lineNumber < lines.length ? lines[lineNumber].trim() : "";
    }
    
    private String extractInstitution(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.contains("University") || line.contains("College") || line.contains("Institute")) {
                return line.trim();
            }
        }
        return lines.length > 0 ? lines[0].trim() : "";
    }
    
    private String extractYear(String text) {
        Pattern pattern = Pattern.compile("(\\d{4}\\s*-\\s*\\d{4}|\\d{4}\\s*-\\s*Present|\\w{3}\\.?\\s*\\d{4}\\s*-\\s*\\w{3}\\.?\\s*\\d{4})");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : "";
    }
    
    private String extractJobTitle(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.matches(".*(?i)(developer|engineer|manager|intern|analyst|designer|consultant).*") && line.length() < 80) {
                return line;
            }
        }
        return lines.length > 0 ? lines[0].trim() : "";
    }
    
    private String extractCompany(String text) {
        String[] lines = text.split("\n");
        for (int i = 0; i < Math.min(3, lines.length); i++) {
            String line = lines[i].trim();
            if (!line.matches(".*(?i)(developer|engineer|manager|intern).*") && line.length() > 5 && line.length() < 80) {
                return line.replaceAll("\\d{4}.*", "").trim();
            }
        }
        return "";
    }
    
    private String extractDuration(String text) {
        Pattern pattern = Pattern.compile("(\\w{3}\\.?\\s*\\d{4}\\s*-\\s*(\\w{3}\\.?\\s*\\d{4}|Present))");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : "";
    }
    
    private String extractDescription(String text) {
        StringBuilder description = new StringBuilder();
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("•") || line.startsWith("-") || line.startsWith("*")) {
                if (description.length() > 0) description.append(" ");
                description.append(line);
                if (description.length() > 500) break; // Limit length
            }
        }
        return description.toString();
    }
    
    private String extractProjectName(String text) {
        String[] lines = text.split("\n");
        if (lines.length > 0) {
            return lines[0].split("\\|")[0].trim();
        }
        return "";
    }
    
    private String extractTechnologies(String text) {
        String[] lines = text.split("\n");
        if (lines.length > 0 && lines[0].contains("|")) {
            String[] parts = lines[0].split("\\|");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        }
        return "";
    }
}
