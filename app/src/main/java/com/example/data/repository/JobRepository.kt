package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.ApplicationEntity
import com.example.data.local.JobEntity
import com.example.data.local.PrepopulatedJobs
import com.example.data.local.SavedJobEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.ApplicationRecord
import com.example.data.model.ApplicationStatus
import com.example.data.model.Job
import com.example.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class JobRepository(private val database: AppDatabase) {

    private val jobDao = database.jobDao()
    private val applicationDao = database.applicationDao()
    private val savedJobDao = database.savedJobDao()
    private val userProfileDao = database.userProfileDao()

    suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
        if (jobDao.getJobCount() == 0) {
            jobDao.insertAll(PrepopulatedJobs.initialJobs)
            val initialProfile = UserProfileEntity(
                id = 1,
                name = "Alex Morgan",
                title = "Senior Mobile & Fullstack Engineer",
                email = "alex.morgan@example.com",
                phone = "+1 (555) 234-5678",
                location = "San Francisco, CA (Open to Remote)",
                bio = "Passionate product-minded engineer with 6+ years specializing in Android (Kotlin, Jetpack Compose, Coroutines), modern mobile architecture, and scalable cloud systems.",
                skills = listOf("Kotlin", "Jetpack Compose", "Android SDK", "Coroutines & Flow", "Room", "KMP", "System Design", "CI/CD"),
                portfolioUrl = "https://alexmorgan.dev",
                linkedinUrl = "https://linkedin.com/in/alexmorgan-dev",
                yearsOfExperience = 6,
                expectedSalaryMin = 160000
            )
            userProfileDao.saveUserProfile(initialProfile)
        }
    }

    val jobsFlow: Flow<List<Job>> = combine(
        jobDao.getAllJobs(),
        savedJobDao.getAllSavedJobIds(),
        applicationDao.getAllApplications()
    ) { jobEntities, savedIds, applications ->
        val savedSet = savedIds.toSet()
        val appliedJobIds = applications.map { it.jobId }.toSet()

        jobEntities.map { entity ->
            Job(
                id = entity.id,
                title = entity.title,
                company = entity.company,
                location = entity.location,
                workType = entity.workType,
                employmentType = entity.employmentType,
                salaryMin = entity.salaryMin,
                salaryMax = entity.salaryMax,
                salaryFormatted = entity.salaryFormatted,
                experienceLevel = entity.experienceLevel,
                postedAgo = entity.postedAgo,
                category = entity.category,
                skills = entity.skills,
                description = entity.description,
                responsibilities = entity.responsibilities,
                requirements = entity.requirements,
                benefits = entity.benefits,
                companyWebsite = entity.companyWebsite,
                companySize = entity.companySize,
                logoColorHex = entity.logoColorHex,
                isFeatured = entity.isFeatured,
                isUserCreated = entity.isUserCreated,
                applicantsCount = entity.applicantsCount,
                isSaved = savedSet.contains(entity.id),
                isApplied = appliedJobIds.contains(entity.id)
            )
        }
    }.flowOn(Dispatchers.IO)

    val applicationsFlow: Flow<List<ApplicationRecord>> = applicationDao.getAllApplications().map { list ->
        list.map { entity ->
            ApplicationRecord(
                id = entity.id,
                jobId = entity.jobId,
                jobTitle = entity.jobTitle,
                company = entity.company,
                appliedDate = entity.appliedDate,
                status = try {
                    ApplicationStatus.valueOf(entity.status)
                } catch (e: Exception) {
                    ApplicationStatus.APPLIED
                },
                applicantName = entity.applicantName,
                applicantEmail = entity.applicantEmail,
                notes = entity.notes,
                interviewDate = entity.interviewDate
            )
        }
    }.flowOn(Dispatchers.IO)

    val userProfileFlow: Flow<UserProfile> = userProfileDao.getUserProfile().map { entity ->
        if (entity != null) {
            UserProfile(
                name = entity.name,
                title = entity.title,
                email = entity.email,
                phone = entity.phone,
                location = entity.location,
                bio = entity.bio,
                skills = entity.skills,
                portfolioUrl = entity.portfolioUrl,
                linkedinUrl = entity.linkedinUrl,
                yearsOfExperience = entity.yearsOfExperience,
                expectedSalaryMin = entity.expectedSalaryMin
            )
        } else {
            UserProfile()
        }
    }.flowOn(Dispatchers.IO)

    suspend fun toggleSaveJob(jobId: String, currentSaved: Boolean) = withContext(Dispatchers.IO) {
        if (currentSaved) {
            savedJobDao.unsaveJob(jobId)
        } else {
            savedJobDao.saveJob(SavedJobEntity(jobId = jobId))
        }
    }

    suspend fun applyForJob(
        job: Job,
        applicantName: String,
        applicantEmail: String,
        notes: String
    ): Boolean = withContext(Dispatchers.IO) {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
        val todayStr = dateFormat.format(Date())

        val appEntity = ApplicationEntity(
            id = UUID.randomUUID().toString(),
            jobId = job.id,
            jobTitle = job.title,
            company = job.company,
            appliedDate = todayStr,
            status = ApplicationStatus.APPLIED.name,
            applicantName = applicantName,
            applicantEmail = applicantEmail,
            notes = notes,
            interviewDate = ""
        )
        applicationDao.insertApplication(appEntity)

        // Increment applicants count
        val updatedJob = JobEntity(
            id = job.id,
            title = job.title,
            company = job.company,
            location = job.location,
            workType = job.workType,
            employmentType = job.employmentType,
            salaryMin = job.salaryMin,
            salaryMax = job.salaryMax,
            salaryFormatted = job.salaryFormatted,
            experienceLevel = job.experienceLevel,
            postedAgo = job.postedAgo,
            category = job.category,
            skills = job.skills,
            description = job.description,
            responsibilities = job.responsibilities,
            requirements = job.requirements,
            benefits = job.benefits,
            companyWebsite = job.companyWebsite,
            companySize = job.companySize,
            logoColorHex = job.logoColorHex,
            isFeatured = job.isFeatured,
            isUserCreated = job.isUserCreated,
            applicantsCount = job.applicantsCount + 1
        )
        jobDao.insertJob(updatedJob)
        true
    }

    suspend fun updateApplicationStatus(id: String, status: ApplicationStatus) = withContext(Dispatchers.IO) {
        applicationDao.updateStatus(id, status.name)
    }

    suspend fun updateApplicationNotes(id: String, notes: String, interviewDate: String) = withContext(Dispatchers.IO) {
        applicationDao.updateNotesAndInterview(id, notes, interviewDate)
    }

    suspend fun deleteApplication(id: String) = withContext(Dispatchers.IO) {
        applicationDao.deleteApplication(id)
    }

    suspend fun postNewJob(job: Job) = withContext(Dispatchers.IO) {
        val entity = JobEntity(
            id = if (job.id.isNotBlank()) job.id else "job_user_${System.currentTimeMillis()}",
            title = job.title,
            company = job.company,
            location = job.location,
            workType = job.workType,
            employmentType = job.employmentType,
            salaryMin = job.salaryMin,
            salaryMax = job.salaryMax,
            salaryFormatted = job.salaryFormatted,
            experienceLevel = job.experienceLevel,
            postedAgo = "Just now",
            category = job.category,
            skills = job.skills,
            description = job.description,
            responsibilities = job.responsibilities,
            requirements = job.requirements,
            benefits = job.benefits,
            companyWebsite = job.companyWebsite,
            companySize = job.companySize,
            logoColorHex = job.logoColorHex,
            isFeatured = job.isFeatured,
            isUserCreated = true,
            applicantsCount = 0
        )
        jobDao.insertJob(entity)
    }

    suspend fun saveUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        val entity = UserProfileEntity(
            id = 1,
            name = profile.name,
            title = profile.title,
            email = profile.email,
            phone = profile.phone,
            location = profile.location,
            bio = profile.bio,
            skills = profile.skills,
            portfolioUrl = profile.portfolioUrl,
            linkedinUrl = profile.linkedinUrl,
            yearsOfExperience = profile.yearsOfExperience,
            expectedSalaryMin = profile.expectedSalaryMin
        )
        userProfileDao.saveUserProfile(entity)
    }
}
