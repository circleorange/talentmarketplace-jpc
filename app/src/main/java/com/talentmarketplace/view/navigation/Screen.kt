package com.talentmarketplace.view.navigation

// Only classes within sealed class can inherit it's properties
sealed class Screen(val route: String) {
    object JobPostingListScreen : Screen("job_posting_list")
    object JobPostingScreen : Screen("job_posting")
}