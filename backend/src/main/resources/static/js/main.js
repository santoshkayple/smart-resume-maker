// Toast Notification System
function showToast(message, type = 'success', duration = 3000) {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <div class="flex items-center">
            <div class="flex-1">
                <p class="font-medium">${message}</p>
            </div>
            <button onclick="this.parentElement.parentElement.remove()" class="ml-4">
                <i data-lucide="x" class="w-4 h-4"></i>
            </button>
        </div>
    `;
    
    document.body.appendChild(toast);
    lucide.createIcons();
    
    setTimeout(() => {
        toast.style.animation = 'slideIn 0.3s ease-out reverse';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

// File Upload Handler
function setupFileUpload(uploadAreaId, fileInputId, onFileSelect) {
    const uploadArea = document.getElementById(uploadAreaId);
    const fileInput = document.getElementById(fileInputId);
    
    if (!uploadArea || !fileInput) return;
    
    // Click to upload
    uploadArea.addEventListener('click', () => fileInput.click());
    
    // Drag and drop
    uploadArea.addEventListener('dragover', (e) => {
        e.preventDefault();
        uploadArea.classList.add('dragover');
    });
    
    uploadArea.addEventListener('dragleave', () => {
        uploadArea.classList.remove('dragover');
    });
    
    uploadArea.addEventListener('drop', (e) => {
        e.preventDefault();
        uploadArea.classList.remove('dragover');
        
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            fileInput.files = files;
            if (onFileSelect) onFileSelect(files[0]);
        }
    });
    
    // File input change
    fileInput.addEventListener('change', (e) => {
        if (e.target.files.length > 0) {
            if (onFileSelect) onFileSelect(e.target.files[0]);
        }
    });
}

// Progress Bar
function updateProgress(percentage) {
    const progressFill = document.querySelector('.progress-fill');
    if (progressFill) {
        progressFill.style.width = `${percentage}%`;
    }
}

// Confirm Dialog
function confirmAction(message, onConfirm) {
    if (confirm(message)) {
        onConfirm();
    }
}

// Delete Resume
async function deleteResume(resumeId) {
    confirmAction('Are you sure you want to delete this resume?', async () => {
        try {
            const response = await fetch(`/api/resumes/${resumeId}`, {
                method: 'DELETE'
            });
            
            if (response.ok) {
                showToast('Resume deleted successfully', 'success');
                setTimeout(() => window.location.reload(), 1000);
            } else {
                showToast('Failed to delete resume', 'error');
            }
        } catch (error) {
            console.error('Error deleting resume:', error);
            showToast('An error occurred', 'error');
        }
    });
}

// Delete Job Description
async function deleteJobDescription(jdId) {
    confirmAction('Are you sure you want to delete this job description?', async () => {
        try {
            const response = await fetch(`/api/job-descriptions/${jdId}`, {
                method: 'DELETE'
            });
            
            if (response.ok) {
                showToast('Job description deleted successfully', 'success');
                setTimeout(() => window.location.reload(), 1000);
            } else {
                showToast('Failed to delete job description', 'error');
            }
        } catch (error) {
            console.error('Error deleting job description:', error);
            showToast('An error occurred', 'error');
        }
    });
}

// Format Date
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
}

// Truncate Text
function truncateText(text, maxLength) {
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
}

// Copy to Clipboard
function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(() => {
        showToast('Copied to clipboard', 'success', 2000);
    }).catch(err => {
        console.error('Failed to copy:', err);
        showToast('Failed to copy', 'error');
    });
}

// Debounce function for search/filter
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Initialize tooltips (if needed)
function initTooltips() {
    const tooltips = document.querySelectorAll('[data-tooltip]');
    tooltips.forEach(element => {
        element.addEventListener('mouseenter', function() {
            const tooltipText = this.getAttribute('data-tooltip');
            const tooltip = document.createElement('div');
            tooltip.className = 'absolute bg-gray-900 text-white text-xs rounded py-1 px-2 z-50';
            tooltip.textContent = tooltipText;
            tooltip.style.bottom = '100%';
            tooltip.style.left = '50%';
            tooltip.style.transform = 'translateX(-50%)';
            tooltip.style.marginBottom = '5px';
            this.style.position = 'relative';
            this.appendChild(tooltip);
            
            this.addEventListener('mouseleave', function() {
                tooltip.remove();
            }, { once: true });
        });
    });
}

// Form validation helper
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return false;
    
    const inputs = form.querySelectorAll('input[required], textarea[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!input.value.trim()) {
            isValid = false;
            input.classList.add('border-red-500');
            
            // Remove error class on input
            input.addEventListener('input', function() {
                this.classList.remove('border-red-500');
            }, { once: true });
        }
    });
    
    if (!isValid) {
        showToast('Please fill in all required fields', 'error');
    }
    
    return isValid;
}

// Loading overlay
function showLoading() {
    const overlay = document.createElement('div');
    overlay.id = 'loadingOverlay';
    overlay.className = 'fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50';
    overlay.innerHTML = `
        <div class="bg-white rounded-lg p-8 flex flex-col items-center">
            <div class="spinner"></div>
            <p class="mt-4 text-gray-700 font-medium">Loading...</p>
        </div>
    `;
    document.body.appendChild(overlay);
}

function hideLoading() {
    const overlay = document.getElementById('loadingOverlay');
    if (overlay) {
        overlay.remove();
    }
}

// API helper with error handling
async function apiCall(url, options = {}) {
    try {
        showLoading();
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        hideLoading();
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        hideLoading();
        console.error('API call failed:', error);
        showToast('An error occurred. Please try again.', 'error');
        throw error;
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    // Initialize Lucide icons
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }
    
    // Initialize tooltips
    initTooltips();
    
    // Add fade-in animation to main content
    const main = document.querySelector('main');
    if (main) {
        main.classList.add('fade-in');
    }
    
    console.log('Smart Resume Builder initialized');
});

// Export functions for use in templates
window.smartResume = {
    showToast,
    setupFileUpload,
    updateProgress,
    confirmAction,
    deleteResume,
    deleteJobDescription,
    formatDate,
    truncateText,
    copyToClipboard,
    debounce,
    validateForm,
    showLoading,
    hideLoading,
    apiCall
};
