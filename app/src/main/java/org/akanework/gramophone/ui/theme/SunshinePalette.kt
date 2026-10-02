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
 * The Sunshine Music palette, ported verbatim from Sunshinussy
 * (`shared/src/commonMain/kotlin/com/highsockscapital/sunshine/ui/theme/Color.kt`).
 *
 * Cream paper, warm ink, amber, sage. These are fixed values on purpose: Sunshine Music does not
 * use Material You dynamic colour, so the app looks identical on every device.
 *
 * Note that several roles here have no direct Material3 `ColorScheme` slot (backgroundGradientTop,
 * sidebarControl, messageBubble, scrim, ...). They are kept so that XML-side themes and any custom
 * surfaces can be tinted from the same single source.
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

val LightSunshinePalette = SunshinePalette(
    background = Color(0xFFF6F3E7),
    backgroundGradientTop = Color(0xFFF6F3E7),
    settingsBackground = Color(0xFFF6F3E7),
    sidebarBackground = Color(0xFFFBF9F0),
    sidebarControl = Color(0xFFF3F0E4),
    settingsIcon = Color(0xFF000000),
    surface = Color(0xFFFFFFFF),
    surfaceHigh = Color(0xFFF3F3F2),
    surfaceHigher = Color(0xFFECECEC),
    surfaceVariant = Color(0xFFE5E5E5),
    outline = Color(0xFF161610),
    outlineSoft = Color(0xFFE3DFD0),
    onSurface = Color(0xFF161610),
    onSurfaceVariant = Color(0xFF5F5B4E),
    primary = Color(0xFFFF9E43),
    onPrimary = Color(0xFF3D2400),
    primaryContainer = Color(0xFFFFE3C2),
    onPrimaryContainer = Color(0xFF6B3D00),
    secondary = Color(0xFF4A7B6B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDDF1E8),
    onSecondaryContainer = Color(0xFF1E4A3B),
    tertiary = Color(0xFFFFB866),
    error = Color(0xFFB43E3E),
    messageBubble = Color(0xFFFFE8CF),
    scrim = Color(0x22000000),
)

val DarkSunshinePalette = SunshinePalette(
    background = Color(0xFF161610),
    backgroundGradientTop = Color(0xFF1C1B16),
    settingsBackground = Color(0xFF161610),
    sidebarBackground = Color(0xFF1E1D18),
    sidebarControl = Color(0xFF26241E),
    settingsIcon = Color(0xFFE8E6DE),
    surface = Color(0xFF201F19),
    surfaceHigh = Color(0xFF26251F),
    surfaceHigher = Color(0xFF2C2B24),
    surfaceVariant = Color(0xFF343229),
    outline = Color(0xFF4A483E),
    outlineSoft = Color(0xFF2E2C25),
    onSurface = Color(0xFFE8E6DE),
    onSurfaceVariant = Color(0xFFA8A599),
    primary = Color(0xFFFFAE56),
    onPrimary = Color(0xFF3D2400),
    primaryContainer = Color(0xFF5C3A10),
    onPrimaryContainer = Color(0xFFFFE3C2),
    secondary = Color(0xFF89C8AF),
    onSecondary = Color(0xFF143126),
    secondaryContainer = Color(0xFF24483A),
    onSecondaryContainer = Color(0xFFDDF6EA),
    tertiary = Color(0xFFFFC98A),
    error = Color(0xFFFF8E8E),
    messageBubble = Color(0xFF4A3418),
    scrim = Color(0x66000000),
)
