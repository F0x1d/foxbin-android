package com.f0x1d.foxbin.ui.widget

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ButtonWithIcon(modifier: Modifier = Modifier, @StringRes text: Int, @DrawableRes icon: Int, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        onClick = onClick
    ) {
        IconWithText(
            text = text,
            icon = icon
        )
    }
}

@Composable
fun OutlinedButtonWithIcon(modifier: Modifier = Modifier, @StringRes text: Int, @DrawableRes icon: Int, onClick: () -> Unit) {
    OutlinedButton(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        onClick = onClick
    ) {
        IconWithText(
            text = text,
            icon = icon
        )
    }
}

@Composable
private fun IconWithText(@StringRes text: Int, @DrawableRes icon: Int) {
    Icon(
        painter = painterResource(id = icon),
        contentDescription = null
    )

    Spacer(Modifier.size(ButtonDefaults.IconSpacing))

    Text(text = stringResource(id = text))
}