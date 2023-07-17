package com.example.appquota

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun StartScreen(navController: NavController) {
    Column(verticalArrangement = Arrangement.Center) {
        Button(onClick = { navController.navigate(Screens.SelectBlockable.name) }) {
            Text("Select an app to block")
        }
    }
//    TODO display current blocked app here?
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
