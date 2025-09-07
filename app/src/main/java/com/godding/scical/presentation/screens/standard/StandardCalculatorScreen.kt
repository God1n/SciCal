package com.godding.scical.presentation.screens.standard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.godding.scical.presentation.ui.components.CalculatorDisplay
import com.godding.scical.presentation.ui.components.CalculatorButtonGrid

@Composable
fun StandardCalculatorScreen(
    viewModel: StandardCalculatorViewModel = viewModel()
) {
    val formula by viewModel.formula
    val result by viewModel.result
    
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display section - takes up 40% of available space
            CalculatorDisplay(
                formula = formula,
                result = result,
                modifier = Modifier.weight(0.4f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Button section - takes up remaining space
            CalculatorButtonGrid(
                modifier = Modifier.weight(0.6f),
                onButtonClick = { label ->
                    when (label) {
                        in "0".."9" -> viewModel.onNumberClick(label)
                        "." -> viewModel.onDecimalClick()
                        "+", "-", "×", "÷" -> viewModel.onOperatorClick(label)
                        "=" -> viewModel.onEqualsClick()
                        "C" -> viewModel.onClear()
                        "CE" -> viewModel.onClearEntry()
                        "⌫" -> viewModel.onBackspace()
                    }
                }
            )
        }
    }
}