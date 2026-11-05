package com.example

import com.example.data.local.PrepopulatedJobs
import com.example.data.model.ApplicationStatus
import com.example.data.model.ExperienceLevel
import com.example.data.model.JobCategory
import com.example.data.model.WorkType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun initialJobs_dataset_hasFeaturedAndMultipleCategories() {
    val jobs = PrepopulatedJobs.initialJobs
    assertTrue("Should have initial jobs", jobs.isNotEmpty())
    assertTrue("Should contain featured jobs", jobs.any { it.isFeatured })
    assertTrue("Should have mobile category", jobs.any { it.category == "Mobile" })
    assertTrue("Should have engineering category", jobs.any { it.category == "Engineering" })

    val androidJob = jobs.firstOrNull { it.id == "job_1" }
    assertNotNull("Android job must exist", androidJob)
    assertEquals("Senior Android Engineer", androidJob?.title)
    assertTrue(androidJob?.skills?.contains("Kotlin") == true)
  }

  @Test
  fun jobModels_and_filters_behaveCorrectly() {
    val job = PrepopulatedJobs.initialJobs.first()
    assertEquals(150000, job.salaryMin)
    assertTrue(job.salaryMax > job.salaryMin)
    assertEquals(JobCategory.ALL.displayName, "All Roles")
    assertEquals(WorkType.REMOTE.displayName, "Remote")
    assertEquals(ExperienceLevel.SENIOR.displayName, "Senior")
    assertEquals(ApplicationStatus.APPLIED.displayName, "Applied")
  }
}
