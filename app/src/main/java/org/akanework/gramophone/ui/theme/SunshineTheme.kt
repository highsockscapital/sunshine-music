/*
 *     Copyright (C) 2025 Sunshine Music contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.akanework.gramophone.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

/**
 * Material3 [ColorScheme] built from a [SunshinePalette].
 *
 * Material You dynamic colour is deliberately NOT used. The Sunshine palette is the brand, and a
 * wallpaper-derived scheme would replace it entirely on Android 12+.
 */
fun SunshinePalette.toColorScheme(dark: Boolean): ColorScheme {
    val scheme = if (dark) darkColorScheme() else lightColorScheme()
    return scheme.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        error = error,
        background = background,
        surface = surface,
        surfaceVariant = surfaceVariant,
        surfaceContainerHighest = surfaceVariant,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
    )
}

/** The palette for the given mode. */
fun sunshinePalette(dark: Boolean): SunshinePalette = if (dark) DarkSunshinePalette else LightSunshinePalette

/**
 * Gramophone's existing "pure dark" preference, kept because it is an existing user-facing setting.
 * It crushes every surface to true black rather than the palette's warm near-black.
 */
fun ColorScheme.asPureDark(): ColorScheme = copy(
    background = androidx.compose.ui.graphics.Color.Black,
    surface = androidx.compose.ui.graphics.Color.Black,
    surfaceVariant = androidx.compose.ui.graphics.Color.Black,
    surfaceContainerLowest = androidx.compose.ui.graphics.Color.Black,
    surfaceContainerLow = androidx.compose.ui.graphics.Color.Black,
    surfaceContainer = androidx.compose.ui.graphics.Color.Black,
    surfaceContainerHigh = androidx.compose.ui.graphics.Color.Black,
    surfaceContainerHighest = androidx.compose.ui.graphics.Color.Black,
)