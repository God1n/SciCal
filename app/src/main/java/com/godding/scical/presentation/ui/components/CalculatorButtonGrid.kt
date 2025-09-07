package com.godding.scical.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorButtonGrid(
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    data class ButtonData(val label: String, val type: ButtonType, val span: Int = 1)
    
    val buttonRows = listOf(
        // Row 1: Clear, Clear Entry, Division, Multiplication, Backspace
        listOf(
            ButtonData("C", ButtonType.Action),
            ButtonData("÷", ButtonType.Operator),
            ButtonData("×", ButtonType.Operator),
            ButtonData("⌫", ButtonType.Special)
        ),
        // Row 2: 7, 8, 9, Subtraction
        listOf(
            ButtonData("7", ButtonType.Number),
            ButtonData("8", ButtonType.Number),
            ButtonData("9", ButtonType.Number),
            ButtonData("-", ButtonType.Operator)
        ),
        // Row 3: 4, 5, 6, Addition
        listOf(
            ButtonData("4", ButtonType.Number),
            ButtonData("5", ButtonType.Number),
            ButtonData("6", ButtonType.Number),
            ButtonData("+", ButtonType.Operator)
        ),
        // Row 4: 1, 2, 3, Equals
        listOf(
            ButtonData("1", ButtonType.Number),
            ButtonData("2", ButtonType.Number),
            ButtonData("3", ButtonType.Number),
            ButtonData("=", ButtonType.Special)
        ),
        // Row 5: 0 (double width), Decimal point, Clear Entry
        listOf(
            ButtonData("0", ButtonType.Number, span = 2),
            ButtonData(".", ButtonType.Number),
            ButtonData("CE", ButtonType.Action)
        )
    )
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        buttonRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { buttonData ->
                    CalculatorButton(
                        label = buttonData.label,
                        type = buttonData.type,
                        modifier = Modifier
                            .weight(buttonData.span.toFloat())
                            .padding(horizontal = 2.dp),
                        onClick = { onButtonClick(buttonData.label) }
                    )
                }
                
                // Add spacer for rows with fewer than maximum buttons
                val maxButtons = 4
                val currentButtons = row.sumOf { it.span }
                if (currentButtons < maxButtons) {
                    Spacer(modifier = Modifier.weight((maxButtons - currentButtons).toFloat()))
                }
            }
        }
    }
}