package com.godding.scical.presentation.screens.standard

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class StandardCalculatorViewModel : ViewModel() {
    // Example state: current input
    private val _input = mutableStateOf("")
    val input: State<String> = _input

    fun onNumberClick(number: String) {
        _input.value += number
    }

    fun onClear() {
        _input.value = ""
    }
}

