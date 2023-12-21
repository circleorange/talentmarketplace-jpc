package com.talentmarketplace.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.talentmarketplace.view.components.ShowToolBar
import com.talentmarketplace.view.screens.JobPostingScreen
import com.talentmarketplace.view.theme.JobPostingJPCTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.i

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobPostingJPCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column( modifier = Modifier.fillMaxSize())
                    {
                        ShowToolBar()
                        JobPostingScreen()
                    }
                }
            }
        }
        Timber.plant(Timber.DebugTree())
        i("Talent Marketplace started")
    }
}


