package com.godding.scical.presentation.screens.scientific

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.godding.scical.presentation.ui.components.CalculatorDisplay
import com.godding.scical.presentation.ui.components.CompactCalculatorButton
import com.godding.scical.presentation.ui.components.ButtonType

@Composable
fun ScientificCalculatorScreen(
    viewModel: ScientificCalculatorViewModel = viewModel()
) {
    val formula by viewModel.formula
    val result by viewModel.result
    val isDegreeMode by viewModel.isDegreeMode
    
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
                .padding(16.dp)
        ) {
            CalculatorDisplay(
                formula = formula,
                result = result,
                modifier = Modifier.weight(0.28f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RAD",
                    fontSize = 14.sp,
                    color = if (!isDegreeMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = isDegreeMode,
                    onCheckedChange = { viewModel.toggleAngleMode() },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Text(
                    text = "DEG",
                    fontSize = 14.sp,
                    color = if (isDegreeMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
            
            ScientificButtonGrid(
                modifier = Modifier.weight(0.72f),
                onButtonClick = { label ->
                    when (label) {
                        in "0".."9" -> viewModel.onNumberClick(label)
                        "." -> viewModel.onDecimalClick()
                        "+", "-", "×", "÷", "^", "mod" -> viewModel.onOperatorClick(label)
                        "=" -> viewModel.onEqualsClick()
                        "C" -> viewModel.onClear()
                        "CE" -> viewModel.onClearEntry()
                        "⌫" -> viewModel.onBackspace()
                        "sin", "cos", "tan", "asin", "acos", "atan",
                        "ln", "log", "√", "x²", "x³", "1/x", "!", "e^x", "10^x" -> {
                            viewModel.onScientificFunction(label)
                        }
                        "π", "e" -> viewModel.onConstantClick(label)
                    }
                }
            )
        }
    }
}

@Composable
private fun ScientificButtonGrid(
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    data class ButtonData(val label: String, val type: ButtonType, val span: Int = 1)
    
    val buttonRows = listOf(
        listOf(
            ButtonData("sin", ButtonType.Special),
            ButtonData("cos", ButtonType.Special),
            ButtonData("tan", ButtonType.Special),
            ButtonData("ln", ButtonType.Special),
            ButtonData("log", ButtonType.Special)
        ),
        listOf(
            ButtonData("asin", ButtonType.Special),
            ButtonData("acos", ButtonType.Special),
            ButtonData("atan", ButtonType.Special),
            ButtonData("√", ButtonType.Special),
            ButtonData("^", ButtonType.Operator)
        ),
        listOf(
            ButtonData("x²", ButtonType.Special),
            ButtonData("x³", ButtonType.Special),
            ButtonData("1/x", ButtonType.Special),
            ButtonData("!", ButtonType.Special),
            ButtonData("mod", ButtonType.Operator)
        ),
        listOf(
            ButtonData("π", ButtonType.Special),
            ButtonData("e", ButtonType.Special),
            ButtonData("e^x", ButtonType.Special),
            ButtonData("10^x", ButtonType.Special),
            ButtonData("C", ButtonType.Action)
        ),
        listOf(
            ButtonData("7", ButtonType.Number),
            ButtonData("8", ButtonType.Number),
            ButtonData("9", ButtonType.Number),
            ButtonData("÷", ButtonType.Operator),
            ButtonData("⌫", ButtonType.Special)
        ),
        listOf(
            ButtonData("4", ButtonType.Number),
            ButtonData("5", ButtonType.Number),
            ButtonData("6", ButtonType.Number),
            ButtonData("×", ButtonType.Operator),
            ButtonData("CE", ButtonType.Action)
        ),
        listOf(
            ButtonData("1", ButtonType.Number),
            ButtonData("2", ButtonType.Number),
            ButtonData("3", ButtonType.Number),
            ButtonData("-", ButtonType.Operator),
            ButtonData("=", ButtonType.Special)
        ),
        listOf(
            ButtonData("0", ButtonType.Number, span = 2),
            ButtonData(".", ButtonType.Number),
            ButtonData("+", ButtonType.Operator, span = 2)
        )
    )
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttonRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { buttonData ->
                    CompactCalculatorButton(
                        label = buttonData.label,
                        type = buttonData.type,
                        modifier = Modifier
                            .weight(buttonData.span.toFloat()),
                        onClick = { onButtonClick(buttonData.label) }
                    )
                }
                
                val maxButtons = 5
                val currentButtons = row.sumOf { it.span }
                if (currentButtons < maxButtons) {
                    Spacer(modifier = Modifier.weight((maxButtons - currentButtons).toFloat()))
                }
            }
        }
    }
}