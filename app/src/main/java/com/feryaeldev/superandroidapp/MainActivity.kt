package com.feryaeldev.superandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.feryaeldev.superandroidapp.ui.theme.SuperAndroidAppTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            SuperAndroidAppTheme {
                MyScaffoldApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffoldApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
//    val context = LocalContext.current.applicationContext

    // Estructura de Scaffold
    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(onItemClicked = {
                scope.launch { drawerState.close() }
                navController.navigate(it)
            })
        },
        drawerState = drawerState,
        gesturesEnabled = true,
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Mi Aplicación") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { /* Acción del FAB */ }) {
                        Icon(Icons.Default.Add, contentDescription = "FAB")
                    }
                }
            ) { innerPadding ->
                // Contenido principal
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("home") { HomeScreen() }
                    composable("search") { SearchScreen() }
                    composable("profile") { ProfileScreen() }
                }
            }
        }
    )
}

@Composable
fun DrawerContent(onItemClicked: (String) -> Unit) {
    LazyColumn {
        item {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .height(150.dp)
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Menu", color = Color.White)
                }
            }
        }
        item { HorizontalDivider() }
        item {
            NavigationDrawerItem(
                label = { Text("Inicio", modifier = Modifier.padding(16.dp)) },
                selected = false,
                onClick = { onItemClicked("home") })
            NavigationDrawerItem(
                label = { Text("Buscar", modifier = Modifier.padding(16.dp)) },
                selected = false,
                onClick = { onItemClicked("search") })
            NavigationDrawerItem(
                label = { Text("Perfil", modifier = Modifier.padding(16.dp)) },
                selected = false,
                onClick = { onItemClicked("profile") })
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Profile
    )

    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// Ejemplo de las pantallas que serán mostradas en el NavHost
@Composable
fun HomeScreen() {
    Text("Pantalla de Inicio")
}

@Composable
fun SearchScreen() {
    Text("Pantalla de Búsqueda")
}

@Composable
fun ProfileScreen() {
    Text("Pantalla de Perfil")
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomNavItem("home", "Inicio", Icons.Default.Home)
    data object Search : BottomNavItem("search", "Buscar", Icons.Default.Search)
    data object Profile : BottomNavItem("profile", "Perfil", Icons.Default.Person)
}

@Preview(showBackground = true)
@Composable
fun PreviewMyScaffoldApp() {
    MyScaffoldApp()
}