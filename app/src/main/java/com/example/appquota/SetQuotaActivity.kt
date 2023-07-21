package com.example.appquota

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.appquota.ui.theme.AppQuotaTheme


class SetQuotaActivity: ComponentActivity() {
    companion object {
        var currentQuotaMillis: Long = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppQuotaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetQuotaScreen()
                }
            }
        }
    }
}

@Composable
fun SetQuotaScreen(modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableStateOf(0) }
    val activity = (LocalContext.current as? Activity)

    Column() {
        Text("How long do you want to use this app?")
        Text(text = sliderPosition.toString() + " minutes")

        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = {sliderPosition = it.toInt()},
            valueRange = 0f..60f,
            steps = 60
        )
        Button(onClick = {
            SetQuotaActivity.currentQuotaMillis = sliderPosition.toLong() * 60 * 1000
            firstLaunch = false
            activity?.finish()
        }) {
            Text("Confirm")
        }
    }
}