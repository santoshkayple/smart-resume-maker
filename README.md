# 🎯 Smart Resume Builder

An AI-powered resume builder that optimizes your resume based on job descriptions, provides ATS compatibility scores, and generates professional PDFs.

## ✨ Features

- **Resume Upload & Parsing**: Upload PDF/DOCX resumes and automatically extract information
- **Job Description Analysis**: Paste job descriptions to extract key requirements
- **Smart Matching**: Calculate compatibility scores between resume and JD
- **AI Optimization**: Automatically improve resume content based on job requirements
- **ATS Score**: Check how well your resume will perform with Applicant Tracking Systems
- **Keyword Analysis**: Identify missing keywords and get suggestions
- **Multiple Templates**: Export resumes in Classic, Modern, and Professional formats
- **Real-time Editing**: Edit resume sections with live preview
- **Version History**: Track changes and compare different versions

## 🏗️ Architecture

```
Backend: Spring Boot 3.2.0 + Java 17
Database: H2 (embedded)
Resume Parsing: Apache PDFBox + Apache POI
PDF Generation: iText 8
Text Analysis: Custom NLP algorithms
```

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

## 🚀 Quick Start

### 1. Clone the Repository

```bash
cd /Users/santsohdahit/dev/project/smart-resume-builder
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The server will start at `http://localhost:8080`

### 4. Access H2 Database Console

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/resumedb
Username: sa
Password: (leave empty)
```

## 📡 API Endpoints

### Resume Endpoints

```
POST   /api/resumes/upload              - Upload resume file (PDF/DOCX)
POST   /api/resumes/create              - Create resume manually
GET    /api/resumes                     - Get all resumes
GET    /api/resumes/{id}                - Get specific resume
PUT    /api/resumes/{id}                - Update resume
DELETE /api/resumes/{id}                - Delete resume
POST   /api/resumes/{id}/match?jdId=X   - Calculate match score
POST   /api/resumes/{id}/optimize?jdId=X - Optimize resume
GET    /api/resumes/{id}/optimization-suggestions?jdId=X - Get suggestions
GET    /api/resumes/{id}/download?template=classic - Download PDF
```

### Job Description Endpoints

```
POST   /api/job-descriptions            - Create/analyze JD
GET    /api/job-descriptions            - Get all JDs
GET    /api/job-descriptions/{id}       - Get specific JD
PUT    /api/job-descriptions/{id}       - Update JD
DELETE /api/job-descriptions/{id}       - Delete JD
```

## 📝 Usage Examples

### 1. Upload Resume

```bash
curl -X POST http://localhost:8080/api/resumes/upload \
  -F "file=@/path/to/resume.pdf"
```

### 2. Create Job Description

```bash
curl -X POST http://localhost:8080/api/job-descriptions \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Looking for Software Engineer with 3+ years experience in Java, Spring Boot, React..."
  }'
```

### 3. Match Resume with JD

```bash
curl -X POST "http://localhost:8080/api/resumes/1/match?jdId=1"
```

Response:
```json
{
  "overallScore": 75.5,
  "skillsMatchScore": 80.0,
  "experienceMatchScore": 70.0,
  "atsScore": 85.0,
  "matchedKeywords": ["java", "spring boot", "react"],
  "missingKeywords": ["kubernetes", "aws"],
  "suggestions": [
    "Add more relevant technical skills",
    "Missing key skills: kubernetes, aws"
  ],
  "strength": "Strong technical skills alignment",
  "weakness": "Minor improvements needed"
}
```

### 4. Optimize Resume

```bash
curl -X POST "http://localhost:8080/api/resumes/1/optimize?jdId=1"
```

### 5. Download Resume PDF

```bash
curl -X GET "http://localhost:8080/api/resumes/1/download?template=modern" \
  --output resume.pdf
```

## 🎨 Available PDF Templates

- **classic**: Traditional black and white format
- **modern**: Colorful with accent highlights
- **professional**: Clean corporate style

## 🧪 Testing

Run all tests:
```bash
mvn test
```

## 📊 How It Works

### 1. Resume Parsing
- Extracts text from PDF/DOCX using Apache PDFBox and POI
- Uses regex patterns to identify:
  - Email, phone, LinkedIn, GitHub
  - Section headers (Experience, Education, Skills, etc.)
  - Structured content

### 2. Job Description Analysis
- Extracts job title, company, and requirements
- Identifies required vs preferred skills
- Extracts key responsibilities
- Builds keyword index

### 3. Matching Algorithm
- **Skills Match**: Jaccard similarity between skill sets
- **Experience Match**: Keyword overlap analysis
- **ATS Score**: Checks formatting, keywords, and completeness
- **Overall Score**: Weighted average (40% skills, 30% keywords, 30% experience)

### 4. Optimization
- Adds missing keywords naturally
- Improves bullet points with action verbs
- Enhances summary with job-relevant terms
- Quantifies achievements where possible

## 🔧 Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server port
server.port=8080

# Database location
spring.datasource.url=jdbc:h2:file:./data/resumedb

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging level
logging.level.com.resume.builder=DEBUG
```

## 📁 Project Structure

```
src/main/java/com/resume/builder/
├── ResumeBuilderApplication.java    # Main application
├── controller/
│   ├── ResumeController.java        # Resume REST endpoints
│   └── JobDescriptionController.java # JD REST endpoints
├── service/
│   ├── ResumeParserService.java     # Parse PDF/DOCX
│   ├── JDAnalyzerService.java       # Analyze job descriptions
│   ├── MatchingService.java         # Calculate match scores
│   ├── OptimizationService.java     # Optimize resumes
│   └── PDFGeneratorService.java     # Generate PDF output
├── model/
│   ├── Resume.java                  # Resume entity
│   ├── JobDescription.java          # JD entity
│   └── MatchResult.java             # Match result entity
├── repository/
│   ├── ResumeRepository.java
│   ├── JobDescriptionRepository.java
│   └── MatchResultRepository.java
├── dto/
│   ├── ResumeDTO.java
│   ├── MatchScoreDTO.java
│   └── OptimizationSuggestionDTO.java
└── config/
    └── CorsConfig.java              # CORS configuration
```

## 🚧 Future Enhancements

- [ ] Add more sophisticated NLP (Stanford CoreNLP integration)
- [ ] Machine learning-based keyword extraction
- [ ] Multi-language support
- [ ] LinkedIn integration
- [ ] Cover letter generation
- [ ] Interview question suggestions
- [ ] Salary estimation based on skills
- [ ] Resume scoring history graphs

## 🐛 Troubleshooting

### Issue: Port 8080 already in use
```bash
# Change port in application.properties
server.port=8081
```

### Issue: File upload fails
```bash
# Increase upload size in application.properties
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
```

### Issue: PDF generation fails
```bash
# Ensure iText dependencies are properly loaded
mvn clean install -U
```

## 📄 License

This project is for educational purposes.

## 👨‍💻 Author

**Santosh**
- Project: Undergraduate Thesis
- Tech Stack: Spring Boot + Java 17

## 🙏 Acknowledgments

- Apache PDFBox for PDF parsing
- Apache POI for Word document handling
- iText for PDF generation
- Spring Boot for the excellent framework

---

**Happy Resume Building! 🎉**
