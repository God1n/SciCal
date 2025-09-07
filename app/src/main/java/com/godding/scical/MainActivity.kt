package com.godding.scical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.godding.scical.presentation.MainScreen
import com.godding.scical.ui.theme.SciCalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SciCalTheme {
                MainScreen()
            }
        }
    }
}
