package com.tinnovakovic.ewallet.presentation.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tinnovakovic.ewallet.shared.navigation.Destination

fun NavGraphBuilder.searchScreen() {
    composable(route = Destination.Search.name) {
        SearchScreen()
    }
}