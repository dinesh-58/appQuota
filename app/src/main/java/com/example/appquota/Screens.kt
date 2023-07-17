package com.example.appquota

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.navigation.NavController
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlinx.coroutines.launch


const val TAG = "DataStoreExample"
suspend fun logBlockedAppPreference() {
    MainActivity.getAppContext().dataStore.data
        .map { preferences ->
            preferences[BLOCKED_APP_KEY] ?: "Default Value if Blocked App is not set"
        }
//            hmmm commenting out the map function would cause previous value of preference to be shown as well.
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
            } else {
                throw exception
            }
        }
        .collect { blockedApp ->
            Log.d(TAG, "Blocked App Preference: $blockedApp")
        }
}
@Composable
fun StartScreen(navController: NavController) {
    var triggerRecomposition by remember{ mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.Center) {
        Button(onClick = { navController.navigate(Screens.SelectBlockable.name) }) {
            Text("Select an app to block")
        }
    }
//    TODO gonna make a button to check current blocked app
    LaunchedEffect(triggerRecomposition) {
        logBlockedAppPreference()
    }
    DisposableEffect(Unit) {
        // The DisposableEffect will be called when the composable becomes active.
        // Use it to trigger the recomposition when the screen is entered.
        triggerRecomposition = !triggerRecomposition
        onDispose { }// DisposableEffect expects an onDispose even if it is empty
    }
}

@Composable
fun SelectBlockableAppScreen(navController: NavController) {
    val appList = MainActivity.getApps()
    val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column {
        LazyColumn(Modifier
            .selectableGroup()
            .fillMaxHeight(0.8f)
        ) {
            items(appList) { app ->
//            TODO change to checkbox & add a confirm button (onclick = writes & navigates)
                Row(modifier = Modifier
                    .selectable(
                        selected = (app.packageName == selectedOption),
                        role = Role.RadioButton
                    ) { onOptionSelected(app.packageName) }
                ) {
                    RadioButton(
                        selected = (app.packageName == selectedOption),
                        onClick = null //null recommended for accessibility with screenreaders
                    )
//                app.icon // TODO prob need to display this too as drawable or something
                    Text(
                        text = app.label
                    )
                }
            }
        }
        Button(onClick = {
//            TODO if navigation happens before app is set, return a boolean so that navigation only happens after
            coroutineScope.launch { setBlockedApp(selectedOption) }
            navController.navigateUp()
        }) {
            Text("Confirm")
        }
    }
}
