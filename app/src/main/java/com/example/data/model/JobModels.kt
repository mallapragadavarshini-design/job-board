package com.example.data.model

enum class JobCategory(val displayName: String) {
    ALL("All Roles"),
    ENGINEERING("Engineering"),
    MOBILE("Mobile"),
    FRONTEND("Frontend"),
    BACKEND("Backend"),
    AI_ML("AI & Machine Learning"),
    DESIGN("UI/UX Design"),
    PRODUCT("Product Management"),
    DATA("Data Science"),
    DEVOPS("Cloud & DevOps")
}

enum class WorkType(val displayName: String) {
    ALL("All Types"),
    REMOTE("Remote"),
    HYBRID("Hybrid"),
    ONSITE("On-site")
}

enum class ExperienceLevel(val displayName: String) {
    ALL("All Levels"),
    ENTRY("Entry Level"),
    MID("Mid-Level"),
    SENIOR("Senior"),
    LEAD("Lead / Principal")
}

enum class EmploymentType(val displayName: String) {
    FULL_TIME("Full-time"),
    CONTRACT("Contract"),
    PART_TIME("Part-time"),
    INTERNSHIP("Internship")
}

enum class ApplicationStatus(val displayName: String) {
    APPLIED("Applied"),
    REVIEWING("In Review"),
    INTERVIEWING("Interviewing"),
    OFFER("Offer Received"),
    REJECTED("Archived")
}

data class Job(
    val id: String,
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
    val isFeatured: Boolean = false,
    val isUserCreated: Boolean = false,
    val applicantsCount: Int = 0,
    val isSaved: Boolean = false,
    val isApplied: Boolean = false
)

data class ApplicationRecord(
    val id: String,
    val jobId: String,
    val jobTitle: String,
    val company: String,
    val appliedDate: String,
    val status: ApplicationStatus,
    val applicantName: String,
    val applicantEmail: String,
    val notes: String = "",
    val interviewDate: String = ""
)

data class UserProfile(
    val name: String = "Alex Morgan",
    val title: String = "Senior Mobile & Fullstack Engineer",
    val email: String = "alex.morgan@example.com",
    val phone: String = "+1 (555) 234-5678",
    val location: String = "San Francisco, CA (Open to Remote)",
    val bio: String = "Passionate product-minded engineer with 6+ years specializing in Android (Kotlin, Jetpack Compose, Coroutines), modern mobile architecture, and scalable cloud systems.",
    val skills: List<String> = listOf("Kotlin", "Jetpack Compose", "Android SDK", "Coroutines & Flow", "Room", "KMP", "System Design", "CI/CD"),
    val portfolioUrl: String = "https://alexmorgan.dev",
    val linkedinUrl: String = "https://linkedin.com/in/alexmorgan-dev",
    val yearsOfExperience: Int = 6,
    val expectedSalaryMin: Int = 160000
)
