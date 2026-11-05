package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        JobEntity::class,
        ApplicationEntity::class,
        SavedJobEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jobDao(): JobDao
    abstract fun applicationDao(): ApplicationDao
    abstract fun savedJobDao(): SavedJobDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jobhub_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate default jobs on database creation
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                database.jobDao().insertAll(PrepopulatedJobs.initialJobs)
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
                                database.userProfileDao().saveUserProfile(initialProfile)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
