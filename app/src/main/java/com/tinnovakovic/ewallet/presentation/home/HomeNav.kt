package com.tinnovakovic.ewallet.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tinnovakovic.ewallet.shared.Destination

fun NavGraphBuilder.homeScreen() {
    composable(route = Destination.Home.name) {
        HomeScreen()
    }
}