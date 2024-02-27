package com.tinnovakovic.ewallet.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tinnovakovic.ewallet.presentation.home.homeScreen
import com.tinnovakovic.ewallet.presentation.search.searchScreen
import com.tinnovakovic.ewallet.shared.Destination
import com.tinnovakovic.ewallet.shared.NavManager
import com.tinnovakovic.ewallet.ui.theme.EWalletTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navManager: NavManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EWalletTheme {
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    navManager.commands.collectAsState().value.also { command ->
                        if (command.destinationRoute.isNotEmpty()) navController.navigate(command.destinationRoute)
                    }
                    NavHost(
                        navController = navController,
                        startDestination = Destination.Home.name,
                        Modifier.padding(innerPadding)
                    ) {
                        homeScreen()
                        searchScreen()
                    }
                }
            }
        }
    }
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
    EWalletTheme {
        Greeting("Android")
    }
}

// TODO:
//  -- Question: How would you store a private key in an App
//    --  Answer: I would only store the Wallet Address while the app is active, it will be stored in encrypted SharedPref (DataStore exists but it doesn't have encryption capabilities.)
//    --  The moment the app terminates I would delete the Wallet Address.
//    --  Everytime the user logs in or the Wallet Address is requested but the value in SharedPref is null I would request it again via the secured HTTPS protocol, I would encrypt it and save it in sharedPref, then read it from sharedPref (following the offline app best practises of single source of truth.)
//        -- Alternatively I would never save the private key, I would always get it from HTTPS whenever it is required

// TODO:
//  -- Pretend you have a private key, retrieve it from a repo that returns a string "pretendPrivateKey", save it in sharedPref? - Might be overkill...