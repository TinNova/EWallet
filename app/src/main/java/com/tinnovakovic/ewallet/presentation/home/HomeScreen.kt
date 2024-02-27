package com.tinnovakovic.ewallet.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tinnovakovic.ewallet.presentation.home.HomeContract.UiEvents
import com.tinnovakovic.ewallet.presentation.home.HomeContract.UiState

@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@Composable
fun HomeScreenContent(
    uiState: UiState,
    uiAction: (UiEvents) -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
    ) {

        Text(
            text = "Wallet Address",
            modifier = Modifier.padding(12.dp)
        )
        Text(
            text = "Hard coded wallet address",
            fontSize = 16.sp,
            modifier = Modifier.padding(12.dp)
        ) //Put it in encrypted sharedPref

        Spacer(modifier = Modifier.height(64.dp))
        OutlinedButton(
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 64.dp, vertical = 24.dp),
            onClick = {
                uiAction(UiEvents.ButtonClicked)
            }
        ) {
            Text(text = "ERC20 TOKENS")
        }
    }


}