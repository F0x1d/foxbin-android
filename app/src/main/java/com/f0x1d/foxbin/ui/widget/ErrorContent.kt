package com.f0x1d.foxbin.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.f0x1d.foxbin.R

@Composable
fun ErrorContent(error: String?, onRetryClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(30.dp),
                painter = painterResource(id = R.drawable.ic_clear),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )

            if (error != null) {
                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = error
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            ButtonWithIcon(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = R.string.retry,
                icon = R.drawable.ic_retry,
                onClick = onRetryClick
            )
        }
    }
}