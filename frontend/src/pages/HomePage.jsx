import { Link } from 'react-router-dom';
import { Upload, FileText, Target, Download, Zap } from 'lucide-react';

export default function HomePage() {
  return (
    <div className="px-4 py-8">
      {/* Hero Section */}
      <div className="text-center mb-16">
        <h1 className="text-5xl font-bold text-gray-900 mb-4">
          Build Your Perfect Resume
        </h1>
        <p className="text-xl text-gray-600 mb-8 max-w-3xl mx-auto">
          AI-powered resume builder that optimizes your resume based on job descriptions,
          provides ATS compatibility scores, and generates professional PDFs.
        </p>
        <div className="flex justify-center space-x-4">
          <Link
            to="/upload"
            className="px-8 py-3 bg-indigo-600 text-white rounded-lg font-semibold hover:bg-indigo-700 transition-colors"
          >
            Get Started
          </Link>
          <Link
            to="/resumes"
            className="px-8 py-3 bg-white text-indigo-600 border-2 border-indigo-600 rounded-lg font-semibold hover:bg-indigo-50 transition-colors"
          >
            View My Resumes
          </Link>
        </div>
      </div>

      {/* Features */}
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8 mb-16">
        <FeatureCard
          icon={<Upload className="h-8 w-8 text-indigo-600" />}
          title="Upload & Parse"
          description="Upload your existing resume in PDF or DOCX format. Our AI automatically extracts all information."
        />
        <FeatureCard
          icon={<Target className="h-8 w-8 text-indigo-600" />}
          title="Smart Matching"
          description="Match your resume against job descriptions and get detailed compatibility scores."
        />
        <FeatureCard
          icon={<Zap className="h-8 w-8 text-indigo-600" />}
          title="AI Optimization"
          description="Automatically improve your resume content based on job requirements and industry best practices."
        />
        <FeatureCard
          icon={<FileText className="h-8 w-8 text-indigo-600" />}
          title="ATS Score"
          description="Check how well your resume performs with Applicant Tracking Systems."
        />
        <FeatureCard
          icon={<Download className="h-8 w-8 text-indigo-600" />}
          title="Multiple Templates"
          description="Export your resume in Classic, Modern, or Professional PDF templates."
        />
        <FeatureCard
          icon={<FileText className="h-8 w-8 text-indigo-600" />}
          title="Real-time Editing"
          description="Edit resume sections with live preview and instant feedback."
        />
      </div>

      {/* How It Works */}
      <div className="bg-white rounded-lg shadow-md p-8 mb-16">
        <h2 className="text-3xl font-bold text-center mb-8">How It Works</h2>
        <div className="grid md:grid-cols-4 gap-6">
          <Step number="1" title="Upload Resume" description="Upload your existing resume or create a new one" />
          <Step number="2" title="Add Job Description" description="Paste the job description you're applying for" />
          <Step number="3" title="Get Insights" description="See match scores and optimization suggestions" />
          <Step number="4" title="Download" description="Export your optimized resume as PDF" />
        </div>
      </div>

      {/* CTA */}
      <div className="bg-indigo-600 rounded-lg shadow-xl p-8 text-center text-white">
        <h2 className="text-3xl font-bold mb-4">Ready to Build Your Perfect Resume?</h2>
        <p className="text-lg mb-6">
          Join thousands of job seekers who have landed their dream jobs with our AI-powered resume builder.
        </p>
        <Link
          to="/upload"
          className="inline-block px-8 py-3 bg-white text-indigo-600 rounded-lg font-semibold hover:bg-gray-100 transition-colors"
        >
          Start Building Now
        </Link>
      </div>
    </div>
  );
}

function FeatureCard({ icon, title, description }) {
  return (
    <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
      <div className="mb-4">{icon}</div>
      <h3 className="text-xl font-semibold mb-2">{title}</h3>
      <p className="text-gray-600">{description}</p>
    </div>
  );
}

function Step({ number, title, description }) {
  return (
    <div className="text-center">
      <div className="w-12 h-12 bg-indigo-600 text-white rounded-full flex items-center justify-center text-xl font-bold mx-auto mb-4">
        {number}
      </div>
      <h3 className="font-semibold mb-2">{title}</h3>
      <p className="text-sm text-gray-600">{description}</p>
    </div>
  );
}
