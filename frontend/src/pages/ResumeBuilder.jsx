import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Save, Download, ArrowLeft } from 'lucide-react';
import { resumeAPI } from '../services/api';

function ResumeBuilder() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [resume, setResume] = useState({
    name: '',
    email: '',
    phone: '',
    linkedin: '',
    github: '',
    summary: '',
    experience: '',
    education: '',
    skills: '',
    projects: '',
    certifications: '',
    template: 'classic'
  });

  useEffect(() => {
    if (id && id !== 'new') {
      fetchResume();
    } else {
      setLoading(false);
    }
  }, [id]);

  const fetchResume = async () => {
    try {
      const response = await resumeAPI.getResume(id);
      setResume(response.data);
    } catch (error) {
      console.error('Error fetching resume:', error);
      alert('Failed to load resume');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (field, value) => {
    setResume(prev => ({ ...prev, [field]: value }));
  };

  const handleSave = async () => {
    setSaving(true);
    try {
      if (id && id !== 'new') {
        await resumeAPI.updateResume(id, resume);
        alert('Resume updated successfully!');
      } else {
        const response = await resumeAPI.createResume(resume);
        alert('Resume created successfully!');
        navigate(`/resume/${response.data.id}`);
      }
    } catch (error) {
      console.error('Error saving resume:', error);
      alert('Failed to save resume');
    } finally {
      setSaving(false);
    }
  };

  const handleDownload = async (template) => {
    if (!id || id === 'new') {
      alert('Please save the resume first');
      return;
    }

    try {
      const response = await resumeAPI.downloadPDF(id, template);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `${resume.name.replace(/\s+/g, '_')}_${template}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error('Error downloading PDF:', error);
      alert('Failed to download PDF');
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="text-center">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-4 border-indigo-600 border-t-transparent"></div>
          <p className="mt-4 text-gray-600">Loading resume...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto space-y-6 animate-slide-in">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <button
            onClick={() => navigate('/')}
            className="btn-secondary"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              {id === 'new' ? 'Create Resume' : 'Edit Resume'}
            </h1>
            <p className="text-gray-600">Fill in your information below</p>
          </div>
        </div>

        <div className="flex space-x-3">
          <button
            onClick={handleSave}
            disabled={saving}
            className="btn-primary flex items-center space-x-2"
          >
            <Save className="w-5 h-5" />
            <span>{saving ? 'Saving...' : 'Save'}</span>
          </button>
        </div>
      </div>

      {/* Personal Information */}
      <div className="card">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Personal Information</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Full Name *
            </label>
            <input
              type="text"
              value={resume.name}
              onChange={(e) => handleChange('name', e.target.value)}
              className="input"
              placeholder="John Doe"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Email *
            </label>
            <input
              type="email"
              value={resume.email}
              onChange={(e) => handleChange('email', e.target.value)}
              className="input"
              placeholder="john@example.com"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Phone
            </label>
            <input
              type="tel"
              value={resume.phone}
              onChange={(e) => handleChange('phone', e.target.value)}
              className="input"
              placeholder="+1-555-0123"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              LinkedIn
            </label>
            <input
              type="text"
              value={resume.linkedin}
              onChange={(e) => handleChange('linkedin', e.target.value)}
              className="input"
              placeholder="linkedin.com/in/johndoe"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              GitHub
            </label>
            <input
              type="text"
              value={resume.github}
              onChange={(e) => handleChange('github', e.target.value)}
              className="input"
              placeholder="github.com/johndoe"
            />
          </div>
        </div>
      </div>

      {/* Summary */}
      <div className="card">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Professional Summary</h2>
        <textarea
          value={resume.summary}
          onChange={(e) => handleChange('summary', e.target.value)}
          className="textarea"
          rows="4"
          placeholder="Brief summary of your professional background and career goals..."
        />
      </div>

      {/* Experience */}
      <div className="card">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Work Experience</h2>
        <textarea
          value={resume.experience}
          onChange={(e) => handleChange('experience', e.target.value)}
          className="textarea"
          rows="8"
          placeholder="Senior Software Engineer - Company Name (2020-Present)&#10;• Developed and maintained web applications...&#10;• Led team of 5 developers...&#10;&#10;Software Engineer - Another Company (2018-2020)&#10;• Built RESTful APIs..."
        />
      </div>

      {/* Education */}
      <div className="card">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Education</h2>
        <textarea
          value={resume.education}
          onChange={(e) => handleChange('education', e.target.value)}
          className="textarea"
          rows="4"
          placeholder="Bachelor of Science in Computer Science&#10;University Name - 2018&#10;GPA: 3.8/4.0"
        />
      </div>

      {/* Skills */}
      <div className="card">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Technical Skills</h2>
        <textarea
          value={resume.skills}
          onChange={(e) => handleChange('skills', e.target.value)}
          className="textarea"
          rows="4"
          placeholder="Java, Spring Boot, React, JavaScript, Python, MySQL, Docker, Kubernetes, AWS, Git"
        />
      </div>

      {/* Projects */}
      <div className="card">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Projects</h2>
        <textarea
          value={resume.projects}
          onChange={(e) => handleChange('projects', e.target.value)}
          className="textarea"
          rows="6"
          placeholder="Project Name - Brief description&#10;• Key features and technologies used...&#10;• Impact and results..."
        />
      </div>

      {/* Certifications */}
      <div className="card">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Certifications</h2>
        <textarea
          value={resume.certifications}
          onChange={(e) => handleChange('certifications', e.target.value)}
          className="textarea"
          rows="3"
          placeholder="AWS Certified Solutions Architect (2023)&#10;Oracle Certified Java Developer (2022)"
        />
      </div>

      {/* Template Selection & Download */}
      {id && id !== 'new' && (
        <div className="card">
          <h2 className="text-xl font-bold text-gray-900 mb-4">Download Resume</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <button
              onClick={() => handleDownload('classic')}
              className="border-2 border-gray-200 rounded-lg p-4 hover:border-indigo-500 hover:shadow-md transition"
            >
              <Download className="w-8 h-8 text-indigo-600 mx-auto mb-2" />
              <h3 className="font-semibold text-gray-900">Classic</h3>
              <p className="text-sm text-gray-500">Traditional black & white</p>
            </button>

            <button
              onClick={() => handleDownload('modern')}
              className="border-2 border-gray-200 rounded-lg p-4 hover:border-indigo-500 hover:shadow-md transition"
            >
              <Download className="w-8 h-8 text-purple-600 mx-auto mb-2" />
              <h3 className="font-semibold text-gray-900">Modern</h3>
              <p className="text-sm text-gray-500">Colorful with accents</p>
            </button>

            <button
              onClick={() => handleDownload('professional')}
              className="border-2 border-gray-200 rounded-lg p-4 hover:border-indigo-500 hover:shadow-md transition"
            >
              <Download className="w-8 h-8 text-green-600 mx-auto mb-2" />
              <h3 className="font-semibold text-gray-900">Professional</h3>
              <p className="text-sm text-gray-500">Clean corporate style</p>
            </button>
          </div>
        </div>
      )}

      {/* Save Button (Bottom) */}
      <div className="flex justify-end">
        <button
          onClick={handleSave}
          disabled={saving}
          className="btn-primary px-8 py-3 text-lg"
        >
          {saving ? 'Saving...' : 'Save Resume'}
        </button>
      </div>
    </div>
  );
}

export default ResumeBuilder;
