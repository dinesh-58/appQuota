package com.example.appquota

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
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
val BLOCKED_APP_KEY = stringPreferencesKey("BLOCKED_APP_KEY")
suspend fun setBlockedApp(appLabel: String) {
    val context = MainActivity.getAppContext()
    context.dataStore.edit { preferences ->
        preferences[BLOCKED_APP_KEY] = appLabel
    }
}

class AppNameAndIcon(val packageName: String, val label: String, val icon: Int)
class MainActivity : ComponentActivity() {
    companion object {
        private var instance: MainActivity? = null

        fun getAppContext(): Context {
            return instance!!.applicationContext
        }
        fun getApps(): List<AppNameAndIcon> {
            val pm = getAppContext().packageManager
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

            // different flags need to be passed due to API changes in android 13 (TIRAMISU/ API 33)
            val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.queryIntentActivities(mainIntent, PackageManager.ResolveInfoFlags.of(0L))
            } else {
                pm.queryIntentActivities(mainIntent, 0)
            }

            val appInfoMinimal: List<AppNameAndIcon> = packages.map { app ->
                val packageName = app.activityInfo.packageName
                val resources =  pm.getResourcesForApplication(app.activityInfo.applicationInfo)
                val appName = if (app.activityInfo.labelRes != 0) {
                    resources.getString(app.activityInfo.labelRes)
                } else {
                    app.activityInfo.applicationInfo.loadLabel(pm).toString()
                }
//                TODO might have to use app.packageName for comparing open apps later idk
//                val packageName = resolveInfo.activityInfo.packageName
                val appIcon = app.activityInfo.loadIcon(pm)
                AppNameAndIcon(packageName, appName, app.icon) // this is returned to create a list of type AppNameAndIcon
            }
            return appInfoMinimal
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

enum class Screens {
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