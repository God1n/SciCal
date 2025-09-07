package com.godding.scical.presentation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.godding.scical.presentation.screens.programmer.ProgrammerCalculatorScreen
import com.godding.scical.presentation.screens.scientific.ScientificCalculatorScreen
import com.godding.scical.presentation.screens.standard.StandardCalculatorScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String) {
    object Standard : Screen("standard", "Standard Calculator")
    object Scientific : Screen("scientific", "Scientific Calculator")
    object Programmer : Screen("programmer", "Programmer Calculator")
}

val drawerScreens = listOf(
    Screen.Standard,
    Screen.Scientific,
    Screen.Programmer
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                drawerScreens.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(screen.title) },
                        selected = false,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Standard.route
        ) {
            composable(Screen.Standard.route) { StandardCalculatorScreen() }
            composable(Screen.Scientific.route) { ScientificCalculatorScreen() }
            composable(Screen.Programmer.route) { ProgrammerCalculatorScreen() }
        }
    }
}
