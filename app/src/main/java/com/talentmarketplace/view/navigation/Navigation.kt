package com.talentmarketplace.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.talentmarketplace.view.screen.JobPostScreen
import com.talentmarketplace.view.screen.JobPostListScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.currentBackStackEntryAsState
import com.talentmarketplace.view.component.ToolBar
import com.talentmarketplace.view.screen.ProfileScreen
import com.talentmarketplace.view.screen.SettingsScreen
import com.talentmarketplace.view.screen.SignInScreen
import com.talentmarketplace.view.screen.SignUpScreen

data class BottomNavigationItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

val LocalNavController = compositionLocalOf<NavController> {
    error("No Controller")
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold (
            topBar = {
                ToolBar()
                     },
            bottomBar = {
                // Do not show bottomBar during SignUp or SignIn
                val currentRoute = navController
                    .currentBackStackEntryAsState()
                    .value?.destination?.route

                if (
                    currentRoute != Routes.Auth.SignUp.route
                    && currentRoute != Routes.Auth.SignIn.route
                ) {
                    NavigationBar (navController)
                }
            }
        ) {
            innerPadding -> NavHost(
                navController,
                startDestination = Routes.Auth.SignIn.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Routes.User.Settings.route) { SettingsScreen() }
                composable(Routes.User.Profile.route) { ProfileScreen() }
                composable(Routes.Auth.SignUp.route) { SignUpScreen() }
                composable(Routes.Auth.SignOut.route) { SignInScreen() }
                composable(Routes.Auth.SignIn.route) { SignInScreen() }
                composable(Routes.Job.List.route) { JobPostListScreen() }
                composable(Routes.Job.Create.route) { JobPostScreen() }
                composable(Routes.Job.Get.route) { backStackEntry ->
                    val jobPostID = backStackEntry.arguments?.getString("id")
                    JobPostScreen(
                        jobPostID = jobPostID,
                        isEditMode = true
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem(
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            badgeCount = 23,
        ),
        BottomNavigationItem(
            label = "Create",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add,
            badgeCount = 23,
        ),
        BottomNavigationItem(
            label = "Profile",
            selectedIcon = Icons.Filled.Email,
            unselectedIcon = Icons.Outlined.Email
        ),
        BottomNavigationItem(
            label = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    when (index) {
                        0 -> navController.navigate(Routes.Job.List.route)
                        1 -> navController.navigate(Routes.Job.Create.route)
                        2 -> navController.navigate(Routes.User.Profile.route)
                        3 -> navController.navigate(Routes.User.Settings.route)
                    }
                },
                label = { Text(text = item.label) },
                icon = {
                    Icon(
                        // depends whether icon selected
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        }
                        else item.unselectedIcon,
                        contentDescription = item.label
                    )
                }
            )
        }
    }
}
