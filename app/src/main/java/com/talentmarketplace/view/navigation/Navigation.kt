package com.talentmarketplace.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.talentmarketplace.view.component.ToolBar
import com.talentmarketplace.view.screen.JobPostingScreen
import com.talentmarketplace.view.screen.JobPostingListScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.JobPostingListScreen.route) {
        composable(Screen.JobPostingListScreen.route) {
            JobPostingListScreen(navController)
        }
        composable(Screen.JobPostingScreen.route) {
            JobPostingScreen(navController)
        }
    }
}

@Composable
fun JobPostingListScreen(navController: NavController) {
    ToolBar(onAddClicked = { navController.navigate(Screen.JobPostingScreen.route) })
    JobPostingListScreen()
}

@Composable
fun JobPostingScreen(navController: NavController) {
    ToolBar(onAddClicked = { navController.navigate(Screen.JobPostingListScreen.route) })
    JobPostingScreen()
}
