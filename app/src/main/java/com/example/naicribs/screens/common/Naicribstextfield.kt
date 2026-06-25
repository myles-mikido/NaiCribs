package com.example.naicribs.screens.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.naicribs.ui.theme.NaiCribsColors

/**
 * Shared text field used across ALL screens — auth, agent, customer.
 * Import from: com.example.naicribs.screens.common.NaiCribsTextField
 */
@Composable
fun NaiCribsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value                = value,
        onValueChange        = onValueChange,
        label                = { Text(label, fontSize = 13.sp) },
        leadingIcon          = if (leadingIcon != null) {
            {
                Icon(
                    imageVector        = leadingIcon,
                    contentDescription = null,
                    tint               = NaiCribsColors.TextMuted,
                    modifier           = Modifier.size(20.dp)
                )
            }
        } else null,
        trailingIcon         = trailingContent,
        visualTransformation = visualTransformation,
        keyboardOptions      = KeyboardOptions(keyboardType = keyboardType),
        singleLine           = true,
        modifier             = modifier.fillMaxWidth(),
        shape                = RoundedCornerShape(12.dp),
        colors               = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = NaiCribsColors.Purple,
            unfocusedBorderColor = NaiCribsColors.Border,
            focusedLabelColor    = NaiCribsColors.Purple,
            cursorColor          = NaiCribsColors.Purple
        )
    )
}