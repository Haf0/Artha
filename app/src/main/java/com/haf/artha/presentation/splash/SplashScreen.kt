package com.haf.artha.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.R
import com.haf.artha.navigation.Screen
import com.haf.artha.preference.PreferenceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SplashScreen(
    navController: NavHostController,
    preferenceViewModel: PreferenceViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.ic_splash), contentDescription = "Splash Screen Image", modifier = Modifier.size(300.dp))
        LaunchedEffect(key1 = true) {
            delay(2000)
            preferenceViewModel.checkOnboardingStatus().collectLatest {(isCompleted, step) ->
                if (isCompleted) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                } else {
                    val onboardingSteps = mapOf(
                        0 to Screen.SetUsername,
                        1 to Screen.SetAccount,
                        2 to Screen.SetCategory
                    )

                    onboardingSteps[step]?.let { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(Screen.SplashScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }

        }
    }
}