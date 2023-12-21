package com.talentmarketplace.view.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.talentmarketplace.viewmodel.JobPostingListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.talentmarketplace.model.JobPostingModel
import com.talentmarketplace.view.theme.JobPostingJPCTheme

@Composable
fun JobPostingListScreen(viewModel: JobPostingListViewModel = hiltViewModel()) {
    val jobPostings by viewModel.jobPostings.collectAsState()

    JobPostingJPCTheme {
        LazyColumn {
            items(jobPostings) { jobPosting -> JobPostingItem(jobPosting) }
        }
    }

}

// Layout for single list item
@Composable
fun JobPostingItem(jobPosting: JobPostingModel) {
    Text("Company: ${jobPosting.companyName}")
    Text("Position: ${jobPosting.title}")
}
