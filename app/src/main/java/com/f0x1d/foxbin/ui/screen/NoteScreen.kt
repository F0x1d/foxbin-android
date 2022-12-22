package com.f0x1d.foxbin.ui.screen

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.f0x1d.foxbin.R
import com.f0x1d.foxbin.extensions.DefaultIfError
import com.f0x1d.foxbin.extensions.DefaultIfLoading
import com.f0x1d.foxbin.extensions.IfLoaded
import com.f0x1d.foxbin.extensions.shareNote
import com.f0x1d.foxbin.model.Screen
import com.f0x1d.foxbin.ui.activity.MainActivity
import com.f0x1d.foxbin.ui.widget.BackNavigationIcon
import com.f0x1d.foxbin.viewmodel.NoteViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(navController: NavController, slug: String) {
    val viewModel = noteViewModel(slug = slug)
    val context = LocalContext.current

    val loadingState by viewModel.loadingStateFlow.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Column {
        LargeTopAppBar(
            title = { Text(text = slug) },
            navigationIcon = { BackNavigationIcon(navController = navController) },
            actions = {
                loadingState.IfLoaded {
                    if (it.editable == true) {
                        IconButton(
                            onClick = {
                                navController.navigate("${Screen.Edit.route}/${it.slug}/${Uri.encode(it.content)}") // so cool
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = null
                            )
                        }
                    }
                }
                IconButton(onClick = { context.shareNote(slug) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = null
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            loadingState.DefaultIfLoading()
            loadingState.DefaultIfError { viewModel.load() }

            loadingState.IfLoaded { note ->
                Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Column {
                        Spacer(modifier = Modifier.size(10.dp))

                        SelectionContainer {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        start = 10.dp,
                                        end = 10.dp
                                    )
                                    .navigationBarsPadding(),
                                text = note.content ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun noteViewModel(slug: String): NoteViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        MainActivity.ViewModelFactoryProvider::class.java
    ).noteViewModelFactory()

    return viewModel(factory = NoteViewModel.provideFactory(factory, slug))
}