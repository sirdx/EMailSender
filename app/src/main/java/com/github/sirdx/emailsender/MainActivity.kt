package com.github.sirdx.emailsender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.sirdx.emailsender.presentation.navigation.Screen
import com.github.sirdx.emailsender.presentation.screens.history.HistoryScreen
import com.github.sirdx.emailsender.presentation.screens.send.SendScreen
import com.github.sirdx.emailsender.presentation.ui.theme.EMailSenderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screens = listOf(
            Screen.Send,
            Screen.History
        )

        setContent {
            EMailSenderTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            screens.forEach { screen ->
                                NavigationBarItem(
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    label = { Text(text = stringResource(id = screen.resourceId)) },
                                    icon = { Icon(screen.icon, contentDescription = null) },
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }

                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.Send.route
                    ) {
                        composable(Screen.Send.route) {
                            SendScreen(navController = navController)
                        }
                        composable(Screen.History.route) {
                            HistoryScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}