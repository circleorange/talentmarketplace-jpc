package com.talentmarketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.talentmarketplace.models.OfferModel
import com.talentmarketplace.ui.theme.OfferJPCTheme
import timber.log.Timber
import timber.log.Timber.i


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfferJPCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column( modifier = Modifier.fillMaxSize())
                    {
                        ShowToolBar()
                        ShowAddOffer()
                    }
                }
            }
        }
        Timber.plant(Timber.DebugTree())
        i("Talent Marketplace started")
    }
}

fun addOffer(title: String) {
    var offer = OfferModel()

    offer.title = title

    i("Title entered : $title")
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ShowAddOffer() {
    var title by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    OfferJPCTheme {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    placeholderColor = MaterialTheme.colorScheme.onPrimary
                ),
                value = title,
                onValueChange = {
                    title = it
                    showError = false
                },
                isError = showError,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.text_titleHint)) },
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
                supportingText = { ShowSupportText(showError) }
            )

            Button(
                onClick = {
                    if (title.isEmpty()) { showError = true }
                    else { addOffer(title) }
                },
                elevation = ButtonDefaults.buttonElevation(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(width = 4.dp))
                Text(stringResource(id = R.string.button_addOffer))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ShowToolBar() {
    OfferJPCTheme {
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
fun ShowSupportText(isError : Boolean)
{
    if (isError)
        Text(
            text = "This Field is Required",
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
    OfferJPCTheme {
        Greeting("Android")
    }
}