package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ApplyJobDialog
import com.example.ui.screens.ApplicationsScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.JobDetailScreen
import com.example.ui.screens.PostJobScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedJobsScreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal
import com.example.ui.viewmodel.JobBoardViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.flow.collectLatest

sealed class NavItem(
    val screen: Screen,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
) {
    object Explore : NavItem(Screen.EXPLORE, "Explore", Icons.Filled.Search, Icons.Outlined.Search, "nav_explore")
    object Saved : NavItem(Screen.SAVED, "Saved", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder, "nav_saved")
    object Applications : NavItem(Screen.APPLICATIONS, "Pipeline", Icons.Filled.TrackChanges, Icons.Outlined.TrackChanges, "nav_pipeline")
    object PostJob : NavItem(Screen.POST_JOB, "Post Job", Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline, "nav_post_job")
    object Profile : NavItem(Screen.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person, "nav_profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobHubApp(viewModel: JobBoardViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedJob by viewModel.selectedJob.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val filteredJobs by viewModel.filteredJobs.collectAsStateWithLifecycle()
    val featuredJobs by viewModel.featuredJobs.collectAsStateWithLifecycle()
    val savedJobs by viewModel.savedJobs.collectAsStateWithLifecycle()
    val applications by viewModel.applications.collectAsStateWithLifecycle()
    val selectedAppStatusFilter by viewModel.selectedAppStatusFilter.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val jobToApply by viewModel.jobToApply.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.snackbarEvent) {
        viewModel.snackbarEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val navItems = listOf(
        NavItem.Explore,
        NavItem.Saved,
        NavItem.Applications,
        NavItem.PostJob,
        NavItem.Profile
    )

    // Handle back button when not on Explore
    if (currentScreen != Screen.EXPLORE && currentScreen != Screen.DETAIL) {
        BackHandler {
            viewModel.navigateTo(Screen.EXPLORE)
        }
    }

    if (currentScreen == Screen.DETAIL && selectedJob != null) {
        JobDetailScreen(
            job = selectedJob!!,
            onBack = { viewModel.closeJobDetail() },
            onToggleSave = { viewModel.toggleSave(selectedJob!!) },
            onApply = { viewModel.openApplyModal(selectedJob!!) }
        )
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PrimaryBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "J",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "JobHub",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar")
                ) {
                    navItems.forEach { item ->
                        val isSelected = currentScreen == item.screen
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(item.screen) },
                            icon = {
                                if (item == NavItem.Applications && applications.isNotEmpty()) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = PrimaryBlue,
                                                contentColor = Color.White
                                            ) {
                                                Text(applications.size.toString())
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    }
                                } else if (item == NavItem.Saved && savedJobs.isNotEmpty()) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = SecondaryTeal,
                                                contentColor = Color.White
                                            ) {
                                                Text(savedJobs.size.toString())
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryBlue,
                                selectedTextColor = PrimaryBlue,
                                indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag(item.tag)
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.EXPLORE -> {
                        ExploreScreen(
                            jobs = filteredJobs,
                            featuredJobs = featuredJobs,
                            filterState = filterState,
                            onQueryChanged = { viewModel.setQuery(it) },
                            onCategoryChanged = { viewModel.setCategory(it) },
                            onApplyFilter = { workType, exp, minSal ->
                                viewModel.setWorkType(workType)
                                viewModel.setExperience(exp)
                                viewModel.setMinSalary(minSal)
                            },
                            onResetFilters = { viewModel.resetFilters() },
                            onJobClick = { viewModel.openJobDetail(it) },
                            onToggleSave = { viewModel.toggleSave(it) }
                        )
                    }
                    Screen.SAVED -> {
                        SavedJobsScreen(
                            savedJobs = savedJobs,
                            onJobClick = { viewModel.openJobDetail(it) },
                            onToggleSave = { viewModel.toggleSave(it) },
                            onExploreJobs = { viewModel.navigateTo(Screen.EXPLORE) }
                        )
                    }
                    Screen.APPLICATIONS -> {
                        ApplicationsScreen(
                            applications = applications,
                            selectedFilter = selectedAppStatusFilter,
                            onFilterChanged = { viewModel.setApplicationStatusFilter(it) },
                            onUpdateStatus = { id, status -> viewModel.updateApplicationStatus(id, status) },
                            onUpdateNotes = { id, notes, interviewDate -> viewModel.updateApplicationNotes(id, notes, interviewDate) },
                            onDelete = { viewModel.deleteApplication(it) },
                            onExploreJobs = { viewModel.navigateTo(Screen.EXPLORE) }
                        )
                    }
                    Screen.POST_JOB -> {
                        PostJobScreen(
                            onPostJob = { newJob -> viewModel.postNewJob(newJob) }
                        )
                    }
                    Screen.PROFILE -> {
                        ProfileScreen(
                            profile = userProfile,
                            allJobs = filteredJobs,
                            appliedCount = applications.size,
                            savedCount = savedJobs.size,
                            onUpdateProfile = { viewModel.updateProfile(it) }
                        )
                    }
                    Screen.DETAIL -> {}
                }
            }
        }
    }

    // Modal apply dialog
    jobToApply?.let { job ->
        ApplyJobDialog(
            job = job,
            userProfile = userProfile,
            onSubmit = { name, email, notes ->
                viewModel.submitApplication(job, name, email, notes)
            },
            onDismiss = { viewModel.closeApplyModal() }
        )
    }
}
