package com.example.appquota

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appquota.ui.theme.AppQuotaTheme

const val USER_PREFERENCES_NAME = "user_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)
val BLOCKED_APP = stringPreferencesKey("")
suspend fun setBlockedApp(appName: String) {
    val context = MainActivity.getAppContext()
    context.dataStore.edit {
        // TODO
    }
}
class MainActivity : ComponentActivity() {
    companion object {
        private var instance: MainActivity? = null

        fun getAppContext(): Context {
            return instance!!.applicationContext
        }
        fun getApps(): MutableList<ApplicationInfo> {
            // gets all apps including system apps. baal ho for now
            // bunch of different contexts can be used. directly accessing packageManager property uses the Activity context
            val pm = getAppContext().packageManager
            // due to API changes in android 13 (TIRAMISU/ API 33)
            val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L))
            } else {
                pm.getInstalledApplications(0)
            }

//        for (packageInfo in packages) {
//            Log.d(TAG, "Package name:" + packageInfo.packageName)
//        }
            return packages
        }
    }
    init {
        instance = this
    }

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

enum class Screens() {
    Start,
    SelectBlockable
}

@Composable
fun AppQuota(modifier: Modifier = Modifier) {
//    var showAppList by remember {mutableStateOf(false)}

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Start.name) {
        composable(Screens.Start.name) { StartScreen(navController) }
        composable(Screens.SelectBlockable.name) { SelectBlockableAppScreen(navController) }
    }

}


@Preview(showBackground = true)
@Composable
fun AppQuotaPreview() {
    AppQuotaTheme {
    }
}