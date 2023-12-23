package com.talentmarketplace.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import com.talentmarketplace.view.screen.JobPostingScreen
import com.talentmarketplace.view.screen.JobPostingListScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.talentmarketplace.view.component.ToolBar

data class BottomNavigationItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold (
        topBar = { ToolBar() },
        bottomBar = { NavigationBar(navController) } ) {
        innerPadding ->
        NavHost(
            navController,
            startDestination = "Home",
            Modifier.padding(innerPadding)) {
            composable("Home") { JobPostingListScreen() }
            composable("Create") { JobPostingScreen() }
            composable("Profile") { JobPostingListScreen() }
            composable("Settings") { JobPostingListScreen() }
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
                    navController.navigate(item.label)
                },
                label = { Text(text = item.label) },
                icon = {
                    BadgedBox(badge = {
                        if (item.badgeCount != null) {
                            Badge {  Text(text = item.badgeCount.toString()) }
                        }
                    } ) {
                        Icon(
                            // depends whether icon selected
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            }
                            else item.unselectedIcon,
                            contentDescription = item.label )
                    }
                }
            )
        }
    }
}
