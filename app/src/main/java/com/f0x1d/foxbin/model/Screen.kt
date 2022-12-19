package com.f0x1d.foxbin.model

sealed class Screen(val route: String) {
    object Notes: Screen("notes")
    object Note: Screen("note")
    object Publish: Screen("publish")
    object Edit: Screen("edit")
    object Login: Screen("login")
}