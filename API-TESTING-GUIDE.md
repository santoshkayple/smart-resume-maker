# API Testing Guide - Smart Resume Builder

## Base URL
```
http://localhost:8080/api
```

---

## 📋 Resume Endpoints

### 1. Upload Resume (PDF/DOCX)
```bash
POST /api/resumes/upload
Content-Type: multipart/form-data

curl -X POST http://localhost:8080/api/resumes/upload \
  -F "file=@sample-resume.txt"
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@email.com",
  "phone": "+1-555-0123",
  "linkedin": "linkedin.com/in/johndoe",
  "github": "github.com/johndoe",
  "summary": "Experienced Software Engineer...",
  "experience": "Senior Software Engineer...",
  "education": "Bachelor of Science...",
  "skills": "Java, Spring Boot, React...",
  "projects": "E-Commerce Platform...",
  "certifications": "AWS Certified...",
  "createdAt": "2024-11-14T10:30:00",
  "updatedAt": "2024-11-14T10:30:00"
}
```

---

### 2. Create Resume Manually
```bash
POST /api/resumes/create
Content-Type: application/json

curl -X POST http://localhost:8080/api/resumes/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane@email.com",
    "phone": "+1-555-9999",
    "summary": "Full-stack developer with 3 years experience",
    "skills": "Python, Django, React, PostgreSQL",
    "experience": "Software Developer at TechCo",
    "education": "BS Computer Science"
  }'
```

---

### 3. Get All Resumes
```bash
GET /api/resumes

curl http://localhost:8080/api/resumes
```

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@email.com",
    ...
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@email.com",
    ...
  }
]
```

---

### 4. Get Single Resume
```bash
GET /api/resumes/{id}

curl http://localhost:8080/api/resumes/1
```

---

### 5. Update Resume
```bash
PUT /api/resumes/{id}
Content-Type: application/json

curl -X PUT http://localhost:8080/api/resumes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@email.com",
    "phone": "+1-555-0123",
    "summary": "Updated summary with more keywords...",
    "skills": "Java, Spring Boot, React, Docker, AWS, Kubernetes",
    "experience": "Updated experience section...",
    "education": "BS Computer Science, MIT"
  }'
```

---

### 6. Delete Resume
```bash
DELETE /api/resumes/{id}

curl -X DELETE http://localhost:8080/api/resumes/1
```

**Response (204 No Content)**

---

### 7. Match Resume with Job Description
```bash
POST /api/resumes/{id}/match?jdId={jdId}

curl -X POST "http://localhost:8080/api/resumes/1/match?jdId=1"
```

**Response (200 OK)**:
```json
{
  "overallScore": 78.5,
  "skillsMatchScore": 85.0,
  "experienceMatchScore": 75.0,
  "atsScore": 82.0,
  "matchedKeywords": [
    "java",
    "spring boot",
    "docker",
    "kubernetes",
    "microservices",
    "aws"
  ],
  "missingKeywords": [
    "graphql",
    "kafka",
    "terraform"
  ],
  "suggestions": [
    "Your resume is a good match! Consider highlighting specific achievements.",
    "Add more relevant technical skills mentioned in the job description.",
    "Missing key skills: graphql, kafka, terraform"
  ],
  "strength": "Strong technical skills alignment with job requirements",
  "weakness": "Minor improvements needed for better match"
}
```

---

### 8. Optimize Resume Based on JD
```bash
POST /api/resumes/{id}/optimize?jdId={jdId}

curl -X POST "http://localhost:8080/api/resumes/1/optimize?jdId=1"
```

**Response (200 OK)**:
Returns optimized resume with:
- Enhanced summary with job-relevant keywords
- Improved skills section
- Better bullet points with action verbs
- Added missing keywords

---

### 9. Get Optimization Suggestions
```bash
GET /api/resumes/{id}/optimization-suggestions?jdId={jdId}

curl -X GET "http://localhost:8080/api/resumes/1/optimization-suggestions?jdId=1"
```

**Response (200 OK)**:
```json
{
  "optimizedSummary": "Motivated Senior Software Engineer professional. Experienced Software Engineer with 5+ years in building scalable web applications. Experienced in graphql, kafka, terraform.",
  "optimizedExperience": "• Developed and developed microservices using Spring Boot...\n• Led team of 5 developers...",
  "optimizedSkills": "aws, docker, graphql, java, kafka, kubernetes, spring boot, terraform",
  "bulletPointImprovements": {
    "Led a team of 5 developers": "• Developed Led a team of 5 developers",
    "Built full-stack web applications": "• Implemented Built full-stack web applications"
  },
  "addedKeywords": 3
}
```

---

### 10. Download Resume as PDF
```bash
GET /api/resumes/{id}/download?template={classic|modern|professional}

# Classic template
curl -X GET "http://localhost:8080/api/resumes/1/download?template=classic" \
  --output resume_classic.pdf

# Modern template (with colors)
curl -X GET "http://localhost:8080/api/resumes/1/download?template=modern" \
  --output resume_modern.pdf

# Professional template
curl -X GET "http://localhost:8080/api/resumes/1/download?template=professional" \
  --output resume_professional.pdf
```

**Response (200 OK)**:
Returns PDF file as binary data

---

## 💼 Job Description Endpoints

### 1. Create Job Description
```bash
POST /api/job-descriptions
Content-Type: application/json

curl -X POST http://localhost:8080/api/job-descriptions \
  -H "Content-Type: application/json" \
  -d '{
    "description": "We are looking for a Senior Software Engineer with 5+ years experience in Java, Spring Boot, microservices, Docker, Kubernetes, AWS. Experience with React is a plus. Responsibilities include designing scalable systems and leading development teams."
  }'
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "jobTitle": "Senior Software Engineer",
  "companyName": "",
  "description": "We are looking for a Senior Software Engineer...",
  "requiredSkills": "java, spring boot, microservices, docker, kubernetes, aws, react",
  "preferredSkills": "graphql, kafka",
  "responsibilities": "designing scalable systems and leading development teams",
  "createdAt": "2024-11-14T10:35:00"
}
```

---

### 2. Get All Job Descriptions
```bash
GET /api/job-descriptions

curl http://localhost:8080/api/job-descriptions
```

---

### 3. Get Single Job Description
```bash
GET /api/job-descriptions/{id}

curl http://localhost:8080/api/job-descriptions/1
```

---

### 4. Update Job Description
```bash
PUT /api/job-descriptions/{id}
Content-Type: application/json

curl -X PUT http://localhost:8080/api/job-descriptions/1 \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Updated job description text..."
  }'
```

---

### 5. Delete Job Description
```bash
DELETE /api/job-descriptions/{id}

curl -X DELETE http://localhost:8080/api/job-descriptions/1
```

---

## 🧪 Complete Testing Workflow

### Step 1: Start the Application
```bash
cd /Users/santsohdahit/dev/project/smart-resume-builder
./start.sh
```

### Step 2: Create a Job Description
```bash
curl -X POST http://localhost:8080/api/job-descriptions \
  -H "Content-Type: application/json" \
  -d "$(cat sample-job-description.txt | jq -Rs '{description: .}')"
```

### Step 3: Upload a Resume
```bash
curl -X POST http://localhost:8080/api/resumes/upload \
  -F "file=@sample-resume.txt" \
  | jq '.'
```

### Step 4: Get Match Score
```bash
curl -X POST "http://localhost:8080/api/resumes/1/match?jdId=1" | jq '.'
```

### Step 5: Get Optimization Suggestions
```bash
curl -X GET "http://localhost:8080/api/resumes/1/optimization-suggestions?jdId=1" | jq '.'
```

### Step 6: Optimize the Resume
```bash
curl -X POST "http://localhost:8080/api/resumes/1/optimize?jdId=1" | jq '.'
```

### Step 7: Download Optimized Resume
```bash
curl -X GET "http://localhost:8080/api/resumes/1/download?template=modern" \
  --output optimized_resume.pdf
```

### Step 8: Open the PDF
```bash
open optimized_resume.pdf  # macOS
# or
xdg-open optimized_resume.pdf  # Linux
```

---

## 📊 Response Status Codes

| Code | Meaning | When |
|------|---------|------|
| 200 | OK | Successful GET, PUT, POST |
| 201 | Created | Successfully created resource |
| 204 | No Content | Successfully deleted |
| 400 | Bad Request | Invalid data sent |
| 404 | Not Found | Resource doesn't exist |
| 500 | Server Error | Internal error |

---

## 🔧 Using Postman

### Import Collection
1. Open Postman
2. Click "Import"
3. Create new collection "Smart Resume Builder"
4. Add requests as shown above
5. Set environment variable: `baseUrl = http://localhost:8080/api`

### Sample Request in Postman
```
Method: POST
URL: {{baseUrl}}/resumes/upload
Body: form-data
  - Key: file
  - Type: File
  - Value: [Select sample-resume.txt]
```

---

## 🐛 Common Issues

### Issue: Connection Refused
**Solution**: Make sure the application is running
```bash
./start.sh
```

### Issue: 404 Not Found
**Solution**: Check the endpoint URL and ID exists
```bash
# List all resumes first
curl http://localhost:8080/api/resumes
```

### Issue: 500 Internal Server Error
**Solution**: Check application logs
```bash
# Logs are printed in the console where you ran ./start.sh
```

---

## 💡 Pro Tips

1. **Use jq for pretty JSON output**:
   ```bash
   curl http://localhost:8080/api/resumes | jq '.'
   ```

2. **Save response to file**:
   ```bash
   curl http://localhost:8080/api/resumes/1 > resume.json
   ```

3. **Verbose output for debugging**:
   ```bash
   curl -v http://localhost:8080/api/resumes
   ```

4. **Test with Postman for easier debugging**:
   - Better visualization
   - Save requests
   - Environment variables
   - Collection runner

---

**Happy Testing! 🚀**
