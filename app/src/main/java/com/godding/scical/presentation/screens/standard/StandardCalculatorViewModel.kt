package com.godding.scical.presentation.screens.standard

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class StandardCalculatorViewModel : ViewModel() {
    private val _formula = mutableStateOf("")
    val formula: State<String> = _formula
    
    private val _result = mutableStateOf("")
    val result: State<String> = _result

    private var operand1: String = ""
    private var operand2: String = ""
    private var operator: String? = null
    private var calculatedResult: String? = null
    private var lastInputWasOperator = false

    fun onNumberClick(number: String) {
        if (calculatedResult != null) {
            clearAll()
        }
        if (operator == null) {
            operand1 += number
            _result.value = operand1
            _formula.value = ""
        } else {
            operand2 += number
            _formula.value = operand1 + " " + operator + " " + operand2
            _result.value = operand2
        }
        lastInputWasOperator = false
    }

    fun onOperatorClick(op: String) {
        if (operand1.isEmpty() && op == "-") {
            operand1 = "-"
            _result.value = operand1
            _formula.value = ""
            return
        }
        if (operand1.isEmpty()) return
        if (operator != null && operand2.isNotEmpty()) {
            calculateResult()
            operand1 = calculatedResult ?: operand1
            operand2 = ""
            calculatedResult = null
        }
        operator = op
        _formula.value = operand1 + " " + operator
        _result.value = operand1
        lastInputWasOperator = true
    }

    fun onDecimalClick() {
        if (operator == null) {
            if (!operand1.contains(".")) {
                operand1 = if (operand1.isEmpty()) "0." else operand1 + "."
                _result.value = operand1
                _formula.value = ""
            }
        } else {
            if (!operand2.contains(".")) {
                operand2 = if (operand2.isEmpty()) "0." else operand2 + "."
                _formula.value = operand1 + " " + operator + " " + operand2
                _result.value = operand2
            }
        }
    }

    fun onEqualsClick() {
        if (operand1.isNotEmpty() && operator != null && operand2.isNotEmpty()) {
            val fullFormula = operand1 + " " + operator + " " + operand2
            calculateResult()
            _formula.value = fullFormula + " ="
            _result.value = calculatedResult ?: operand1
            operand1 = calculatedResult ?: operand1
            operand2 = ""
            operator = null
            calculatedResult = null
        }
    }

    fun onBackspace() {
        if (calculatedResult != null) {
            clearAll()
            return
        }
        if (operator == null) {
            if (operand1.isNotEmpty()) {
                operand1 = operand1.dropLast(1)
                _result.value = operand1
                _formula.value = ""
            }
        } else if (operand2.isNotEmpty()) {
            operand2 = operand2.dropLast(1)
            _formula.value = operand1 + " " + operator + if (operand2.isNotEmpty()) " " + operand2 else ""
            _result.value = if (operand2.isNotEmpty()) operand2 else operand1
        } else if (operator != null) {
            operator = null
            _formula.value = ""
            _result.value = operand1
        }
    }

    fun onClear() {
        clearAll()
    }
    
    fun onClearEntry() {
        if (operator == null) {
            operand1 = ""
            _result.value = ""
        } else {
            operand2 = ""
            _result.value = operand1
            _formula.value = operand1 + " " + operator
        }
    }

    private fun clearAll() {
        operand1 = ""
        operand2 = ""
        operator = null
        calculatedResult = null
        _formula.value = ""
        _result.value = ""
        lastInputWasOperator = false
    }

    private fun calculateResult() {
        val op1 = operand1.toDoubleOrNull()
        val op2 = operand2.toDoubleOrNull()
        if (op1 == null || op2 == null) {
            calculatedResult = operand1
            return
        }
        val result = when (operator) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "×" -> op1 * op2
            "÷" -> if (op2 == 0.0) {
                calculatedResult = "Error"
                return
            } else op1 / op2
            else -> {
                calculatedResult = operand1
                return
            }
        }
        
        // Format result to remove unnecessary decimal places
        calculatedResult = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            String.format("%.10f", result).trimEnd('0').trimEnd('.')
        }
    }
}
