package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmploymentType
import com.example.data.model.ExperienceLevel
import com.example.data.model.Job
import com.example.data.model.JobCategory
import com.example.data.model.WorkType
import com.example.ui.theme.PrimaryBlue
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostJobScreen(
    onPostJob: (Job) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(JobCategory.ENGINEERING.displayName) }
    var location by remember { mutableStateOf("Remote") }
    var workType by remember { mutableStateOf(WorkType.REMOTE.displayName) }
    var employmentType by remember { mutableStateOf(EmploymentType.FULL_TIME.displayName) }
    var experienceLevel by remember { mutableStateOf(ExperienceLevel.SENIOR.displayName) }
    var salaryMin by remember { mutableStateOf("140000") }
    var salaryMax by remember { mutableStateOf("180000") }
    var skillsInput by remember { mutableStateOf("Kotlin, Jetpack Compose, Coroutines, Room, Git") }
    var description by remember { mutableStateOf("We are seeking a talented engineer to join our fast-growing product team. You will lead development of core user experiences and shape our mobile architecture.") }
    var responsibilitiesInput by remember { mutableStateOf("Architect and deliver modern Kotlin mobile apps\nCollaborate closely with design and backend teams\nOptimize app startup time and maintain zero crash rates") }
    var requirementsInput by remember { mutableStateOf("4+ years experience with Android / Kotlin\nDeep knowledge of Jetpack Compose\nPassion for writing clean, tested, maintainable code") }
    var benefitsInput by remember { mutableStateOf("100% remote flexibility\nFull medical, dental & vision coverage\n$2,000 annual learning & hardware stipend") }
    var companyWebsite by remember { mutableStateOf("https://examplecompany.com") }
    var companySize by remember { mutableStateOf("50–200 employees") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var workTypeExpanded by remember { mutableStateOf(false) }
    var experienceExpanded by remember { mutableStateOf(false) }

    fun fillSampleData() {
        title = "Staff Kotlin Multiplatform Engineer"
        company = "NovaTech Digital"
        category = JobCategory.MOBILE.displayName
        location = "Austin, TX (Remote Friendly)"
        workType = WorkType.REMOTE.displayName
        employmentType = EmploymentType.FULL_TIME.displayName
        experienceLevel = ExperienceLevel.LEAD.displayName
        salaryMin = "175000"
        salaryMax = "225000"
        skillsInput = "Kotlin Multiplatform, Compose Multiplatform, Swift, Coroutines, Gradle"
        description = "Join NovaTech to spearhead our cross-platform architecture, sharing domain logic across Android, iOS, and Desktop."
        responsibilitiesInput = "Build shared business libraries in KMP\nEstablish CI/CD build matrix with GitHub Actions\nMentor mobile developers across squads"
        requirementsInput = "6+ years mobile engineering experience\nProduction Kotlin Multiplatform experience\nStrong understanding of native Swift & Kotlin memory models"
        benefitsInput = "Top 1% market salary + equity\nUnlimited PTO and wellness stipends\nTop-of-the-line M3 Max MacBook Pro"
        companyWebsite = "https://novatech.io"
        companySize = "200–500 employees"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("post_job_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Post a New Job",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Publish an opening to thousands of active candidates instantly.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedButton(
                    onClick = { fillSampleData() },
                    modifier = Modifier.testTag("fill_sample_job_button")
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryBlue)
                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                    Text("Auto-fill", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Role Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Job Title *") },
                        placeholder = { Text("e.g. Senior Android Engineer") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("post_job_title_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = company,
                        onValueChange = { company = it },
                        label = { Text("Company Name *") },
                        placeholder = { Text("e.g. Acme Corp") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("post_job_company_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category dropdown
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            JobCategory.values().filter { it != JobCategory.ALL }.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.displayName) },
                                    onClick = {
                                        category = cat.displayName
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Work Type dropdown
                        ExposedDropdownMenuBox(
                            expanded = workTypeExpanded,
                            onExpandedChange = { workTypeExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = workType,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Work Mode") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = workTypeExpanded) },
                                modifier = Modifier.menuAnchor(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = workTypeExpanded,
                                onDismissRequest = { workTypeExpanded = false }
                            ) {
                                WorkType.values().filter { it != WorkType.ALL }.forEach { wt ->
                                    DropdownMenuItem(
                                        text = { Text(wt.displayName) },
                                        onClick = {
                                            workType = wt.displayName
                                            workTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Experience Level dropdown
                        ExposedDropdownMenuBox(
                            expanded = experienceExpanded,
                            onExpandedChange = { experienceExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = experienceLevel,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Experience") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = experienceExpanded) },
                                modifier = Modifier.menuAnchor(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = experienceExpanded,
                                onDismissRequest = { experienceExpanded = false }
                            ) {
                                ExperienceLevel.values().filter { it != ExperienceLevel.ALL }.forEach { el ->
                                    DropdownMenuItem(
                                        text = { Text(el.displayName) },
                                        onClick = {
                                            experienceLevel = el.displayName
                                            experienceExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        placeholder = { Text("e.g. San Francisco, CA or Remote") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = salaryMin,
                            onValueChange = { salaryMin = it },
                            label = { Text("Min Salary ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = salaryMax,
                            onValueChange = { salaryMax = it },
                            label = { Text("Max Salary ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = skillsInput,
                        onValueChange = { skillsInput = it },
                        label = { Text("Key Skills (comma separated)") },
                        placeholder = { Text("Kotlin, Compose, Coroutines") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Job Description & Requirements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Overview & About the Role") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = responsibilitiesInput,
                        onValueChange = { responsibilitiesInput = it },
                        label = { Text("Key Responsibilities (one per line)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = requirementsInput,
                        onValueChange = { requirementsInput = it },
                        label = { Text("Requirements & Qualifications (one per line)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = benefitsInput,
                        onValueChange = { benefitsInput = it },
                        label = { Text("Perks & Benefits (one per line)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            val minInt = salaryMin.toIntOrNull() ?: 120000
            val maxInt = salaryMax.toIntOrNull() ?: 160000
            val minK = minInt / 1000
            val maxK = maxInt / 1000
            val formattedSalary = "$$minK" + "k – $$maxK" + "k / yr"

            val canSubmit = title.isNotBlank() && company.isNotBlank()

            Button(
                onClick = {
                    if (canSubmit) {
                        val skillsList = skillsInput.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        val respList = responsibilitiesInput.split("\n").map { it.trim() }.filter { it.isNotBlank() }
                        val reqList = requirementsInput.split("\n").map { it.trim() }.filter { it.isNotBlank() }
                        val benList = benefitsInput.split("\n").map { it.trim() }.filter { it.isNotBlank() }

                        val colorPalette = listOf("#2563EB", "#0D9488", "#7C3AED", "#EC4899", "#D97706", "#059669")
                        val randomColor = colorPalette.random()

                        val newJob = Job(
                            id = "job_${UUID.randomUUID()}",
                            title = title.trim(),
                            company = company.trim(),
                            location = location.trim(),
                            workType = workType,
                            employmentType = employmentType,
                            salaryMin = minInt,
                            salaryMax = maxInt,
                            salaryFormatted = formattedSalary,
                            experienceLevel = experienceLevel,
                            postedAgo = "Just now",
                            category = category,
                            skills = if (skillsList.isEmpty()) listOf("Kotlin", "Mobile") else skillsList,
                            description = description.trim(),
                            responsibilities = respList,
                            requirements = reqList,
                            benefits = benList,
                            companyWebsite = companyWebsite.trim(),
                            companySize = companySize.trim(),
                            logoColorHex = randomColor,
                            isFeatured = false,
                            isUserCreated = true,
                            applicantsCount = 0
                        )
                        onPostJob(newJob)
                    }
                },
                enabled = canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("publish_job_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    text = "Publish Job Opening",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}
