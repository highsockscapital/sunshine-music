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
 * Every role below is set explicitly. Material3's default light and dark schemes fill any role
 * left unset with the Material baseline, which is cold and lavender, so an unset role is a hole
 * through which default Material theming shows.
 */
fun SunshinePalette.toColorScheme(dark: Boolean): ColorScheme {
    val scheme = if (dark) darkColorScheme() else lightColorScheme()
    return scheme.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = background,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onSecondary,
        tertiaryContainer = secondaryContainer,
        onTertiaryContainer = onSecondaryContainer,
        background = background,
        onBackground = onSurface,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = onSurface,
        inverseOnSurface = background,
        error = error,
        onError = onSecondary,
        errorContainer = secondaryContainer,
        onErrorContainer = onSecondaryContainer,
        outline = outline,
        outlineVariant = outlineSoft,
        scrim = scrim,
        surfaceBright = surfaceBright,
        surfaceDim = surfaceDim,
        surfaceContainerLowest = surfaceContainerLowest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHigh,
    )
}

/** The palette for the given mode. */
fun sunshinePalette(dark: Boolean): SunshinePalette = if (dark) DarkSunshinePalette else LightSunshinePalette

/**
 * Gramophone's existing "pure dark" preference is kept because it is an existing user-facing
 * setting, but the duotone palette already uses ink (#161610) as its darkest surface. There is no
 * darker colour left to crush to, so this is deliberately a no-op.
 */
fun ColorScheme.asPureDark(): ColorScheme = this