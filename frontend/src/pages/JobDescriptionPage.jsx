import { useState, useEffect } from 'react';
import { Briefcase, Plus, Trash2, Loader } from 'lucide-react';
import { jdAPI } from '../services/api';

export default function JobDescriptionPage() {
  const [jobDescriptions, setJobDescriptions] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [newJD, setNewJD] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    fetchJobDescriptions();
  }, []);

  const fetchJobDescriptions = async () => {
    try {
      const response = await jdAPI.getAll();
      setJobDescriptions(response.data);
    } catch (err) {
      console.error('Failed to load job descriptions');
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async () => {
    if (!newJD.trim()) {
      alert('Please enter a job description');
      return;
    }

    setSaving(true);
    try {
      await jdAPI.create(newJD);
      setNewJD('');
      setShowForm(false);
      fetchJobDescriptions();
    } catch (err) {
      alert('Failed to create job description');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Are you sure you want to delete this job description?')) return;
    
    try {
      await jdAPI.delete(id);
      setJobDescriptions(jobDescriptions.filter(jd => jd.id !== id));
    } catch (err) {
      alert('Failed to delete job description');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader className="animate-spin h-8 w-8 text-indigo-600" />
      </div>
    );
  }

  return (
    <div className="px-4 py-8">
      <div className="max-w-5xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Job Descriptions</h1>
          <button
            onClick={() => setShowForm(!showForm)}
            className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 transition-colors flex items-center"
          >
            <Plus className="h-4 w-4 mr-2" />
            Add New
          </button>
        </div>

        {/* Add New JD Form */}
        {showForm && (
          <div className="bg-white rounded-lg shadow-md p-6 mb-6">
            <h2 className="text-xl font-semibold mb-4">Add New Job Description</h2>
            <textarea
              value={newJD}
              onChange={(e) => setNewJD(e.target.value)}
              rows={12}
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 mb-4"
              placeholder="Paste the complete job description here...&#10;&#10;Include:&#10;- Job title&#10;- Company name&#10;- Required skills&#10;- Responsibilities&#10;- Qualifications"
            />
            <div className="flex space-x-4">
              <button
                onClick={handleCreate}
                disabled={saving}
                className="px-6 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 disabled:bg-gray-400 transition-colors flex items-center"
              >
                {saving ? (
                  <>
                    <Loader className="animate-spin h-4 w-4 mr-2" />
                    Creating...
                  </>
                ) : (
                  'Create'
                )}
              </button>
              <button
                onClick={() => {
                  setShowForm(false);
                  setNewJD('');
                }}
                className="px-6 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 transition-colors"
              >
                Cancel
              </button>
            </div>
          </div>
        )}

        {/* JD List */}
        {jobDescriptions.length === 0 ? (
          <div className="bg-white rounded-lg shadow-md p-12 text-center">
            <Briefcase className="h-16 w-16 mx-auto mb-4 text-gray-400" />
            <h2 className="text-xl font-semibold mb-2">No job descriptions yet</h2>
            <p className="text-gray-600 mb-6">
              Add job descriptions to match with your resumes
            </p>
            <button
              onClick={() => setShowForm(true)}
              className="inline-block px-6 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 transition-colors"
            >
              Add Job Description
            </button>
          </div>
        ) : (
          <div className="space-y-4">
            {jobDescriptions.map((jd) => (
              <div key={jd.id} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
                <div className="flex justify-between items-start mb-4">
                  <div className="flex-1">
                    <h3 className="text-xl font-semibold text-gray-900 mb-2">
                      {jd.jobTitle || 'Job Position'}
                    </h3>
                    {jd.companyName && (
                      <p className="text-sm text-gray-600 mb-2">{jd.companyName}</p>
                    )}
                    <p className="text-sm text-gray-500">
                      Added: {new Date(jd.createdAt).toLocaleDateString()}
                    </p>
                  </div>
                  <button
                    onClick={() => handleDelete(jd.id)}
                    className="px-3 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 transition-colors"
                  >
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>

                {jd.requiredSkills && (
                  <div className="mb-3">
                    <h4 className="text-sm font-semibold text-gray-700 mb-1">Required Skills:</h4>
                    <p className="text-sm text-gray-600">{jd.requiredSkills}</p>
                  </div>
                )}

                {jd.description && (
                  <div className="mt-4">
                    <details className="cursor-pointer">
                      <summary className="text-sm font-semibold text-indigo-600 hover:text-indigo-700">
                        View Full Description
                      </summary>
                      <p className="mt-2 text-sm text-gray-600 whitespace-pre-wrap">
                        {jd.description.substring(0, 500)}
                        {jd.description.length > 500 && '...'}
                      </p>
                    </details>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
