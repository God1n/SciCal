package com.godding.scical.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.godding.scical.presentation.screens.programmer.ProgrammerCalculatorScreen
import com.godding.scical.presentation.screens.scientific.ScientificCalculatorScreen
import com.godding.scical.presentation.screens.standard.StandardCalculatorScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Standard : Screen("standard", "Standard Calculator", Icons.Filled.AddCircle)
    object Scientific : Screen("scientific", "Scientific Calculator", Icons.Filled.Face)
    object Programmer : Screen("programmer", "Programmer Calculator", Icons.Filled.Info)
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SciCal",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Scientific Calculator",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Navigation Items
                drawerScreens.forEach { screen ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title, fontWeight = FontWeight.Medium) },
                        selected = currentRoute == screen.route,
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
                
                Spacer(modifier = Modifier.height(16.dp))
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
