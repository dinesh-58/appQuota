package com.example.appquota

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun StartScreen(navController: NavController) {
    Column(verticalArrangement = Arrangement.Center) {
        Button(onClick = { navController.navigate(Screens.SelectBlockable.name) }) {
            Text("Select an app to block")
        }
    }
}

@Composable
fun SelectBlockableAppScreen(navController: NavController) {
// hmmm app select garesi feri StartScreen ma navigate hunu paryo. arguement pathaunu satta
// data store garne
// maybe something like  navController.navigate("StartScreen/com.asdfas.asfdas")
    val appList = MainActivity.getApps()
    LazyColumn {
        items(appList) { app ->
            Text(text = app.packageName)
        }
    }
}
