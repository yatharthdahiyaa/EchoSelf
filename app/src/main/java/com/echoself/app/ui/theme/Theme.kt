package com.echoself.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val CosmosColorScheme = darkColorScheme(
    primary          = PurpleVibrant,
    onPrimary        = TextWhite,
    primaryContainer = PurpleGlow,
    background       = CosmosBlack,
    surface          = CosmosMid,
    onBackground     = TextWhite,
    onSurface        = TextWhite,
    secondary        = BlueAccent,
    tertiary         = GoldAccent,
    outline          = GlassBorder
)

@Composable
fun EchoSelfTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CosmosColorScheme,
        typography  = EchoSelfTypography,
        content     = content
    )
}
