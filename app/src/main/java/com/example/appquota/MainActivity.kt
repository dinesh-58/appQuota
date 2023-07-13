package com.example.appquota

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appquota.ui.theme.AppQuotaTheme

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

    fun getApps(): MutableList<ApplicationInfo> {
        // gets all apps including system apps. baal ho for now
        // bunch of different contexts can be used. directly accessing packageManager property uses the Activity context
        val pm = packageManager
        // due to API changes in android 13 (TIRAMISU/ API 33),
        val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L))
        } else {
            pm.getInstalledApplications(0)
        }

        for (packageInfo in packages) {
            Log.d(TAG, "Package name:" + packageInfo.packageName)
        }
        return packages
    }
}

@Composable
fun AppQuota(modifier: Modifier = Modifier) {
    Text("Test")
}

@Preview(showBackground = true)
@Composable
fun AppQuotaPreview() {
    AppQuotaTheme {
    }
}