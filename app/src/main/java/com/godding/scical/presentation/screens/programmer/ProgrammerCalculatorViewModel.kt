package com.godding.scical.presentation.screens.programmer

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class ProgrammerCalculatorViewModel : ViewModel() {
    private val _formula = mutableStateOf("")
    val formula: State<String> = _formula
    
    private val _result = mutableStateOf("")
    val result: State<String> = _result
    
    private val _binaryResult = mutableStateOf("")
    val binaryResult: State<String> = _binaryResult
    
    private val _hexResult = mutableStateOf("")
    val hexResult: State<String> = _hexResult
    
    private val _octalResult = mutableStateOf("")
    val octalResult: State<String> = _octalResult

    private var operand1: String = ""
    private var operand2: String = ""
    private var operator: String? = null
    private var calculatedResult: String? = null
    private var lastInputWasOperator = false
    private var currentBase = mutableStateOf(NumberBase.DECIMAL)
    val numberBase: State<NumberBase> = currentBase

    enum class NumberBase(val value: Int, val displayName: String) {
        BINARY(2, "BIN"),
        OCTAL(8, "OCT"),
        DECIMAL(10, "DEC"),
        HEXADECIMAL(16, "HEX")
    }

    fun onNumberClick(number: String) {
        if (calculatedResult != null) {
            clearAll()
        }
        
        // Validate input based on current base
        if (!isValidInputForBase(number, currentBase.value)) {
            return
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
        updateAllBaseRepresentations()
    }

    fun onOperatorClick(op: String) {
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
            updateAllBaseRepresentations()
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
        updateAllBaseRepresentations()
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
        updateAllBaseRepresentations()
    }

    fun onBitwiseOperation(operation: String) {
        if (operand1.isEmpty()) return
        
        val decimalValue = convertToDecimal(operand1, currentBase.value)
        if (decimalValue == null) return
        
        val result = when (operation) {
            "NOT" -> decimalValue.toLong().inv()
            else -> return
        }
        
        val formattedResult = convertFromDecimal(result, currentBase.value)
        operand1 = formattedResult
        _result.value = operand1
        _formula.value = "$operation($operand1)"
        updateAllBaseRepresentations()
    }

    fun switchNumberBase(base: NumberBase) {
        if (currentBase.value == base) return
        
        // Convert current value to new base
        val currentDecimalValue = getCurrentDecimalValue()
        if (currentDecimalValue != null) {
            val newValue = convertFromDecimal(currentDecimalValue, base)
            
            if (operator == null) {
                operand1 = newValue
                _result.value = operand1
            } else if (operand2.isNotEmpty()) {
                operand2 = newValue
                _result.value = operand2
                _formula.value = operand1 + " " + operator + " " + operand2
            }
        }
        
        currentBase.value = base
        updateAllBaseRepresentations()
    }

    private fun clearAll() {
        operand1 = ""
        operand2 = ""
        operator = null
        calculatedResult = null
        _formula.value = ""
        _result.value = ""
        _binaryResult.value = ""
        _hexResult.value = ""
        _octalResult.value = ""
        lastInputWasOperator = false
    }

    private fun calculateResult() {
        val dec1 = convertToDecimal(operand1, currentBase.value)
        val dec2 = convertToDecimal(operand2, currentBase.value)
        
        if (dec1 == null || dec2 == null) {
            calculatedResult = operand1
            return
        }
        
        val result = when (operator) {
            "+" -> dec1 + dec2
            "-" -> dec1 - dec2
            "×" -> dec1 * dec2
            "÷" -> if (dec2 == 0L) {
                calculatedResult = "Error"
                return
            } else dec1 / dec2
            "AND" -> dec1 and dec2
            "OR" -> dec1 or dec2
            "XOR" -> dec1 xor dec2
            "<<" -> dec1 shl dec2.toInt()
            ">>" -> dec1 shr dec2.toInt()
            else -> {
                calculatedResult = operand1
                return
            }
        }
        
        calculatedResult = convertFromDecimal(result, currentBase.value)
    }

    private fun updateAllBaseRepresentations() {
        val decimalValue = getCurrentDecimalValue()
        if (decimalValue != null) {
            _binaryResult.value = convertFromDecimal(decimalValue, NumberBase.BINARY)
            _hexResult.value = convertFromDecimal(decimalValue, NumberBase.HEXADECIMAL)
            _octalResult.value = convertFromDecimal(decimalValue, NumberBase.OCTAL)
        } else {
            _binaryResult.value = ""
            _hexResult.value = ""
            _octalResult.value = ""
        }
    }

    private fun getCurrentDecimalValue(): Long? {
        val currentValue = if (operator == null || operand2.isEmpty()) {
            operand1
        } else {
            operand2
        }
        return convertToDecimal(currentValue, currentBase.value)
    }

    private fun convertToDecimal(value: String, fromBase: NumberBase): Long? {
        if (value.isEmpty()) return null
        return try {
            when (fromBase) {
                NumberBase.BINARY -> value.toLong(2)
                NumberBase.OCTAL -> value.toLong(8)
                NumberBase.DECIMAL -> value.toLong(10)
                NumberBase.HEXADECIMAL -> value.toLong(16)
            }
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun convertFromDecimal(value: Long, toBase: NumberBase): String {
        return when (toBase) {
            NumberBase.BINARY -> value.toString(2).uppercase()
            NumberBase.OCTAL -> value.toString(8).uppercase()
            NumberBase.DECIMAL -> value.toString()
            NumberBase.HEXADECIMAL -> value.toString(16).uppercase()
        }
    }

    private fun isValidInputForBase(input: String, base: NumberBase): Boolean {
        return when (base) {
            NumberBase.BINARY -> input.all { it in "01" }
            NumberBase.OCTAL -> input.all { it in "01234567" }
            NumberBase.DECIMAL -> input.all { it.isDigit() }
            NumberBase.HEXADECIMAL -> input.all { it in "0123456789ABCDEFabcdef" }
        }
    }
}