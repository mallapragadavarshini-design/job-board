package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString("|||") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return value.split("|||")
    }
}

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: String,
    val title: String,
    val company: String,
    val location: String,
    val workType: String,
    val employmentType: String,
    val salaryMin: Int,
    val salaryMax: Int,
    val salaryFormatted: String,
    val experienceLevel: String,
    val postedAgo: String,
    val category: String,
    val skills: List<String>,
    val description: String,
    val responsibilities: List<String>,
    val requirements: List<String>,
    val benefits: List<String>,
    val companyWebsite: String,
    val companySize: String,
    val logoColorHex: String,
    val isFeatured: Boolean,
    val isUserCreated: Boolean,
    val applicantsCount: Int
)

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey val id: String,
    val jobId: String,
    val jobTitle: String,
    val company: String,
    val appliedDate: String,
    val status: String, // APPLIED, REVIEWING, INTERVIEWING, OFFER, REJECTED
    val applicantName: String,
    val applicantEmail: String,
    val notes: String = "",
    val interviewDate: String = ""
)

@Entity(tableName = "saved_jobs")
data class SavedJobEntity(
    @PrimaryKey val jobId: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val title: String,
    val email: String,
    val phone: String,
    val location: String,
    val bio: String,
    val skills: List<String>,
    val portfolioUrl: String,
    val linkedinUrl: String,
    val yearsOfExperience: Int,
    val expectedSalaryMin: Int
)
