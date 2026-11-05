package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApplicationRecord
import com.example.data.model.ApplicationStatus
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.PrimaryBlue

@Composable
fun ApplicationsScreen(
    applications: List<ApplicationRecord>,
    selectedFilter: ApplicationStatus?,
    onFilterChanged: (ApplicationStatus?) -> Unit,
    onUpdateStatus: (String, ApplicationStatus) -> Unit,
    onUpdateNotes: (String, String, String) -> Unit,
    onDelete: (String) -> Unit,
    onExploreJobs: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCount = applications.size
    val inReviewCount = applications.count { it.status == ApplicationStatus.REVIEWING }
    val interviewingCount = applications.count { it.status == ApplicationStatus.INTERVIEWING }
    val offerCount = applications.count { it.status == ApplicationStatus.OFFER }

    val filteredList = if (selectedFilter == null) {
        applications
    } else {
        applications.filter { it.status == selectedFilter }
    }

    var editingApplicationNotes by remember { mutableStateOf<ApplicationRecord?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("applications_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        item {
            Text(
                text = "Application Pipeline",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Track your interview stages and stay organized across every opportunity.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(label = "Total", count = totalCount, color = PrimaryBlue, modifier = Modifier.weight(1f))
                MetricCard(label = "In Review", count = inReviewCount, color = Color(0xFF0284C7), modifier = Modifier.weight(1f))
                MetricCard(label = "Interviews", count = interviewingCount, color = AccentPurple, modifier = Modifier.weight(1f))
                MetricCard(label = "Offers", count = offerCount, color = AccentEmerald, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { onFilterChanged(null) },
                    label = { Text("All (${applications.size})") },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryBlue,
                        selectedLabelColor = Color.White
                    )
                )

                ApplicationStatus.values().forEach { status ->
                    val count = applications.count { it.status == status }
                    FilterChip(
                        selected = selectedFilter == status,
                        onClick = { onFilterChanged(status) },
                        label = { Text("${status.displayName} ($count)") },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (filteredList.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.TrackChanges,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (applications.isEmpty()) "No applications yet" else "No applications in this stage",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (applications.isEmpty()) "Explore open jobs and submit your first application to track your progress here." else "Try selecting 'All' to view your complete application history.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    if (applications.isEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onExploreJobs,
                            modifier = Modifier.testTag("pipeline_explore_jobs_button")
                        ) {
                            Text("Browse Open Positions")
                        }
                    }
                }
            }
        } else {
            items(filteredList, key = { it.id }) { app ->
                ApplicationCard(
                    application = app,
                    onUpdateStatus = { status -> onUpdateStatus(app.id, status) },
                    onEditNotes = { editingApplicationNotes = app },
                    onDelete = { onDelete(app.id) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Edit notes dialog
    editingApplicationNotes?.let { app ->
        NotesEditDialog(
            application = app,
            onSave = { notes, interviewDate ->
                onUpdateNotes(app.id, notes, interviewDate)
                editingApplicationNotes = null
            },
            onDismiss = { editingApplicationNotes = null }
        )
    }
}

@Composable
fun ApplicationCard(
    application: ApplicationRecord,
    onUpdateStatus: (ApplicationStatus) -> Unit,
    onEditNotes: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val statusColor = when (application.status) {
        ApplicationStatus.APPLIED -> PrimaryBlue
        ApplicationStatus.REVIEWING -> Color(0xFF0284C7)
        ApplicationStatus.INTERVIEWING -> AccentPurple
        ApplicationStatus.OFFER -> AccentEmerald
        ApplicationStatus.REJECTED -> Color(0xFF64748B)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("application_item_${application.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = application.jobTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = application.company,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Applied on ${application.appliedDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        Text(
                            text = "Change Stage",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                        ApplicationStatus.values().forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.displayName) },
                                onClick = {
                                    onUpdateStatus(st)
                                    showMenu = false
                                },
                                leadingIcon = {
                                    if (application.status == st) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = PrimaryBlue)
                                    }
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Add / Edit Notes") },
                            onClick = {
                                showMenu = false
                                onEditNotes()
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Application", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Status Badge + Quick Stage buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = application.status.displayName,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                TextButton(onClick = onEditNotes) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (application.notes.isBlank()) "Add Note" else "View Note", fontSize = 12.sp)
                }
            }

            // Notes preview if present
            if (application.notes.isNotBlank() || application.interviewDate.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        if (application.interviewDate.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Interview: ${application.interviewDate}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryBlue
                                )
                            }
                        }
                        if (application.notes.isNotBlank()) {
                            if (application.interviewDate.isNotBlank()) Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = application.notes,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NotesEditDialog(
    application: ApplicationRecord,
    onSave: (notes: String, interviewDate: String) -> Unit,
    onDismiss: () -> Unit
) {
    var notes by remember { mutableStateOf(application.notes) }
    var interviewDate by remember { mutableStateOf(application.interviewDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Application Notes") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${application.jobTitle} • ${application.company}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryBlue
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = interviewDate,
                    onValueChange = { interviewDate = it },
                    label = { Text("Next Interview Date / Time") },
                    placeholder = { Text("e.g. Next Tuesday at 2 PM PST") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Private Interview Notes") },
                    placeholder = { Text("Recruiter contact, questions asked, salary offer notes...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(notes.trim(), interviewDate.trim()) },
                modifier = Modifier.testTag("save_application_notes_button")
            ) {
                Text("Save Notes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
