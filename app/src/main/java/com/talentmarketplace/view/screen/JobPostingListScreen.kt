package com.talentmarketplace.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.talentmarketplace.viewmodel.JobPostingListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.view.navigation.LocalNavController
import timber.log.Timber.i

@Composable
fun JobPostingListScreen(
    viewModel: JobPostingListViewModel = hiltViewModel(),
) {

    val navController = LocalNavController.current

    // Get job postings from view model
    val jobPostings by viewModel.jobPostings.collectAsState()

    // Collect exposed navigation commands from view model
    LaunchedEffect(viewModel) {
        viewModel.navCmd.collect {
            route -> navController.navigate(route)
        }
    }

    LazyColumn {
        items(jobPostings) { jobPosting -> JobPostingItem(jobPosting) {
            viewModel.onClickJobPost(jobPosting.id) }
        }
    }
}

// Layout for single list item
@Composable
fun JobPostingItem(jobPosting: JobPostModel, onClick: () -> Unit) {
    i("JobPostingListScreen.JobPostingItem.param: $jobPosting")
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) ) {
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) ) {
            Text("Company: ${jobPosting.companyName}")
            Spacer(modifier = Modifier.height(4.dp))
            Text("Position: ${jobPosting.title}")
        }
    }
}
