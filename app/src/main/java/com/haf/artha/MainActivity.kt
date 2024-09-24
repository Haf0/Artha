package com.haf.artha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.haf.artha.presentation.navigation.NavigationBuilder
import com.haf.artha.ui.theme.ArthaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            installSplashScreen()
            ArthaTheme {
                NavigationBuilder()
            }
        }
    }
}
