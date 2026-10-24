package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.ui.screens.AddEditListScreen
import com.example.todolist.ui.screens.AddItemScreen
import com.example.todolist.ui.screens.ListDetailScreen
import com.example.todolist.ui.screens.ListScreen
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.ui.viewmodels.ListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoListTheme {
                val navController = rememberNavController()
                val viewModel: ListViewModel = viewModel()
                NavHost(navController = navController, startDestination = "lists_list") {
                    composable("lists_list") {
                        ListScreen(navController, viewModel)
                    }
                    composable("add_edit_list") {
                        AddEditListScreen(navController, viewModel)
                    }
                    composable(
                        route = "list_detail/{listId}",
                        arguments = listOf(navArgument("listId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val listId = backStackEntry.arguments?.getInt("listId") ?: 0
                        ListDetailScreen(navController, viewModel, listId)
                    }
                    composable(
                        route = "add_item/{listId}",
                        arguments = listOf(navArgument("listId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val listId = backStackEntry.arguments?.getInt("listId") ?: 0
                        AddItemScreen(navController, viewModel, listId)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoListTheme {
        val navController = rememberNavController()
        val viewModel: ListViewModel = viewModel()
        NavHost(navController = navController, startDestination = "lists_list") {
            composable("lists_list") {
                ListScreen(navController, viewModel)
            }
            composable("add_edit_list") {
                AddEditListScreen(navController, viewModel)
            }
            composable(
                route = "list_detail/{listId}",
                arguments = listOf(navArgument("listId") { type = NavType.IntType })
            ) { backStackEntry ->
                val listId = backStackEntry.arguments?.getInt("listId") ?: 0
                ListDetailScreen(navController, viewModel, listId)
            }
            composable(
                route = "add_item/{listId}",
                arguments = listOf(navArgument("listId") { type = NavType.IntType })
            ) { backStackEntry ->
                val listId = backStackEntry.arguments?.getInt("listId") ?: 0
                AddItemScreen(navController, viewModel, listId)
            }
        }
    }
}
