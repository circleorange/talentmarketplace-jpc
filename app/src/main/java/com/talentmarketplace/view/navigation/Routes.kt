package com.talentmarketplace.view.navigation

// Only classes within sealed class can inherit it's properties
sealed class Routes(val route: String) {
    object Home : Routes("home")
    object JobPostRoutes : Routes("jobPost")
    object SignUp : Routes("signUp")
}