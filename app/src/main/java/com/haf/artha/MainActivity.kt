package com.haf.artha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.haf.artha.presentation.onboarding.setUsername.SetUsername
import com.haf.artha.ui.theme.ArthaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            installSplashScreen()
            ArthaTheme {
                // A surface container using the 'background' color from the theme'
                SetUsername()
            }
        }
    }
}
