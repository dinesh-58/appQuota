package com.example.appquota

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appquota.ui.theme.AppQuotaTheme

class AppBlockedActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val remainingSeconds = intent.getLongExtra("remaining_seconds", 0)
//        TODO this value isn't being sent properly & keeps using default 0

        setContent {
            AppQuotaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppBlockedScreen()
//                    TODO maybe conditionally call this above composable if block_time_finished boolean is false?
                }
            }
        }

//        if (intent.getBooleanExtra("block_time_finished", false)) {
//            Toast.makeText(this, "Blocked time is finished", Toast.LENGTH_SHORT).show()
////            hmmm this toast triggered properly. need to implement the starting time limit so that block screen isn't shown constantly
//            finish()
//        }
    }
}

@Composable
fun AppBlockedScreen(modifier: Modifier = Modifier) {
    var countdownTime by remember { mutableStateOf(10) }

    DisposableEffect(Unit) {
        val timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countdownTime = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                countdownTime = 0
//                TODO now I need to finish this activity. either declare countdownTimer in Activity class body & finish there
//                or find a way to finish here
            }
        }
        timer.start()

        onDispose {
            timer.cancel()
        }
    }
//    val remainingTimeState = rememberUpdatedState(remainingSeconds)
    Text("You can't access this app for $countdownTime more seconds")
}

@Preview
@Composable
fun AppBlockedScreenPreview() {
//    AppBlockedScreen(modifier = Modifier.fillMaxSize())
}