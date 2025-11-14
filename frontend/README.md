# Smart Resume Builder - Frontend

React frontend for the Smart Resume Builder application.

## Features

- **Dashboard**: View and manage all your resumes
- **Resume Upload**: Upload PDF/DOCX files with automatic parsing
- **Resume Builder**: Create and edit resumes with a rich form interface
- **Job Descriptions**: Manage job descriptions for matching
- **Matching Engine**: Calculate match scores and get AI-powered suggestions
- **PDF Export**: Download resumes in multiple templates

## Tech Stack

- React 18
- Vite (Build tool)
- React Router (Navigation)
- Axios (API calls)
- Tailwind CSS (Styling)
- Lucide React (Icons)

## Getting Started

### Prerequisites

- Node.js 16+ and npm
- Backend server running on http://localhost:8080

### Installation

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install
```

### Running the Application

```bash
# Start development server
npm run dev

# The app will be available at http://localhost:5173
```

### Building for Production

```bash
# Create production build
npm run build

# Preview production build
npm run preview
```

## Project Structure

```
frontend/
├── src/
│   ├── pages/
│   │   ├── Dashboard.jsx          # Home page with resume list
│   │   ├── ResumeUpload.jsx       # Upload resume files
│   │   ├── ResumeBuilder.jsx      # Create/edit resumes
│   │   ├── JobDescriptions.jsx    # Manage job descriptions
│   │   └── MatchingPage.jsx       # Match resumes with JDs
│   ├── services/
│   │   └── api.js                 # API integration
│   ├── App.jsx                    # Main app component
│   ├── main.jsx                   # App entry point
│   └── index.css                  # Global styles
├── index.html
├── package.json
├── tailwind.config.js
├── postcss.config.js
└── vite.config.js
```

## API Integration

The frontend communicates with the Spring Boot backend through REST APIs:

- Resume APIs: `/api/resumes/*`
- Job Description APIs: `/api/job-descriptions/*`

All API calls are handled in `src/services/api.js`

## Available Pages

### 1. Dashboard (/)
- View all resumes
- Quick actions (Upload, Create, JDs)
- Resume management (Edit, Delete, Download)

### 2. Resume Upload (/upload)
- Drag & drop file upload
- Automatic resume parsing
- Support for PDF, DOCX, TXT

### 3. Resume Builder (/resume/:id)
- Create new resumes
- Edit existing resumes
- Multiple sections (Personal, Experience, Education, Skills, Projects)
- Download PDF in different templates

### 4. Job Descriptions (/job-descriptions)
- Add new job descriptions
- AI-powered analysis
- Edit and delete JDs

### 5. Matching (/matching)
- Select resume and JD
- Calculate match scores
- View detailed analysis
- Get optimization suggestions
- Apply AI optimization

## Key Features Explained

### Resume Parsing
When you upload a resume, the backend automatically extracts:
- Personal information (name, email, phone)
- Professional summary
- Work experience
- Education
- Skills
- Projects
- Certifications

### Matching Algorithm
The matching engine calculates:
- **Overall Score**: Weighted average of all scores
- **Skills Match**: How well your skills match the requirements
- **Experience Match**: Relevance of your experience
- **ATS Score**: How well your resume will perform with ATS systems

### Optimization
AI-powered optimization:
- Adds missing keywords
- Improves bullet points
- Enhances summary
- Suggests improvements

## Styling

The app uses Tailwind CSS for styling with a custom color scheme:
- Primary: Indigo (600, 700)
- Secondary: Purple (600, 700)
- Success: Green (600, 700)
- Warning: Yellow (600, 700)
- Danger: Red (600, 700)

## Environment Variables

Create a `.env` file in the frontend directory if needed:

```
VITE_API_BASE_URL=http://localhost:8080/api
```

## Troubleshooting

### Issue: API calls fail
**Solution**: Make sure the backend server is running on port 8080

### Issue: CORS errors
**Solution**: Check that CORS is properly configured in the backend (CorsConfig.java)

### Issue: npm install fails
**Solution**: Delete `node_modules` and `package-lock.json`, then run `npm install` again

### Issue: Port 5173 already in use
**Solution**: Kill the process or change the port in vite.config.js

## Development Tips

1. **Hot Reload**: Vite provides fast hot reload during development
2. **API Testing**: Use browser DevTools Network tab to inspect API calls
3. **State Management**: Uses React hooks (useState, useEffect)
4. **Error Handling**: Check browser console for errors

## Contributing

This is an undergraduate thesis project. Feel free to fork and modify for your own use.

## License

Educational use only.

## Author

Santosh - Undergraduate Thesis Project 2024-2025
