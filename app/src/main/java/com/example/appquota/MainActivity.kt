package com.example.appquota

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import android.os.Build
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appquota.ui.theme.AppQuotaTheme

//class MainActivity : AppCompatActivity() {
// if you get installed apps in MainActivity, it needs to extend AppCompatActivity()
// val context: Context = this  // might need to initialize context like this. idk
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppQuotaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppQuota()
                }
            }
        }
    }

}

@Composable
fun AppQuota(modifier: Modifier = Modifier) {

    // gets all apps including system apps. baal ho for now
    // this should prob go in MainActivity
    val context = LocalContext.current
    val pm = context.packageManager
    val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L))
    } else {
        pm.getInstalledApplications(0)
    }

    for (packageInfo in packages) {
        Log.d(TAG, "Package name:" + packageInfo.packageName)
    }
    Text("Test")
}

@Preview(showBackground = true)
@Composable
fun AppQuotaPreview() {
    AppQuotaTheme {
    }
}