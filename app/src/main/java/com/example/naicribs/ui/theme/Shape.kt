package com.example.naicribs.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val NaiCribsShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

// Convenience tokens used across screens for a consistent, slightly
// softer "premium" rounding than the raw MD3 defaults.
object NaiCribsCorners {
    val Field = RoundedCornerShape(14.dp)
    val Card = RoundedCornerShape(18.dp)
    val CardLarge = RoundedCornerShape(24.dp)
    val Button = RoundedCornerShape(16.dp)
    val Chip = RoundedCornerShape(10.dp)
    val Sheet = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
}