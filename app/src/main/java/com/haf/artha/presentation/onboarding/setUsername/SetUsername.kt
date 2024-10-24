package com.haf.artha.presentation.onboarding.setUsername

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.R
import com.haf.artha.navigation.Screen
import com.haf.artha.preference.PreferenceViewModel

@Composable
fun SetUsername(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 16.dp),
    ) {
        Column {
            Text(
                text = "Hai, selamat datang di Artha",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily.SansSerif,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_splash),
                contentDescription = "Splash Screen Image",
                modifier = modifier
                    .size(300.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Artha adalah aplikasi yang akan membantu untuk mengelola keuangan pribadi kamu.",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.SansSerif
            )
            InputUsername(modifier = modifier, context = context, navController = navController)
        }


    }
}


@Composable
fun InputUsername(
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavHostController,
    preferenceViewModel: PreferenceViewModel = hiltViewModel()
) {

    var username by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = {newUsername ->
                username = newUsername
            },
            label = { Text("Masukkan nama kamu") },
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            singleLine = true ,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )

        Button(
            onClick = {
                if (username.isNotEmpty()) {
                    preferenceViewModel.setUsername(username)
                    preferenceViewModel.setCurrentStep(1)
                    navController.navigate(Screen.SetAccount.route)
                }else{
                    Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Lanjutkan")
        }
    }


}