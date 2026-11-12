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

import androidx.compose.ui.graphics.Color

/**
 * The Sunshine Music palette: a strict two-colour duotone.
 *
 * Light mode is paper (#F6F3E7) with ink (#161610) foreground; dark mode swaps them. There are no
 * accent colours. Every surface role resolves to the page colour, and hierarchy or selection is
 * expressed only with alpha of the foreground colour. These are fixed values on purpose: Sunshine
 * Music does not use Material You dynamic colour, so the app looks identical on every device.
 *
 * The whole Material3 surface ramp is defined here, including `surfaceContainerLowest` through
 * `surfaceContainerHighest` and `surfaceTint`. Leaving any of those unset makes Compose fall back
 * to the Material baseline, which is a cold lavender, and that is how default Material theming
 * leaks back in.
 *
 * Note that several roles here have no direct Material3 `ColorScheme` slot (backgroundGradientTop,
 * sidebarControl, messageBubble, scrim, ...). They are kept so that XML-side themes and any custom
 * surfaces can be tinted from the same single source. The six `*Container*` roles and
 * `surfaceTint` exist only on the XML side, in res/values/colors.xml, because Compose's
 * ColorScheme has no such roles.
 */
data class SunshinePalette(
    val background: Color,
    val backgroundGradientTop: Color,
    val settingsBackground: Color,
    val sidebarBackground: Color,
    val sidebarControl: Color,
    val settingsIcon: Color,
    val surface: Color,
    val surfaceHigh: Color,
    val surfaceHigher: Color,
    val surfaceVariant: Color,
    val surfaceBright: Color,
    val surfaceDim: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceTint: Color,
    val outline: Color,
    val outlineSoft: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val error: Color,
    val messageBubble: Color,
    val scrim: Color,
)

private val Ink = Color(0xFF161610)
private val Paper = Color(0xFFF6F3E7)

val LightSunshinePalette = SunshinePalette(
    background = Paper,
    backgroundGradientTop = Paper,
    settingsBackground = Paper,
    sidebarBackground = Paper,
    sidebarControl = Ink.copy(alpha = 0.10f),
    settingsIcon = Ink,
    surface = Paper,
    surfaceHigh = Paper,
    surfaceHigher = Paper,
    surfaceVariant = Ink.copy(alpha = 0.10f),
    surfaceBright = Paper,
    surfaceDim = Paper,
    surfaceContainerLowest = Paper,
    surfaceContainerLow = Paper,
    surfaceContainer = Paper,
    surfaceContainerHigh = Paper,
    surfaceTint = Paper,
    outline = Ink,
    outlineSoft = Ink.copy(alpha = 0.24f),
    onSurface = Ink,
    onSurfaceVariant = Ink.copy(alpha = 0.70f),
    primary = Ink,
    onPrimary = Paper,
    primaryContainer = Ink.copy(alpha = 0.10f),
    onPrimaryContainer = Ink,
    secondary = Ink,
    onSecondary = Paper,
    secondaryContainer = Ink.copy(alpha = 0.10f),
    onSecondaryContainer = Ink,
    tertiary = Ink,
    error = Ink,
    messageBubble = Ink.copy(alpha = 0.10f),
    scrim = Ink.copy(alpha = 0.60f),
)

val DarkSunshinePalette = SunshinePalette(
    background = Ink,
    backgroundGradientTop = Ink,
    settingsBackground = Ink,
    sidebarBackground = Ink,
    sidebarControl = Paper.copy(alpha = 0.10f),
    settingsIcon = Paper,
    surface = Ink,
    surfaceHigh = Ink,
    surfaceHigher = Ink,
    surfaceVariant = Paper.copy(alpha = 0.10f),
    surfaceBright = Ink,
    surfaceDim = Ink,
    surfaceContainerLowest = Ink,
    surfaceContainerLow = Ink,
    surfaceContainer = Ink,
    surfaceContainerHigh = Ink,
    surfaceTint = Ink,
    outline = Paper,
    outlineSoft = Paper.copy(alpha = 0.24f),
    onSurface = Paper,
    onSurfaceVariant = Paper.copy(alpha = 0.70f),
    primary = Paper,
    onPrimary = Ink,
    primaryContainer = Paper.copy(alpha = 0.10f),
    onPrimaryContainer = Paper,
    secondary = Paper,
    onSecondary = Ink,
    secondaryContainer = Paper.copy(alpha = 0.10f),
    onSecondaryContainer = Paper,
    tertiary = Paper,
    error = Paper,
    messageBubble = Paper.copy(alpha = 0.10f),
    scrim = Ink.copy(alpha = 0.60f),
)
