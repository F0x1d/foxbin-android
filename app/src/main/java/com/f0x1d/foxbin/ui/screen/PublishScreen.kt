package com.f0x1d.foxbin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.f0x1d.foxbin.R
import com.f0x1d.foxbin.extensions.DefaultIfError
import com.f0x1d.foxbin.extensions.DefaultIfLoading
import com.f0x1d.foxbin.extensions.IfIdle
import com.f0x1d.foxbin.model.Screen
import com.f0x1d.foxbin.ui.widget.BackNavigationIcon
import com.f0x1d.foxbin.viewmodel.NotesViewModel
import com.f0x1d.foxbin.viewmodel.PublishViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishScreen(navController: NavController, notesViewModel: NotesViewModel, slug: String? = null, content: String? = null) {
    val editing = slug != null

    val viewModel = hiltViewModel<PublishViewModel>()

    val loadingState by viewModel.loadingStateFlow.collectAsState()

    var slugText by rememberSaveable { mutableStateOf(slug ?: "") }
    var contentText by rememberSaveable { mutableStateOf(content ?: "") }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = if (editing) R.string.edit else R.string.publish)) },
                navigationIcon = { BackNavigationIcon(navController = navController) },
                scrollBehavior = scrollBehavior
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                loadingState.DefaultIfLoading()
                loadingState.DefaultIfError { viewModel.retry() }

                loadingState.IfIdle {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp
                                ),
                            value = slugText,
                            onValueChange = { slugText = it },
                            shape = RoundedCornerShape(12.dp),
                            label = { Text(text = stringResource(id = R.string.slug)) },
                            singleLine = true,
                            enabled = !editing
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 10.dp,
                                    start = 10.dp,
                                    end = 10.dp,
                                    bottom = 88.dp
                                )
                                .navigationBarsPadding(),
                            value = contentText,
                            onValueChange = { contentText = it },
                            shape = RoundedCornerShape(12.dp),
                            label = { Text(text = stringResource(id = R.string.content)) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                val onDone = { slug: String ->
                    if (!editing) notesViewModel.reload()

                    navController.popBackStack(Screen.Notes.route, false)
                    navController.navigate("${Screen.Note.route}/$slug")
                }

                if (editing) viewModel.edit(slugText, contentText, onDone)
                else viewModel.publish(slugText, contentText, onDone)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .navigationBarsPadding()
                .imePadding()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_done),
                contentDescription = null
            )
        }
    }
}