package com.example.weightbridge.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun HomeView(
    onInputClicked: () -> Unit,
    onPreviewClicked: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
        Column(modifier = Modifier.padding(start = Dp(8f), top = Dp(30f), end = Dp(8f))) {
            Button(onClick = onInputClicked, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Input New Data")
            }
            Button(onClick = onPreviewClicked, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Show Detail Data")
            }
        }
    }
}