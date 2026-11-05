package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.ApplicationRecord
import com.example.data.model.ApplicationStatus
import com.example.data.model.ExperienceLevel
import com.example.data.model.Job
import com.example.data.model.JobCategory
import com.example.data.model.UserProfile
import com.example.data.model.WorkType
import com.example.data.repository.JobRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    EXPLORE,
    SAVED,
    APPLICATIONS,
    POST_JOB,
    PROFILE,
    DETAIL
}

data class FilterState(
    val query: String = "",
    val category: JobCategory = JobCategory.ALL,
    val workType: WorkType = WorkType.ALL,
    val experienceLevel: ExperienceLevel = ExperienceLevel.ALL,
    val minSalary: Int = 0
)

class JobBoardViewModel(private val repository: JobRepository) : ViewModel() {

    private val _currentScreen = MutableStateFlow(Screen.EXPLORE)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedJob = MutableStateFlow<Job?>(null)
    val selectedJob: StateFlow<Job?> = _selectedJob.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _jobToApply = MutableStateFlow<Job?>(null)
    val jobToApply: StateFlow<Job?> = _jobToApply.asStateFlow()

    private val _selectedAppStatusFilter = MutableStateFlow<ApplicationStatus?>(null)
    val selectedAppStatusFilter: StateFlow<ApplicationStatus?> = _selectedAppStatusFilter.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.ensureInitialized()
        }
    }

    val userProfile: StateFlow<UserProfile> = repository.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val allJobs: StateFlow<List<Job>> = repository.jobsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val applications: StateFlow<List<ApplicationRecord>> = repository.applicationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredJobs: StateFlow<List<Job>> = combine(
        allJobs,
        _filterState
    ) { jobs, filter ->
        jobs.filter { job ->
            val matchesQuery = if (filter.query.isBlank()) true else {
                val q = filter.query.trim().lowercase()
                job.title.lowercase().contains(q) ||
                        job.company.lowercase().contains(q) ||
                        job.location.lowercase().contains(q) ||
                        job.skills.any { it.lowercase().contains(q) } ||
                        job.category.lowercase().contains(q)
            }

            val matchesCategory = when (filter.category) {
                JobCategory.ALL -> true
                else -> job.category.equals(filter.category.displayName, ignoreCase = true) ||
                        (filter.category == JobCategory.ENGINEERING && (job.category.contains("Engineering", ignoreCase = true) || job.category.contains("Backend", ignoreCase = true) || job.category.contains("Frontend", ignoreCase = true) || job.category.contains("Mobile", ignoreCase = true))) ||
                        (filter.category == JobCategory.MOBILE && job.category.contains("Mobile", ignoreCase = true)) ||
                        (filter.category == JobCategory.FRONTEND && job.category.contains("Frontend", ignoreCase = true)) ||
                        (filter.category == JobCategory.BACKEND && job.category.contains("Backend", ignoreCase = true)) ||
                        (filter.category == JobCategory.AI_ML && (job.category.contains("AI", ignoreCase = true) || job.category.contains("Machine Learning", ignoreCase = true))) ||
                        (filter.category == JobCategory.DESIGN && (job.category.contains("Design", ignoreCase = true) || job.category.contains("UI", ignoreCase = true))) ||
                        (filter.category == JobCategory.PRODUCT && job.category.contains("Product", ignoreCase = true)) ||
                        (filter.category == JobCategory.DATA && job.category.contains("Data", ignoreCase = true)) ||
                        (filter.category == JobCategory.DEVOPS && (job.category.contains("DevOps", ignoreCase = true) || job.category.contains("Cloud", ignoreCase = true)))
            }

            val matchesWorkType = when (filter.workType) {
                WorkType.ALL -> true
                WorkType.REMOTE -> job.workType.equals("Remote", ignoreCase = true)
                WorkType.HYBRID -> job.workType.equals("Hybrid", ignoreCase = true)
                WorkType.ONSITE -> job.workType.equals("On-site", ignoreCase = true)
            }

            val matchesExperience = when (filter.experienceLevel) {
                ExperienceLevel.ALL -> true
                else -> job.experienceLevel.contains(filter.experienceLevel.displayName, ignoreCase = true) ||
                        job.experienceLevel.startsWith(filter.experienceLevel.name, ignoreCase = true)
            }

            val matchesSalary = if (filter.minSalary <= 0) true else {
                job.salaryMax >= filter.minSalary
            }

            matchesQuery && matchesCategory && matchesWorkType && matchesExperience && matchesSalary
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredJobs: StateFlow<List<Job>> = combine(allJobs) { jobsList ->
        jobsList[0].filter { it.isFeatured }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedJobs: StateFlow<List<Job>> = combine(allJobs) { jobsList ->
        jobsList[0].filter { it.isSaved }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun openJobDetail(job: Job) {
        _selectedJob.value = job
        _currentScreen.value = Screen.DETAIL
    }

    fun closeJobDetail() {
        _selectedJob.value = null
        _currentScreen.value = Screen.EXPLORE
    }

    fun setQuery(query: String) {
        _filterState.value = _filterState.value.copy(query = query)
    }

    fun setCategory(category: JobCategory) {
        _filterState.value = _filterState.value.copy(category = category)
    }

    fun setWorkType(workType: WorkType) {
        _filterState.value = _filterState.value.copy(workType = workType)
    }

    fun setExperience(experience: ExperienceLevel) {
        _filterState.value = _filterState.value.copy(experienceLevel = experience)
    }

    fun setMinSalary(min: Int) {
        _filterState.value = _filterState.value.copy(minSalary = min)
    }

    fun resetFilters() {
        _filterState.value = FilterState()
    }

    fun toggleSave(job: Job) {
        viewModelScope.launch {
            repository.toggleSaveJob(job.id, job.isSaved)
            val message = if (job.isSaved) "Job removed from bookmarks" else "Job saved to bookmarks"
            _snackbarEvent.emit(message)
            // If current selected job is being viewed, update it
            if (_selectedJob.value?.id == job.id) {
                _selectedJob.value = _selectedJob.value?.copy(isSaved = !job.isSaved)
            }
        }
    }

    fun openApplyModal(job: Job) {
        _jobToApply.value = job
    }

    fun closeApplyModal() {
        _jobToApply.value = null
    }

    fun submitApplication(job: Job, applicantName: String, applicantEmail: String, notes: String) {
        viewModelScope.launch {
            repository.applyForJob(job, applicantName, applicantEmail, notes)
            _jobToApply.value = null
            _snackbarEvent.emit("Application submitted for ${job.title} at ${job.company}!")
            if (_selectedJob.value?.id == job.id) {
                _selectedJob.value = _selectedJob.value?.copy(isApplied = true, applicantsCount = job.applicantsCount + 1)
            }
        }
    }

    fun updateApplicationStatus(id: String, status: ApplicationStatus) {
        viewModelScope.launch {
            repository.updateApplicationStatus(id, status)
            _snackbarEvent.emit("Status updated to ${status.displayName}")
        }
    }

    fun updateApplicationNotes(id: String, notes: String, interviewDate: String) {
        viewModelScope.launch {
            repository.updateApplicationNotes(id, notes, interviewDate)
            _snackbarEvent.emit("Interview notes saved")
        }
    }

    fun deleteApplication(id: String) {
        viewModelScope.launch {
            repository.deleteApplication(id)
            _snackbarEvent.emit("Application removed from tracker")
        }
    }

    fun setApplicationStatusFilter(status: ApplicationStatus?) {
        _selectedAppStatusFilter.value = status
    }

    fun postNewJob(job: Job) {
        viewModelScope.launch {
            repository.postNewJob(job)
            _snackbarEvent.emit("Job posted successfully! It is now live on the board.")
            _currentScreen.value = Screen.EXPLORE
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
            _snackbarEvent.emit("Profile updated successfully")
        }
    }

    class Factory(private val repository: JobRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobBoardViewModel::class.java)) {
                return JobBoardViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
