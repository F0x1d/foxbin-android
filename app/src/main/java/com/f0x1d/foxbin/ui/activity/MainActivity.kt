package com.f0x1d.foxbin.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.f0x1d.foxbin.FoxBinApp
import com.f0x1d.foxbin.model.Screen
import com.f0x1d.foxbin.ui.screen.LoginScreen
import com.f0x1d.foxbin.ui.screen.NoteScreen
import com.f0x1d.foxbin.ui.screen.NotesScreen
import com.f0x1d.foxbin.ui.screen.PublishScreen
import com.f0x1d.foxbin.ui.theme.FoxbinTheme
import com.f0x1d.foxbin.viewmodel.NoteViewModelAssistedFactory
import com.f0x1d.foxbin.viewmodel.NotesViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun noteViewModelFactory(): NoteViewModelAssistedFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FoxbinTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    val notesViewModel = hiltViewModel<NotesViewModel>()

                    NavHost(navController = navController, startDestination = Screen.Notes.route) {
                        composable(Screen.Notes.route) {
                            NotesScreen(
                                navController = navController,
                                viewModel = notesViewModel
                            )
                        }

                        composable(
                            route = "${Screen.Note.route}/{slug}",
                            arguments = listOf(
                                navArgument("slug") { type = NavType.StringType }
                            ),
                            deepLinks = listOf(
                                navDeepLink { uriPattern = "${FoxBinApp.FOXBIN_DOMAIN}{slug}" }
                            )
                        ) {
                            NoteScreen(
                                navController = navController,
                                slug = it.arguments?.getString("slug")!!
                            )
                        }

                        composable(Screen.Publish.route) {
                            PublishScreen(
                                navController = navController,
                                notesViewModel = notesViewModel
                            )
                        }

                        composable(
                            route = "${Screen.Edit.route}/{slug}/{content}",
                            arguments = listOf(
                                navArgument("slug") { type = NavType.StringType },
                                navArgument("content") { type = NavType.StringType }
                            )
                        ) {
                            it.arguments?.apply {
                                PublishScreen(
                                    navController = navController,
                                    notesViewModel = notesViewModel,
                                    slug = getString("slug"),
                                    content = Uri.decode(getString("content"))
                                )
                            }
                        }

                        composable(Screen.Login.route) {
                            LoginScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}