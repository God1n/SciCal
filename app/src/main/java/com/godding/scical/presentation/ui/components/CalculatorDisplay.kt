package com.godding.scical.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorDisplay(
    formula: String,
    result: String,
    modifier: Modifier = Modifier
) {
    val resultScrollState = rememberScrollState()
    val formulaScrollState = rememberScrollState()
    
    // Auto-scroll to end when text changes
    LaunchedEffect(result) {
        if (result.isNotEmpty()) {
            resultScrollState.animateScrollTo(resultScrollState.maxValue)
        }
    }
    
    LaunchedEffect(formula) {
        if (formula.isNotEmpty()) {
            formulaScrollState.animateScrollTo(formulaScrollState.maxValue)
        }
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Formula section - always visible area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (formula.isNotEmpty()) {
                    Text(
                        text = formula,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.horizontalScroll(formulaScrollState),
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        textAlign = TextAlign.End
                    )
                }
            }
            
            // Main result section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = result.ifEmpty { "0" },
                    fontSize = when {
                        result.length <= 4 -> 64.sp
                        result.length <= 8 -> 48.sp
                        result.length <= 12 -> 36.sp
                        result.length <= 16 -> 28.sp
                        else -> 22.sp
                    },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.horizontalScroll(resultScrollState),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}