package com.distlog.reconciliation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.distlog.reconciliation.ui.screens.*
import com.distlog.reconciliation.ui.theme.DistLogTheme
import com.distlog.reconciliation.ui.theme.NeonBlue
import com.distlog.reconciliation.viewmodel.DashboardViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DistLogTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: DashboardViewModel = viewModel()
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri -> viewModel.handleFileUpload(uri) }
    )

    LaunchedEffect(Unit) {
        viewModel.eventMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBars = currentRoute != "splash" && currentRoute != "login"

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBars,
        drawerContent = {
            if (showBars) {
                ModalDrawerSheet(
                    drawerContainerColor = com.distlog.reconciliation.ui.theme.CardBackground
                ) {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "DISTLOG",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        color = NeonBlue
                    )
                    Divider(color = Color.Gray.copy(alpha = 0.2f))
                    
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Dashboard, null) },
                        label = { Text("Dashboard") },
                        selected = currentRoute == "dashboard",
                        onClick = {
                            navController.navigate("dashboard")
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonBlue.copy(alpha = 0.1f),
                            selectedTextColor = NeonBlue,
                            selectedIconColor = NeonBlue
                        )
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Timeline, null) },
                        label = { Text("Timeline") },
                        selected = currentRoute == "timeline",
                        onClick = {
                            navController.navigate("timeline")
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonBlue.copy(alpha = 0.1f),
                            selectedTextColor = NeonBlue,
                            selectedIconColor = NeonBlue
                        )
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Info, null) },
                        label = { Text("About System") },
                        selected = currentRoute == "info",
                        onClick = {
                            navController.navigate("info")
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonBlue.copy(alpha = 0.1f),
                            selectedTextColor = NeonBlue,
                            selectedIconColor = NeonBlue
                        )
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Settings, null) },
                        label = { Text("Settings") },
                        selected = currentRoute == "settings",
                        onClick = {
                            navController.navigate("settings")
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonBlue.copy(alpha = 0.1f),
                            selectedTextColor = NeonBlue,
                            selectedIconColor = NeonBlue
                        )
                    )
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                if (showBars) {
                    CenterAlignedTopAppBar(
                        title = { Text("DISTLOG", style = MaterialTheme.typography.titleLarge, color = NeonBlue) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, "Menu", tint = NeonBlue)
                            }
                        },
                        actions = {
                            IconButton(onClick = { viewModel.refreshData() }) {
                                Icon(Icons.Default.Sync, "Sync", tint = NeonBlue)
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = com.distlog.reconciliation.ui.theme.DarkBackground
                        )
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("splash") { SplashScreen { navController.navigate("login") { popUpTo("splash") { inclusive = true } } } }
                composable("login") { LoginScreen { navController.navigate("dashboard") { popUpTo("login") { inclusive = true } } } }
                composable("dashboard") { DashboardScreen(viewModel) { filePickerLauncher.launch(arrayOf("application/json")) } }
                composable("timeline") { TimelineScreen(viewModel) }
                composable("info") { InfoScreen() }
                composable("settings") { SettingsScreen() }
            }
        }
    }
}
