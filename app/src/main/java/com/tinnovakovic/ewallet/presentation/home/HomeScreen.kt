package com.tinnovakovic.ewallet.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import com.tinnovakovic.ewallet.ui.theme.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tinnovakovic.ewallet.R
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
    LaunchedEffect(true) {
        // This instead on using init{} in viewModel to prevent race condition
        uiAction(HomeContract.UiEvents.Initialise)
    }

    Scaffold { scaffoldPadding ->

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
        ) {

            Text(
                text = stringResource(R.string.home_screen_title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(MaterialTheme.size.medium)
            )
            Text(
                text = uiState.walletAddress,
                fontSize = 24.sp,
                modifier = Modifier.padding(MaterialTheme.size.medium)
            ) //Put it in encrypted sharedPref

            Spacer(modifier = Modifier.height(MaterialTheme.size.medium + MaterialTheme.size.extraLarge))
            OutlinedButton(
                shape = RoundedCornerShape(MaterialTheme.size.medium),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.size.extraLarge,
                    vertical = MaterialTheme.size.large
                ),
                onClick = {
                    uiAction(UiEvents.ButtonClicked)
                }
            ) {
                Text(text = "ERC20 TOKENS")
            }
        }
    }
}
