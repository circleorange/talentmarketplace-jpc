package com.talentmarketplace.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.talentmarketplace.R
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


// TODO: Move StandardTextField to utils
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelResourceID: Int,
    showError: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            placeholderColor = MaterialTheme.colorScheme.onPrimary
        ),
        value = value,
        onValueChange = { onValueChange(it) },
        isError = showError,
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(id = labelResourceID)) },
        trailingIcon = {
            if (showError)
                Icon(
                    Icons.Filled.Warning, "error",
                    tint = MaterialTheme.colorScheme.error
                )
            else
                Icon(
                    Icons.Default.Edit, contentDescription = "add/edit",
                    tint = Color.Black
                )
        } ,
        supportingText = { ShowSupportText(showError, errorMessage) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ShowToolBar() {
    JobPostingJPCTheme {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = Color.White
                )
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            navigationIcon = {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        )
    }
}

@Composable
fun ShowSupportText(isError : Boolean, errorMessage: String?)
{
    if (isError)
        Text(
            text = errorMessage!!,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error,
        )
    else Text(text = "")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JobPostingJPCTheme {
        Greeting("Android")
    }
}