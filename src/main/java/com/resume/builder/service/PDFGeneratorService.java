package com.resume.builder.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.resume.builder.model.Resume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class PDFGeneratorService {

    public byte[] generateResumePDF(Resume resume, String template) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            if ("modern".equalsIgnoreCase(template)) {
                generateModernTemplate(document, resume);
            } else if ("professional".equalsIgnoreCase(template)) {
                generateProfessionalTemplate(document, resume);
            } else {
                generateClassicTemplate(document, resume);
            }
        }
        
        return baos.toByteArray();
    }

    private void generateClassicTemplate(Document document, Resume resume) {
        // Header - Name
        Paragraph name = new Paragraph(resume.getName() != null ? resume.getName() : "Your Name")
            .setFontSize(24)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER);
        document.add(name);
        
        // Contact Info
        StringBuilder contact = new StringBuilder();
        if (resume.getEmail() != null) contact.append(resume.getEmail()).append(" | ");
        if (resume.getPhone() != null) contact.append(resume.getPhone()).append(" | ");
        if (resume.getLinkedIn() != null) contact.append(resume.getLinkedIn());
        
        if (contact.length() > 0) {
            Paragraph contactPara = new Paragraph(contact.toString())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(contactPara);
        }
        
        document.add(new Paragraph("\n"));
        
        // Summary
        if (resume.getSummary() != null && !resume.getSummary().isEmpty()) {
            addSection(document, "PROFESSIONAL SUMMARY", resume.getSummary());
        }
        
        // Experience
        if (resume.getExperience() != null && !resume.getExperience().isEmpty()) {
            addSection(document, "WORK EXPERIENCE", resume.getExperience());
        }
        
        // Education
        if (resume.getEducation() != null && !resume.getEducation().isEmpty()) {
            addSection(document, "EDUCATION", resume.getEducation());
        }
        
        // Skills
        if (resume.getSkills() != null && !resume.getSkills().isEmpty()) {
            addSection(document, "SKILLS", resume.getSkills());
        }
        
        // Projects
        if (resume.getProjects() != null && !resume.getProjects().isEmpty()) {
            addSection(document, "PROJECTS", resume.getProjects());
        }
        
        // Certifications
        if (resume.getCertifications() != null && !resume.getCertifications().isEmpty()) {
            addSection(document, "CERTIFICATIONS", resume.getCertifications());
        }
    }

    private void generateModernTemplate(Document document, Resume resume) {
        // Similar to classic but with color accents
        DeviceRgb primaryColor = new DeviceRgb(41, 128, 185); // Blue
        
        // Header with color
        Paragraph name = new Paragraph(resume.getName() != null ? resume.getName() : "Your Name")
            .setFontSize(28)
            .setBold()
            .setFontColor(primaryColor)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(name);
        
        // Contact Info
        StringBuilder contact = new StringBuilder();
        if (resume.getEmail() != null) contact.append(resume.getEmail()).append(" | ");
        if (resume.getPhone() != null) contact.append(resume.getPhone()).append(" | ");
        if (resume.getLinkedIn() != null) contact.append(resume.getLinkedIn());
        
        if (contact.length() > 0) {
            Paragraph contactPara = new Paragraph(contact.toString())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(contactPara);
        }
        
        document.add(new Paragraph("\n"));
        
        // Sections with colored headers
        if (resume.getSummary() != null && !resume.getSummary().isEmpty()) {
            addColoredSection(document, "PROFESSIONAL SUMMARY", resume.getSummary(), primaryColor);
        }
        
        if (resume.getExperience() != null && !resume.getExperience().isEmpty()) {
            addColoredSection(document, "WORK EXPERIENCE", resume.getExperience(), primaryColor);
        }
        
        if (resume.getEducation() != null && !resume.getEducation().isEmpty()) {
            addColoredSection(document, "EDUCATION", resume.getEducation(), primaryColor);
        }
        
        if (resume.getSkills() != null && !resume.getSkills().isEmpty()) {
            addColoredSection(document, "SKILLS", resume.getSkills(), primaryColor);
        }
        
        if (resume.getProjects() != null && !resume.getProjects().isEmpty()) {
            addColoredSection(document, "PROJECTS", resume.getProjects(), primaryColor);
        }
        
        if (resume.getCertifications() != null && !resume.getCertifications().isEmpty()) {
            addColoredSection(document, "CERTIFICATIONS", resume.getCertifications(), primaryColor);
        }
    }

    private void generateProfessionalTemplate(Document document, Resume resume) {
        // Name in uppercase
        Paragraph name = new Paragraph(
                resume.getName() != null ? resume.getName().toUpperCase() : "YOUR NAME")
            .setFontSize(26)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER);
        document.add(name);
        
        // Contact Info
        StringBuilder contact = new StringBuilder();
        if (resume.getEmail() != null) contact.append(resume.getEmail()).append(" | ");
        if (resume.getPhone() != null) contact.append(resume.getPhone()).append(" | ");
        if (resume.getLinkedIn() != null) contact.append(resume.getLinkedIn());
        
        if (contact.length() > 0) {
            Paragraph contactPara = new Paragraph(contact.toString())
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(contactPara);
        }
        
        document.add(new Paragraph("\n"));
        
        // Sections
        if (resume.getSummary() != null && !resume.getSummary().isEmpty()) {
            addSection(document, "PROFESSIONAL SUMMARY", resume.getSummary());
        }
        
        if (resume.getExperience() != null && !resume.getExperience().isEmpty()) {
            addSection(document, "PROFESSIONAL EXPERIENCE", resume.getExperience());
        }
        
        if (resume.getEducation() != null && !resume.getEducation().isEmpty()) {
            addSection(document, "EDUCATION", resume.getEducation());
        }
        
        if (resume.getSkills() != null && !resume.getSkills().isEmpty()) {
            addSection(document, "TECHNICAL SKILLS", resume.getSkills());
        }
        
        if (resume.getProjects() != null && !resume.getProjects().isEmpty()) {
            addSection(document, "KEY PROJECTS", resume.getProjects());
        }
        
        if (resume.getCertifications() != null && !resume.getCertifications().isEmpty()) {
            addSection(document, "CERTIFICATIONS & LICENSES", resume.getCertifications());
        }
    }

    private void addSection(Document document, String title, String content) {
        // Section title
        Paragraph sectionTitle = new Paragraph(title)
            .setFontSize(14)
            .setBold()
            .setMarginTop(10)
            .setMarginBottom(5);
        document.add(sectionTitle);
        
        // Underline
        Paragraph underline = new Paragraph("_".repeat(80))
            .setFontSize(8)
            .setMarginTop(0)
            .setMarginBottom(5);
        document.add(underline);
        
        // Content
        Paragraph contentPara = new Paragraph(content)
            .setFontSize(11)
            .setMarginBottom(10);
        document.add(contentPara);
    }

    private void addColoredSection(Document document, String title, String content, DeviceRgb color) {
        // Section title with color
        Paragraph sectionTitle = new Paragraph(title)
            .setFontSize(14)
            .setBold()
            .setFontColor(color)
            .setMarginTop(10)
            .setMarginBottom(5);
        document.add(sectionTitle);
        
        // Colored underline
        Paragraph underline = new Paragraph("_".repeat(80))
            .setFontSize(8)
            .setFontColor(color)
            .setMarginTop(0)
            .setMarginBottom(5);
        document.add(underline);
        
        // Content
        Paragraph contentPara = new Paragraph(content)
            .setFontSize(11)
            .setMarginBottom(10);
        document.add(contentPara);
    }
}
