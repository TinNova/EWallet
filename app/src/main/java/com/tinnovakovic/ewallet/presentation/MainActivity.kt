package com.tinnovakovic.ewallet.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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

        var isRecoveringFromProcessDeath = savedInstanceState?.let {
            savedInstanceState.getBoolean(RESTORING_FROM_PROCESS_DEATH, false)
        } ?: false

        setContent {
            EWalletTheme {
                val navController = rememberNavController()


                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Destination.Home.name,
                        Modifier.padding(innerPadding)
                    ) {
                        homeScreen()
                        searchScreen()
                    }

                    navManager.commands.collectAsState().value.also { command ->
                        if (isRecoveringFromProcessDeath) {
                            isRecoveringFromProcessDeath = false
                        } else {
                            if (command.destinationRoute.isNotEmpty()) navController.navigate(
                                command.destinationRoute
                            )
                        }
                    }
                }
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(RESTORING_FROM_PROCESS_DEATH, true)

    }

    companion object {
        const val RESTORING_FROM_PROCESS_DEATH = "restoringFromProcessDeath"
    }

}
