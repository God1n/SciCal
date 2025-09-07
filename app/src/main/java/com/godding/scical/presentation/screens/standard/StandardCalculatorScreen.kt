package com.godding.scical.presentation.screens.standard

import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StandardCalculatorScreen(
    viewModel: StandardCalculatorViewModel = viewModel()
) {
    val input by viewModel.input
    Surface(modifier = Modifier) {
        Column {
            Text("Input: $input")
            Button(onClick = { viewModel.onNumberClick("1") }) {
                Text("1")
            }
            Button(onClick = { viewModel.onClear() }) {
                Text("Clear")
            }
        }
    }
}
