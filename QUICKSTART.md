# 📋 Quick Start Guide

## Setup & Run

### 1. Build and Run (Quick)
```bash
cd /Users/santsohdahit/dev/project/smart-resume-builder
./start.sh
```

### 2. Manual Run
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

### 3. Access the Application
- **API Base URL**: http://localhost:8080/api
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/resumedb`
  - Username: `sa`
  - Password: (empty)

## Testing the API

### Test 1: Upload a Resume

Create a test resume file (`test-resume.txt`):
```
John Doe
john.doe@email.com | +1-555-0123 | linkedin.com/in/johndoe

PROFESSIONAL SUMMARY
Experienced Software Engineer with 5+ years in Java and Spring Boot development.

WORK EXPERIENCE
Senior Software Engineer - Tech Corp (2020-Present)
• Developed microservices using Spring Boot and Docker
• Led team of 5 developers
• Improved system performance by 40%

Software Engineer - StartupXYZ (2018-2020)
• Built RESTful APIs using Java and MySQL
• Implemented CI/CD pipelines

EDUCATION
Bachelor of Computer Science - University Name (2018)

SKILLS
Java, Spring Boot, React, MySQL, Docker, Kubernetes, AWS, Git, REST API

PROJECTS
E-commerce Platform - Built full-stack application with Spring Boot and React

CERTIFICATIONS
AWS Certified Solutions Architect
```

Upload it:
```bash
curl -X POST http://localhost:8080/api/resumes/upload \
  -F "file=@test-resume.txt" \
  | json_pp
```

### Test 2: Create Job Description

```bash
curl -X POST http://localhost:8080/api/job-descriptions \
  -H "Content-Type: application/json" \
  -d '{
    "description": "We are looking for a Senior Software Engineer with strong Java and Spring Boot experience. Required: 5+ years Java, Spring Boot, microservices, Docker, Kubernetes, AWS. Experience with React is a plus. Responsibilities include designing scalable systems and leading development teams."
  }' | json_pp
```

### Test 3: Match Resume with Job

```bash
curl -X POST "http://localhost:8080/api/resumes/1/match?jdId=1" | json_pp
```

### Test 4: Get Optimization Suggestions

```bash
curl -X GET "http://localhost:8080/api/resumes/1/optimization-suggestions?jdId=1" | json_pp
```

### Test 5: Optimize Resume

```bash
curl -X POST "http://localhost:8080/api/resumes/1/optimize?jdId=1" | json_pp
```

### Test 6: Download PDF

```bash
# Classic template
curl -X GET "http://localhost:8080/api/resumes/1/download?template=classic" \
  --output resume_classic.pdf

# Modern template
curl -X GET "http://localhost:8080/api/resumes/1/download?template=modern" \
  --output resume_modern.pdf

# Professional template
curl -X GET "http://localhost:8080/api/resumes/1/download?template=professional" \
  --output resume_professional.pdf
```

### Test 7: Get All Resumes

```bash
curl -X GET http://localhost:8080/api/resumes | json_pp
```

### Test 8: Update Resume

```bash
curl -X PUT http://localhost:8080/api/resumes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@email.com",
    "phone": "+1-555-0123",
    "summary": "Expert Software Engineer with 5+ years specializing in cloud-native applications",
    "skills": "Java, Spring Boot, React, Docker, Kubernetes, AWS, MySQL, MongoDB"
  }' | json_pp
```

## Using Postman

Import these requests into Postman:

### Collection: Smart Resume Builder

```json
{
  "info": {
    "name": "Smart Resume Builder",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Upload Resume",
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "formdata",
          "formdata": [
            {
              "key": "file",
              "type": "file",
              "src": "test-resume.pdf"
            }
          ]
        },
        "url": {
          "raw": "http://localhost:8080/api/resumes/upload",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "resumes", "upload"]
        }
      }
    },
    {
      "name": "Create Job Description",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"description\": \"Your JD text here\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/job-descriptions",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "job-descriptions"]
        }
      }
    },
    {
      "name": "Match Resume with JD",
      "request": {
        "method": "POST",
        "url": {
          "raw": "http://localhost:8080/api/resumes/1/match?jdId=1",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "resumes", "1", "match"],
          "query": [
            {
              "key": "jdId",
              "value": "1"
            }
          ]
        }
      }
    }
  ]
}
```

## Troubleshooting

### Port already in use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Build errors
```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

### Database issues
```bash
# Delete database and restart
rm -rf data/
mvn spring-boot:run
```

## Expected Response Examples

### Match Score Response
```json
{
  "overallScore": 78.5,
  "skillsMatchScore": 85.0,
  "experienceMatchScore": 75.0,
  "atsScore": 82.0,
  "matchedKeywords": ["java", "spring boot", "docker", "kubernetes"],
  "missingKeywords": ["aws", "mongodb"],
  "suggestions": [
    "Add more relevant technical skills",
    "Missing key skills: aws, mongodb"
  ],
  "strength": "Strong technical skills alignment",
  "weakness": "Minor improvements needed"
}
```

### Optimization Suggestions Response
```json
{
  "optimizedSummary": "Motivated Senior Software Engineer professional...",
  "optimizedExperience": "• Developed microservices...",
  "optimizedSkills": "aws, docker, java, kubernetes, mongodb, spring boot...",
  "bulletPointImprovements": {
    "Led team of 5 developers": "• Developed Led team of 5 developers",
    "Built RESTful APIs": "• Implemented Built RESTful APIs"
  },
  "addedKeywords": 2
}
```

## Next Steps

1. ✅ Run the application
2. ✅ Test all endpoints
3. ✅ Upload your actual resume
4. ✅ Create real job descriptions
5. ✅ Get match scores and optimize
6. ⏭️  Build the frontend (React)
7. ⏭️  Add more features

---

**Need help?** Check the main README.md file!
