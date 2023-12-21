package com.talentmarketplace.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.talentmarketplace.view.component.ToolBar
import com.talentmarketplace.view.screen.JobPostingListScreen
import com.talentmarketplace.view.theme.JobPostingJPCTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.i
import com.talentmarketplace.view.navigation.Navigation
import com.talentmarketplace.view.navigation.Screen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            JobPostingJPCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column( modifier = Modifier.fillMaxSize())
                    {
                        Navigation()
                    }
                }
            }
        }
        Timber.plant(Timber.DebugTree())
        i("Talent Marketplace started")
    }
}
