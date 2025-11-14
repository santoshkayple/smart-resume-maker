import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FileText, Upload, PlusCircle, Trash2, Edit, Download } from 'lucide-react';
import { resumeAPI } from '../services/api';

function Dashboard() {
  const navigate = useNavigate();
  const [resumes, setResumes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchResumes();
  }, []);

  const fetchResumes = async () => {
    try {
      const response = await resumeAPI.getAllResumes();
      setResumes(response.data);
    } catch (error) {
      console.error('Error fetching resumes:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this resume?')) {
      try {
        await resumeAPI.deleteResume(id);
        fetchResumes();
      } catch (error) {
        console.error('Error deleting resume:', error);
        alert('Failed to delete resume');
      }
    }
  };

  const handleDownload = async (id, name, template = 'classic') => {
    try {
      const response = await resumeAPI.downloadPDF(id, template);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `${name.replace(/\s+/g, '_')}_resume.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error('Error downloading resume:', error);
      alert('Failed to download resume');
    }
  };

  return (
    <div className="space-y-8 animate-slide-in">
      {/* Header */}
      <div className="bg-gradient-to-r from-indigo-600 to-purple-600 rounded-2xl p-8 text-white">
        <h1 className="text-4xl font-bold mb-2">Welcome to Smart Resume Builder</h1>
        <p className="text-indigo-100 text-lg">
          Build, optimize, and match your resume with job descriptions using AI
        </p>
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <button
          onClick={() => navigate('/upload')}
          className="card hover:shadow-lg transition-shadow flex items-center justify-center space-x-4 p-8 border-2 border-dashed border-gray-300 hover:border-indigo-500"
        >
          <Upload className="w-8 h-8 text-indigo-600" />
          <div className="text-left">
            <h3 className="text-lg font-semibold text-gray-900">Upload Resume</h3>
            <p className="text-sm text-gray-500">Upload PDF or DOCX file</p>
          </div>
        </button>

        <button
          onClick={() => navigate('/resume/new')}
          className="card hover:shadow-lg transition-shadow flex items-center justify-center space-x-4 p-8 border-2 border-dashed border-gray-300 hover:border-indigo-500"
        >
          <PlusCircle className="w-8 h-8 text-indigo-600" />
          <div className="text-left">
            <h3 className="text-lg font-semibold text-gray-900">Create New</h3>
            <p className="text-sm text-gray-500">Build from scratch</p>
          </div>
        </button>

        <button
          onClick={() => navigate('/job-descriptions')}
          className="card hover:shadow-lg transition-shadow flex items-center justify-center space-x-4 p-8 border-2 border-dashed border-gray-300 hover:border-indigo-500"
        >
          <FileText className="w-8 h-8 text-indigo-600" />
          <div className="text-left">
            <h3 className="text-lg font-semibold text-gray-900">Job Descriptions</h3>
            <p className="text-sm text-gray-500">Manage JDs</p>
          </div>
        </button>
      </div>

      {/* Resumes List */}
      <div className="card">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-gray-900">Your Resumes</h2>
          <span className="text-sm text-gray-500">{resumes.length} resumes</span>
        </div>

        {loading ? (
          <div className="text-center py-12">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-4 border-indigo-600 border-t-transparent"></div>
            <p className="mt-4 text-gray-500">Loading resumes...</p>
          </div>
        ) : resumes.length === 0 ? (
          <div className="text-center py-12">
            <FileText className="w-16 h-16 text-gray-400 mx-auto mb-4" />
            <p className="text-gray-500 text-lg">No resumes yet</p>
            <p className="text-gray-400 text-sm mt-2">Upload or create your first resume to get started</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {resumes.map((resume) => (
              <div
                key={resume.id}
                className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
              >
                <div className="flex items-start justify-between mb-3">
                  <div className="flex-1">
                    <h3 className="font-semibold text-gray-900 text-lg">{resume.name || 'Untitled'}</h3>
                    <p className="text-sm text-gray-500">{resume.email}</p>
                  </div>
                  <FileText className="w-5 h-5 text-indigo-600" />
                </div>

                <div className="mb-4 text-sm text-gray-600">
                  <p className="truncate">
                    {resume.summary?.substring(0, 100)}
                    {resume.summary?.length > 100 ? '...' : ''}
                  </p>
                </div>

                <div className="text-xs text-gray-400 mb-4">
                  Updated: {new Date(resume.updatedAt).toLocaleDateString()}
                </div>

                <div className="flex space-x-2">
                  <button
                    onClick={() => navigate(`/resume/${resume.id}`)}
                    className="flex-1 btn-primary text-sm py-2"
                  >
                    <Edit className="w-4 h-4 inline mr-1" />
                    Edit
                  </button>
                  <button
                    onClick={() => handleDownload(resume.id, resume.name)}
                    className="flex-1 btn-secondary text-sm py-2"
                  >
                    <Download className="w-4 h-4 inline mr-1" />
                    PDF
                  </button>
                  <button
                    onClick={() => handleDelete(resume.id)}
                    className="btn-danger text-sm py-2 px-3"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Features Section */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="card text-center">
          <div className="w-12 h-12 bg-indigo-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <Upload className="w-6 h-6 text-indigo-600" />
          </div>
          <h3 className="font-semibold text-gray-900 mb-2">Upload & Parse</h3>
          <p className="text-sm text-gray-600">
            Automatically extract information from your existing resume
          </p>
        </div>

        <div className="card text-center">
          <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <FileText className="w-6 h-6 text-purple-600" />
          </div>
          <h3 className="font-semibold text-gray-900 mb-2">AI Matching</h3>
          <p className="text-sm text-gray-600">
            Match your resume with job descriptions and get scores
          </p>
        </div>

        <div className="card text-center">
          <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <Download className="w-6 h-6 text-green-600" />
          </div>
          <h3 className="font-semibold text-gray-900 mb-2">Export PDF</h3>
          <p className="text-sm text-gray-600">
            Download professional resumes in multiple templates
          </p>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
