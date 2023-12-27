package com.talentmarketplace.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.talentmarketplace.viewmodel.JobPostListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.talentmarketplace.model.JobPostModel
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.localDateFromTimestamp
import com.talentmarketplace.utils.FirestoreConversionManager.Companion.payRangeFromString
import com.talentmarketplace.view.component.FilterChipComponent
import com.talentmarketplace.view.navigation.LocalNavController
import timber.log.Timber.i

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostListScreen(
    viewModel: JobPostListViewModel = hiltViewModel(),
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

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FilterChipComponent(
                onClick = { viewModel.showAllPosts() },
                selected = viewModel.filter.value == "all",
                label = "All",
            )

            Spacer(modifier = Modifier.padding(12.dp))

            FilterChipComponent(
                onClick = { viewModel.showOwnPosts() },
                selected = viewModel.filter.value == "own",
                label = "Own",
            )
        }

        LazyColumn {
            items(jobPostings) { jobPosting -> JobPostItem(jobPosting) {
                viewModel.onClickJobPost(jobPosting.jobPostID) }
            }
        }
    }
}

// Layout for single list item
@Composable
fun JobPostItem(jobPost: JobPostModel, onClick: () -> Unit) {
    i("JobPostListScreen.JobPostItem.param: $jobPost")
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) ) {

        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column (modifier = Modifier
                .padding(16.dp) ) {
                Text("Company: ${jobPost.companyName}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Position: ${jobPost.title}")
            }
            Column (modifier = Modifier
                .padding(16.dp) ) {
                Text("Start: ${localDateFromTimestamp(jobPost.startDate)}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Pay: ${payRangeFromString(jobPost.payRange)}")
            }
        }

    }
}
