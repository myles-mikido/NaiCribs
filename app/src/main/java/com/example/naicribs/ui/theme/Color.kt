package com.example.naicribs.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/* ------------------------------------------------------------------ */
/*  Material 3 base color schemes                                     */
/*  (used as a fallback when dynamic color / Material You isn't        */
/*   available or is disabled)                                        */
/* ------------------------------------------------------------------ */

// Brand seed: violet primary, teal secondary
private val Primary = Color(0xFF6851C9)
private val OnPrimary = Color(0xFFFFFFFF)
private val PrimaryContainer = Color(0xFFE9DFFF)
private val OnPrimaryContainer = Color(0xFF221551)

private val Secondary = Color(0xFF00696B)
private val OnSecondary = Color(0xFFFFFFFF)
private val SecondaryContainer = Color(0xFF9DF1F1)
private val OnSecondaryContainer = Color(0xFF002020)

private val Tertiary = Color(0xFF7D5260)
private val OnTertiary = Color(0xFFFFFFFF)
private val TertiaryContainer = Color(0xFFFFD9E3)
private val OnTertiaryContainer = Color(0xFF31101D)

private val ErrorLightScheme = Color(0xFFBA1A1A)
private val OnErrorLightScheme = Color(0xFFFFFFFF)
private val ErrorContainerLightScheme = Color(0xFFFFDAD6)
private val OnErrorContainerLightScheme = Color(0xFF410002)

private val BackgroundLightScheme = Color(0xFFFAF7FC)
private val OnBackgroundLightScheme = Color(0xFF1C1B1F)
private val SurfaceLightScheme = Color(0xFFFFFFFF)
private val OnSurfaceLightScheme = Color(0xFF1C1B1F)
private val SurfaceVariantLightScheme = Color(0xFFE7E0EB)
private val OnSurfaceVariantLightScheme = Color(0xFF49454E)
private val OutlineLightScheme = Color(0xFF7A757F)

val NaiCribsLightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = ErrorLightScheme,
    onError = OnErrorLightScheme,
    errorContainer = ErrorContainerLightScheme,
    onErrorContainer = OnErrorContainerLightScheme,
    background = BackgroundLightScheme,
    onBackground = OnBackgroundLightScheme,
    surface = SurfaceLightScheme,
    onSurface = OnSurfaceLightScheme,
    surfaceVariant = SurfaceVariantLightScheme,
    onSurfaceVariant = OnSurfaceVariantLightScheme,
    outline = OutlineLightScheme,
)

private val PrimaryDark = Color(0xFFCBBBFF)
private val OnPrimaryDark = Color(0xFF381E72)
private val PrimaryContainerDark = Color(0xFF4F378A)
private val OnPrimaryContainerDark = Color(0xFFE9DFFF)

private val SecondaryDark = Color(0xFF5FE0DE)
private val OnSecondaryDark = Color(0xFF00373A)
private val SecondaryContainerDark = Color(0xFF004F52)
private val OnSecondaryContainerDark = Color(0xFF9DF1F1)

private val TertiaryDark = Color(0xFFEFB8C8)
private val OnTertiaryDark = Color(0xFF4A2532)
private val TertiaryContainerDark = Color(0xFF633B48)
private val OnTertiaryContainerDark = Color(0xFFFFD9E3)

private val ErrorDarkScheme = Color(0xFFFFB4AB)
private val OnErrorDarkScheme = Color(0xFF690005)
private val ErrorContainerDarkScheme = Color(0xFF93000A)
private val OnErrorContainerDarkScheme = Color(0xFFFFDAD6)

private val BackgroundDarkScheme = Color(0xFF141218)
private val OnBackgroundDarkScheme = Color(0xFFE6E1E9)
private val SurfaceDarkScheme = Color(0xFF1D1B20)
private val OnSurfaceDarkScheme = Color(0xFFE6E1E9)
private val SurfaceVariantDarkScheme = Color(0xFF48454E)
private val OnSurfaceVariantDarkScheme = Color(0xFFCAC4CF)
private val OutlineDarkScheme = Color(0xFF948F99)

val NaiCribsDarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDarkScheme,
    onError = OnErrorDarkScheme,
    errorContainer = ErrorContainerDarkScheme,
    onErrorContainer = OnErrorContainerDarkScheme,
    background = BackgroundDarkScheme,
    onBackground = OnBackgroundDarkScheme,
    surface = SurfaceDarkScheme,
    onSurface = OnSurfaceDarkScheme,
    surfaceVariant = SurfaceVariantDarkScheme,
    onSurfaceVariant = OnSurfaceVariantDarkScheme,
    outline = OutlineDarkScheme,
)

/* ------------------------------------------------------------------ */
/*  Extended brand colors                                              */
/*                                                                      */
/*  These preserve the original `NaiCribsColors.Purple`, `.Teal`, etc.  */
/*  call-sites used across every screen, but now resolve through the    */
/*  current theme (light/dark) instead of being hard-coded constants.   */
/* ------------------------------------------------------------------ */

data class NaiCribsExtendedColors(
    val purple: Color,
    val purpleDark: Color,
    val purpleMid: Color,
    val purpleLight: Color,
    val teal: Color,
    val tealLight: Color,
    val tealDark: Color,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val border: Color,
    val error: Color,
    val errorLight: Color,
)

val LightExtendedColors = NaiCribsExtendedColors(
    purple = Color(0xFF6851C9),
    purpleDark = Color(0xFF4A3894),
    purpleMid = Color(0xFF8A78E0),
    purpleLight = Color(0xFFEDE7FF),
    teal = Color(0xFF00696B),
    tealLight = Color(0xFFD3F5F1),
    tealDark = Color(0xFF004B4E),
    background = BackgroundLightScheme,
    surface = SurfaceLightScheme,
    textPrimary = Color(0xFF1C1B1F),
    textMuted = Color(0xFF6F6C74),
    border = Color(0xFFE2DFE8),
    error = ErrorLightScheme,
    errorLight = Color(0xFFFFDAD6),
)

val DarkExtendedColors = NaiCribsExtendedColors(
    purple = Color(0xFFCBBBFF),
    purpleDark = Color(0xFF9C87E8),
    purpleMid = Color(0xFFB5A4F5),
    purpleLight = Color(0xFF362F55),
    teal = Color(0xFF5FE0DE),
    tealLight = Color(0xFF16403F),
    tealDark = Color(0xFF8FF2EF),
    background = BackgroundDarkScheme,
    surface = SurfaceDarkScheme,
    textPrimary = Color(0xFFE6E1E9),
    textMuted = Color(0xFFA9A5AF),
    border = Color(0xFF48454E),
    error = Color(0xFFFFB4AB),
    errorLight = Color(0xFF4C1512),
)

val LocalNaiCribsExtendedColors = staticCompositionLocalOf { LightExtendedColors }

/**
 * Backward-compatible accessor object.
 *
 * Every existing screen references colors as `NaiCribsColors.Purple`,
 * `NaiCribsColors.Background`, etc. Keeping that exact call syntax means
 * none of the call sites need to change â€” only this object's internals do,
 * now sourcing from [LocalNaiCribsExtendedColors] so every color
 * automatically adapts to light/dark theme.
 */
object NaiCribsColors {
    val Purple: Color @Composable get() = LocalNaiCribsExtendedColors.current.purple
    val PurpleDark: Color @Composable get() = LocalNaiCribsExtendedColors.current.purpleDark
    val PurpleMid: Color @Composable get() = LocalNaiCribsExtendedColors.current.purpleMid
    val PurpleLight: Color @Composable get() = LocalNaiCribsExtendedColors.current.purpleLight
    val Teal: Color @Composable get() = LocalNaiCribsExtendedColors.current.teal
    val TealLight: Color @Composable get() = LocalNaiCribsExtendedColors.current.tealLight
    val TealDark: Color @Composable get() = LocalNaiCribsExtendedColors.current.tealDark
    val Background: Color @Composable get() = LocalNaiCribsExtendedColors.current.background
    val Surface: Color @Composable get() = LocalNaiCribsExtendedColors.current.surface
    val TextPrimary: Color @Composable get() = LocalNaiCribsExtendedColors.current.textPrimary
    val TextMuted: Color @Composable get() = LocalNaiCribsExtendedColors.current.textMuted
    val Border: Color @Composable get() = LocalNaiCribsExtendedColors.current.border
    val Error: Color @Composable get() = LocalNaiCribsExtendedColors.current.error
    val ErrorLight: Color @Composable get() = LocalNaiCribsExtendedColors.current.errorLight
}