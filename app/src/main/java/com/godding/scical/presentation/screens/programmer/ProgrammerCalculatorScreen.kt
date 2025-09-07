package com.godding.scical.presentation.screens.programmer

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.godding.scical.presentation.ui.components.CalculatorDisplay
import com.godding.scical.presentation.ui.components.CalculatorButton
import com.godding.scical.presentation.ui.components.CompactCalculatorButton
import com.godding.scical.presentation.ui.components.MiniCalculatorButton
import com.godding.scical.presentation.ui.components.ButtonType

@Composable
fun ProgrammerCalculatorScreen(
    viewModel: ProgrammerCalculatorViewModel = viewModel()
) {
    val formula by viewModel.formula
    val result by viewModel.result
    val binaryResult by viewModel.binaryResult
    val hexResult by viewModel.hexResult
    val octalResult by viewModel.octalResult
    val currentBase by viewModel.numberBase
    
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
            // Display section
            CalculatorDisplay(
                formula = formula,
                result = result,
                modifier = Modifier.weight(0.25f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Number base representations - more compact
            BaseRepresentationCard(
                binaryResult = binaryResult,
                hexResult = hexResult,
                octalResult = octalResult,
                decimalResult = result,
                currentBase = currentBase,
                onBaseClick = { base -> viewModel.switchNumberBase(base) }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Button section - optimized space
            ProgrammerButtonGrid(
                modifier = Modifier.weight(0.68f),
                currentBase = currentBase,
                onButtonClick = { label ->
                    when (label) {
                        in "0".."9", "A", "B", "C", "D", "E", "F" -> viewModel.onNumberClick(label)
                        "+", "-", "×", "÷", "AND", "OR", "XOR", "<<", ">>" -> viewModel.onOperatorClick(label)
                        "=" -> viewModel.onEqualsClick()
                        "C" -> viewModel.onClear()
                        "CE" -> viewModel.onClearEntry()
                        "⌫" -> viewModel.onBackspace()
                        "NOT" -> viewModel.onBitwiseOperation(label)
                        "BIN" -> viewModel.switchNumberBase(ProgrammerCalculatorViewModel.NumberBase.BINARY)
                        "OCT" -> viewModel.switchNumberBase(ProgrammerCalculatorViewModel.NumberBase.OCTAL)
                        "DEC" -> viewModel.switchNumberBase(ProgrammerCalculatorViewModel.NumberBase.DECIMAL)
                        "HEX" -> viewModel.switchNumberBase(ProgrammerCalculatorViewModel.NumberBase.HEXADECIMAL)
                    }
                }
            )
        }
    }
}

@Composable
private fun BaseRepresentationCard(
    binaryResult: String,
    hexResult: String,
    octalResult: String,
    decimalResult: String,
    currentBase: ProgrammerCalculatorViewModel.NumberBase,
    onBaseClick: (ProgrammerCalculatorViewModel.NumberBase) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BaseRow("HEX", hexResult, currentBase == ProgrammerCalculatorViewModel.NumberBase.HEXADECIMAL) {
                onBaseClick(ProgrammerCalculatorViewModel.NumberBase.HEXADECIMAL)
            }
            BaseRow("DEC", decimalResult.ifEmpty { "0" }, currentBase == ProgrammerCalculatorViewModel.NumberBase.DECIMAL) {
                onBaseClick(ProgrammerCalculatorViewModel.NumberBase.DECIMAL)
            }
            BaseRow("OCT", octalResult, currentBase == ProgrammerCalculatorViewModel.NumberBase.OCTAL) {
                onBaseClick(ProgrammerCalculatorViewModel.NumberBase.OCTAL)
            }
            BaseRow("BIN", binaryResult, currentBase == ProgrammerCalculatorViewModel.NumberBase.BINARY) {
                onBaseClick(ProgrammerCalculatorViewModel.NumberBase.BINARY)
            }
        }
    }
}

@Composable
private fun BaseRow(
    label: String,
    value: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MiniCalculatorButton(
            label = label,
            type = if (isSelected) ButtonType.Operator else ButtonType.Special,
            modifier = Modifier.weight(0.22f),
            onClick = onClick
        )
        
        Text(
            text = value.ifEmpty { "0" },
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(0.78f)
                .padding(start = 8.dp)
        )
    }
}

@Composable
private fun ProgrammerButtonGrid(
    currentBase: ProgrammerCalculatorViewModel.NumberBase,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    data class ButtonData(val label: String, val type: ButtonType, val span: Int = 1, val enabled: Boolean = true)
    
    val buttonRows = listOf(
        // Row 1: Bitwise operations
        listOf(
            ButtonData("AND", ButtonType.Operator),
            ButtonData("OR", ButtonType.Operator),
            ButtonData("XOR", ButtonType.Operator),
            ButtonData("NOT", ButtonType.Special),
            ButtonData("C", ButtonType.Action)
        ),
        // Row 2: Shift operations
        listOf(
            ButtonData("<<", ButtonType.Operator),
            ButtonData(">>", ButtonType.Operator),
            ButtonData("÷", ButtonType.Operator),
            ButtonData("×", ButtonType.Operator),
            ButtonData("⌫", ButtonType.Special)
        ),
        // Row 3: Hex digits A-F and operators
        listOf(
            ButtonData("A", ButtonType.Number, enabled = currentBase.value >= 16),
            ButtonData("B", ButtonType.Number, enabled = currentBase.value >= 16),
            ButtonData("C", ButtonType.Number, enabled = currentBase.value >= 16),
            ButtonData("D", ButtonType.Number, enabled = currentBase.value >= 16),
            ButtonData("-", ButtonType.Operator)
        ),
        // Row 4: Hex digits E-F and numbers
        listOf(
            ButtonData("E", ButtonType.Number, enabled = currentBase.value >= 16),
            ButtonData("F", ButtonType.Number, enabled = currentBase.value >= 16),
            ButtonData("7", ButtonType.Number, enabled = currentBase.value >= 8),
            ButtonData("8", ButtonType.Number, enabled = currentBase.value >= 10),
            ButtonData("+", ButtonType.Operator)
        ),
        // Row 5: Numbers
        listOf(
            ButtonData("9", ButtonType.Number, enabled = currentBase.value >= 10),
            ButtonData("6", ButtonType.Number, enabled = currentBase.value >= 8),
            ButtonData("5", ButtonType.Number, enabled = currentBase.value >= 8),
            ButtonData("4", ButtonType.Number, enabled = currentBase.value >= 8),
            ButtonData("=", ButtonType.Special)
        ),
        // Row 6: Numbers
        listOf(
            ButtonData("3", ButtonType.Number, enabled = currentBase.value >= 8),
            ButtonData("2", ButtonType.Number, enabled = currentBase.value >= 8),
            ButtonData("1", ButtonType.Number),
            ButtonData("0", ButtonType.Number),
            ButtonData("CE", ButtonType.Action)
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
                        type = if (buttonData.enabled) buttonData.type else ButtonType.Number,
                        modifier = Modifier
                            .weight(buttonData.span.toFloat()),
                        onClick = { 
                            if (buttonData.enabled) {
                                onButtonClick(buttonData.label)
                            }
                        }
                    )
                }
                
                // Add spacer for rows with fewer than maximum buttons
                val maxButtons = 5
                val currentButtons = row.sumOf { it.span }
                if (currentButtons < maxButtons) {
                    Spacer(modifier = Modifier.weight((maxButtons - currentButtons).toFloat()))
                }
            }
        }
    }
}
