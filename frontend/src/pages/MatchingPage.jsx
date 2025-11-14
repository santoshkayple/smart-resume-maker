import React, { useState, useEffect } from 'react';
import { BarChart3, TrendingUp, AlertCircle, CheckCircle, Sparkles } from 'lucide-react';
import { resumeAPI, jobDescriptionAPI } from '../services/api';

function MatchingPage() {
  const [resumes, setResumes] = useState([]);
  const [jds, setJds] = useState([]);
  const [selectedResume, setSelectedResume] = useState('');
  const [selectedJD, setSelectedJD] = useState('');
  const [loading, setLoading] = useState(true);
  const [matching, setMatching] = useState(false);
  const [optimizing, setOptimizing] = useState(false);
  const [matchResult, setMatchResult] = useState(null);
  const [suggestions, setSuggestions] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [resumesRes, jdsRes] = await Promise.all([
        resumeAPI.getAllResumes(),
        jobDescriptionAPI.getAllJobDescriptions()
      ]);
      setResumes(resumesRes.data);
      setJds(jdsRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleMatch = async () => {
    if (!selectedResume || !selectedJD) {
      alert('Please select both a resume and a job description');
      return;
    }

    setMatching(true);
    setMatchResult(null);
    setSuggestions(null);

    try {
      const [matchRes, suggestionsRes] = await Promise.all([
        resumeAPI.matchWithJD(selectedResume, selectedJD),
        resumeAPI.getOptimizationSuggestions(selectedResume, selectedJD)
      ]);
      
      setMatchResult(matchRes.data);
      setSuggestions(suggestionsRes.data);
    } catch (error) {
      console.error('Error matching:', error);
      alert('Failed to match resume with job description');
    } finally {
      setMatching(false);
    }
  };

  const handleOptimize = async () => {
    if (!selectedResume || !selectedJD) {
      alert('Please select both a resume and a job description');
      return;
    }

    if (!window.confirm('This will optimize your resume based on the job description. Continue?')) {
      return;
    }

    setOptimizing(true);

    try {
      await resumeAPI.optimizeResume(selectedResume, selectedJD);
      alert('Resume optimized successfully! You can now view the updated resume.');
      // Re-fetch match result
      handleMatch();
    } catch (error) {
      console.error('Error optimizing:', error);
      alert('Failed to optimize resume');
    } finally {
      setOptimizing(false);
    }
  };

  const getScoreColor = (score) => {
    if (score >= 80) return 'text-green-600 bg-green-100';
    if (score >= 60) return 'text-yellow-600 bg-yellow-100';
    return 'text-red-600 bg-red-100';
  };

  const getScoreRing = (score) => {
    if (score >= 80) return 'text-green-600';
    if (score >= 60) return 'text-yellow-600';
    return 'text-red-600';
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="text-center">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-4 border-indigo-600 border-t-transparent"></div>
          <p className="mt-4 text-gray-600">Loading...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto space-y-8 animate-slide-in">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Resume Matching & Optimization</h1>
        <p className="text-gray-600">Match your resume with job descriptions and get AI-powered optimization suggestions</p>
      </div>

      {/* Selection Card */}
      <div className="card">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Select Resume and Job Description</h2>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          {/* Resume Selection */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Select Resume
            </label>
            <select
              value={selectedResume}
              onChange={(e) => setSelectedResume(e.target.value)}
              className="input"
            >
              <option value="">-- Choose a resume --</option>
              {resumes.map((resume) => (
                <option key={resume.id} value={resume.id}>
                  {resume.name || 'Untitled'} ({resume.email})
                </option>
              ))}
            </select>
            {resumes.length === 0 && (
              <p className="text-sm text-gray-500 mt-2">No resumes available. Please upload or create a resume first.</p>
            )}
          </div>

          {/* JD Selection */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Select Job Description
            </label>
            <select
              value={selectedJD}
              onChange={(e) => setSelectedJD(e.target.value)}
              className="input"
            >
              <option value="">-- Choose a job description --</option>
              {jds.map((jd) => (
                <option key={jd.id} value={jd.id}>
                  {jd.jobTitle || 'Job Position'} {jd.companyName && `- ${jd.companyName}`}
                </option>
              ))}
            </select>
            {jds.length === 0 && (
              <p className="text-sm text-gray-500 mt-2">No job descriptions available. Please add a job description first.</p>
            )}
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex space-x-4">
          <button
            onClick={handleMatch}
            disabled={!selectedResume || !selectedJD || matching}
            className="btn-primary flex items-center space-x-2 disabled:opacity-50"
          >
            <BarChart3 className="w-5 h-5" />
            <span>{matching ? 'Matching...' : 'Calculate Match Score'}</span>
          </button>

          <button
            onClick={handleOptimize}
            disabled={!selectedResume || !selectedJD || optimizing}
            className="btn-secondary flex items-center space-x-2 disabled:opacity-50"
          >
            <Sparkles className="w-5 h-5" />
            <span>{optimizing ? 'Optimizing...' : 'Optimize Resume'}</span>
          </button>
        </div>
      </div>

      {/* Match Results */}
      {matchResult && (
        <>
          {/* Overall Score */}
          <div className="card bg-gradient-to-r from-indigo-600 to-purple-600 text-white">
            <div className="flex items-center justify-between">
              <div>
                <h2 className="text-3xl font-bold mb-2">Overall Match Score</h2>
                <p className="text-indigo-100">How well your resume matches the job description</p>
              </div>
              <div className="text-6xl font-bold">
                {matchResult.overallScore.toFixed(1)}%
              </div>
            </div>
          </div>

          {/* Detailed Scores */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="card">
              <div className="flex items-center justify-between mb-3">
                <h3 className="font-semibold text-gray-900">Skills Match</h3>
                <TrendingUp className="w-5 h-5 text-indigo-600" />
              </div>
              <div className={`text-3xl font-bold ${getScoreRing(matchResult.skillsMatchScore)}`}>
                {matchResult.skillsMatchScore.toFixed(1)}%
              </div>
              <div className="mt-2">
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className={`h-2 rounded-full ${getScoreRing(matchResult.skillsMatchScore).replace('text', 'bg')}`}
                    style={{ width: `${matchResult.skillsMatchScore}%` }}
                  ></div>
                </div>
              </div>
            </div>

            <div className="card">
              <div className="flex items-center justify-between mb-3">
                <h3 className="font-semibold text-gray-900">Experience Match</h3>
                <BarChart3 className="w-5 h-5 text-purple-600" />
              </div>
              <div className={`text-3xl font-bold ${getScoreRing(matchResult.experienceMatchScore)}`}>
                {matchResult.experienceMatchScore.toFixed(1)}%
              </div>
              <div className="mt-2">
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className={`h-2 rounded-full ${getScoreRing(matchResult.experienceMatchScore).replace('text', 'bg')}`}
                    style={{ width: `${matchResult.experienceMatchScore}%` }}
                  ></div>
                </div>
              </div>
            </div>

            <div className="card">
              <div className="flex items-center justify-between mb-3">
                <h3 className="font-semibold text-gray-900">ATS Score</h3>
                <CheckCircle className="w-5 h-5 text-green-600" />
              </div>
              <div className={`text-3xl font-bold ${getScoreRing(matchResult.atsScore)}`}>
                {matchResult.atsScore.toFixed(1)}%
              </div>
              <div className="mt-2">
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className={`h-2 rounded-full ${getScoreRing(matchResult.atsScore).replace('text', 'bg')}`}
                    style={{ width: `${matchResult.atsScore}%` }}
                  ></div>
                </div>
              </div>
            </div>
          </div>

          {/* Matched Keywords */}
          {matchResult.matchedKeywords && matchResult.matchedKeywords.length > 0 && (
            <div className="card bg-green-50 border border-green-200">
              <h3 className="font-semibold text-green-900 mb-3 flex items-center">
                <CheckCircle className="w-5 h-5 mr-2" />
                Matched Keywords ({matchResult.matchedKeywords.length})
              </h3>
              <div className="flex flex-wrap gap-2">
                {matchResult.matchedKeywords.map((keyword, idx) => (
                  <span
                    key={idx}
                    className="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm font-medium"
                  >
                    {keyword}
                  </span>
                ))}
              </div>
            </div>
          )}

          {/* Missing Keywords */}
          {matchResult.missingKeywords && matchResult.missingKeywords.length > 0 && (
            <div className="card bg-red-50 border border-red-200">
              <h3 className="font-semibold text-red-900 mb-3 flex items-center">
                <AlertCircle className="w-5 h-5 mr-2" />
                Missing Keywords ({matchResult.missingKeywords.length})
              </h3>
              <div className="flex flex-wrap gap-2 mb-4">
                {matchResult.missingKeywords.map((keyword, idx) => (
                  <span
                    key={idx}
                    className="px-3 py-1 bg-red-100 text-red-800 rounded-full text-sm font-medium"
                  >
                    {keyword}
                  </span>
                ))}
              </div>
              <p className="text-sm text-red-700">
                Consider adding these keywords to your resume if you have experience with them.
              </p>
            </div>
          )}

          {/* Suggestions */}
          {matchResult.suggestions && matchResult.suggestions.length > 0 && (
            <div className="card">
              <h3 className="font-semibold text-gray-900 mb-4 text-xl">Improvement Suggestions</h3>
              <ul className="space-y-3">
                {matchResult.suggestions.map((suggestion, idx) => (
                  <li key={idx} className="flex items-start space-x-3">
                    <span className="flex-shrink-0 w-6 h-6 bg-indigo-100 text-indigo-600 rounded-full flex items-center justify-center text-sm font-medium">
                      {idx + 1}
                    </span>
                    <span className="text-gray-700">{suggestion}</span>
                  </li>
                ))}
              </ul>
            </div>
          )}

          {/* Strengths & Weaknesses */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="card bg-green-50 border border-green-200">
              <h3 className="font-semibold text-green-900 mb-3">💪 Strengths</h3>
              <p className="text-green-800">{matchResult.strength}</p>
            </div>

            <div className="card bg-yellow-50 border border-yellow-200">
              <h3 className="font-semibold text-yellow-900 mb-3">⚠️ Areas to Improve</h3>
              <p className="text-yellow-800">{matchResult.weakness}</p>
            </div>
          </div>
        </>
      )}

      {/* Optimization Suggestions */}
      {suggestions && (
        <div className="card bg-indigo-50 border border-indigo-200">
          <h2 className="text-xl font-bold text-indigo-900 mb-4 flex items-center">
            <Sparkles className="w-6 h-6 mr-2" />
            AI Optimization Preview
          </h2>
          
          <div className="space-y-4">
            {suggestions.optimizedSummary && (
              <div>
                <h3 className="font-semibold text-indigo-900 mb-2">Optimized Summary:</h3>
                <p className="text-indigo-800 bg-white p-3 rounded-lg">{suggestions.optimizedSummary}</p>
              </div>
            )}

            {suggestions.optimizedSkills && (
              <div>
                <h3 className="font-semibold text-indigo-900 mb-2">Optimized Skills:</h3>
                <p className="text-indigo-800 bg-white p-3 rounded-lg">{suggestions.optimizedSkills}</p>
              </div>
            )}

            {suggestions.addedKeywords > 0 && (
              <div className="flex items-center space-x-2 text-indigo-700">
                <CheckCircle className="w-5 h-5" />
                <span className="font-medium">{suggestions.addedKeywords} keywords will be added</span>
              </div>
            )}
          </div>

          <button
            onClick={handleOptimize}
            disabled={optimizing}
            className="mt-4 btn-primary w-full"
          >
            {optimizing ? 'Applying Optimization...' : 'Apply Optimization to Resume'}
          </button>
        </div>
      )}
    </div>
  );
}

export default MatchingPage;
