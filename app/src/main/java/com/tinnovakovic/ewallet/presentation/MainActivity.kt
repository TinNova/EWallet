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
import com.tinnovakovic.ewallet.shared.navigation.Destination
import com.tinnovakovic.ewallet.shared.navigation.NavManager
import com.tinnovakovic.ewallet.shared.network.NetworkInformationProvider
import com.tinnovakovic.ewallet.ui.theme.EWalletTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navManager: NavManager

    @Inject
    lateinit var networkInformationProvider: NetworkInformationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkInformationProvider.observeNetwork()

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

// TODO Question From Argent:
//  -- Question: How would you store a private key in an App
//    --  Answer: I would only store the Wallet Address while the app is active, it will be stored in encrypted SharedPref (DataStore exists but it doesn't have encryption capabilities.)
//    --  The moment the app terminates I would delete the Wallet Address.
//    --  Everytime the user logs in or the Wallet Address is requested but the value in SharedPref is null I would request it again via the secured HTTPS protocol, I would encrypt it and save it in sharedPref, then read it from sharedPref (following the offline app best practises of single source of truth.)
//        -- Alternatively I would never save the private key, I would always get it from HTTPS whenever it is required
//  -- Question: Architecture that facilitates future changes & What would you like to highlight
//    --  Answer: I'd like to highlight the InMemoryCache, I created it as an interface so that it can be reused
//    --  I created it to save the topTokens, without the InMemoryCache the topTokens would have to be saved as a global variable which could affect the single source of truth principle and pose a challenge to unit testing
//  -- Question: How do you handle rate limiting
//    --  Answer: The GetTopTokensUseCase helps by filtering the list, it only receives a list after a half second debounce and we save the list of 20 tokens and fetch them
//    --  Answer: In GetTokensWithBalancesUseCase we have a delay of 200ms if the list contains 5 or more tokens, this prevents the limit ever being hit, this is preferred to hitting the limit and then doing exponential backoff
//  -- Question: What improvements would you make
//    --  Answer: If the rate limiting didn't exist I would create a loop that starts every call in an async call prevent a queue occuring where one needs to complete before the next

// TODO:
//  -- First complete the main function of the app
//    -- When restoring state after system kills app the TextField cursor is on the left instead of the right
//    -- GetTokensWithBalancesUseCase has global vals, this needs to be removed to test it
//  -- Second write tests for the main function of the app
//    -- Specifically test the 429, rate limiting code!
//  -- Third, consider animations
//  -- Animate the button like this: https://dribbble.com/shots/17734097-Uber-Green-App-Animation-Exploration
//  -- Animate TopAppBar like this: https://m3.material.io/components/top-app-bar/guidelines#0e9878bc-c1e1-46a5-afa5-4b43a1949dc1
//  -- Use the same colours as in: https://m3.material.io/components/top-app-bar/guidelines#0e9878bc-c1e1-46a5-afa5-4b43a1949dc1
//  -- Animate search bar?: https://dribbble.com/shots/20691553-Animated-Search-Bar-for-an-e-commerce-app
//  -- Pretend you have a private key, retrieve it from a repo that returns a string "pretendPrivateKey", save it in sharedPref? - Might be overkill...
//  -- Consider returning the tokens in a flow, they will all display as quickly as they do now, but the user will get the first results faster..this is a product decision..


// DONE
//    -- Improve HTTP Error handling, make sure we handle all the Ethplorer and Ethscaner errors can send us.
//    -- Save state in case Android terminates the app when it's in the background
//    -- No Results error message never occurs
//    -- If we've fetched data for "u" we don't have to fetch data for "ud" instead we can filter the original data to display only results with "ud"
//    -- Type "u", deleting it, then typing "u" again doesn't do a second network call
//    -- Work on the rate limiting, create a delay between calls of 200ms and handle a rate limiting error as well
//      -- Handle it as an interface so it can be used in other parts of the code
//    -- How to handle exception in Coroutines, handle 429 exception specifically