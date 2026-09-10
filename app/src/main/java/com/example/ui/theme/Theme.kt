package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = FianarTealLight,
    onPrimary = Color.White,
    primaryContainer = FianarTealDark,
    onPrimaryContainer = Color(0xFFB2DFDB),
    secondary = FianarAmberLight,
    onSecondary = Color.Black,
    secondaryContainer = FianarAmberDark,
    onSecondaryContainer = Color(0xFFFFE082),
    tertiary = FianarTerracotta,
    background = FianarSurfaceDark,
    surface = FianarCardDark,
    surfaceVariant = Color(0xFF263330),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = FianarTeal,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2F1),
    onPrimaryContainer = FianarTealDark,
    secondary = FianarAmber,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = FianarAmberDark,
    tertiary = FianarTerracotta,
    background = FianarCream,
    surface = FianarCardLight,
    surfaceVariant = Color(0xFFEDEAE5),
    onBackground = Color(0xFF1B1C1B),
    onSurface = Color(0xFF1B1C1B),
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

