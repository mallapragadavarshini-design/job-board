package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.local.AppDatabase
import com.example.data.repository.JobRepository
import com.example.ui.JobHubApp
import com.example.ui.theme.JobHubTheme
import com.example.ui.viewmodel.JobBoardViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: JobBoardViewModel by viewModels {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = JobRepository(database)
        JobBoardViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobHubTheme {
                JobHubApp(viewModel = viewModel)
            }
        }
    }
}
