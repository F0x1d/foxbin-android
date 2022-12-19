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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.f0x1d.foxbin.R
import com.f0x1d.foxbin.extensions.DefaultIfError
import com.f0x1d.foxbin.extensions.DefaultIfLoading
import com.f0x1d.foxbin.extensions.IfIdle
import com.f0x1d.foxbin.ui.widget.BackNavigationIcon
import com.f0x1d.foxbin.ui.widget.ButtonWithIcon
import com.f0x1d.foxbin.ui.widget.OutlinedButtonWithIcon
import com.f0x1d.foxbin.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val viewModel = hiltViewModel<LoginViewModel>()

    val loadingState by viewModel.loadingStateFlow.collectAsState()

    var usernameText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }

    Column {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.login)) },
            navigationIcon = { BackNavigationIcon(navController = navController) }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            loadingState.DefaultIfLoading()
            loadingState.DefaultIfError { viewModel.retry() }

            loadingState.IfIdle {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 10.dp,
                                end = 10.dp
                            ),
                        value = usernameText,
                        onValueChange = { usernameText = it },
                        shape = RoundedCornerShape(12.dp),
                        label = { Text(text = stringResource(id = R.string.username)) },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 10.dp,
                                end = 10.dp
                            ),
                        value = passwordText,
                        onValueChange = { passwordText = it },
                        shape = RoundedCornerShape(12.dp),
                        label = { Text(text = stringResource(id = R.string.password)) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        OutlinedButtonWithIcon(
                            text = R.string.register,
                            icon = R.drawable.ic_register
                        ) {
                            viewModel.register(
                                usernameText,
                                passwordText
                            ) { navController.popBackStack() }
                        }
                        
                        Spacer(modifier = Modifier.size(10.dp))

                        ButtonWithIcon(
                            text = R.string.login,
                            icon = R.drawable.ic_login
                        ) {
                            viewModel.login(
                                usernameText,
                                passwordText
                            ) { navController.popBackStack() }
                        }
                    }
                }
            }
        }
    }
}