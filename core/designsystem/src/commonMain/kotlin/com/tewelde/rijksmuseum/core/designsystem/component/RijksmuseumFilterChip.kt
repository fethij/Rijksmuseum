package com.tewelde.rijksmuseum.core.designsystem.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Filter chip with included leading checked icon as well as text content slot.
 *
 * @param selected Whether the chip is currently checked.
 * @param onSelectedChange Called when the user clicks the chip and toggles checked.
 * @param modifier Modifier to be applied to the chip.
 * @param enabled Controls the enabled state of the chip. When `false`, this chip will not be
 * clickable and will appear disabled to accessibility services.
 * @param label The text label content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RijksmuseumFilterChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = { onSelectedChange(!selected) },
        label = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                label()
            }
        },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                )
            }
        } else {
            null
        },
        shape = CircleShape,
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.onBackground,
            selectedBorderColor = MaterialTheme.colorScheme.onBackground,
            disabledBorderColor = MaterialTheme.colorScheme.onBackground,
            disabledSelectedBorderColor = MaterialTheme.colorScheme.onBackground,
            selectedBorderWidth = AgvChipDefaults.ChipBorderWidth,
            enabled = enabled,
            selected = selected,
        ),
        colors = FilterChipDefaults.filterChipColors(
            labelColor = MaterialTheme.colorScheme.onBackground,
            iconColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = if (selected) {
                MaterialTheme.colorScheme.onBackground
            } else {
                Color.Transparent
            },
            disabledLabelColor = MaterialTheme.colorScheme.onBackground,
            selectedContainerColor = MaterialTheme.colorScheme.onSecondary,
            selectedLabelColor = MaterialTheme.colorScheme.onBackground,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        ),
    )
}

@Preview
@Composable
fun ChipPreview() {
    RijksmuseumFilterChip(selected = true, onSelectedChange = {}) {
        Text("Chip")
    }
}

/**
 * Now in Android chip default values.
 */
object AgvChipDefaults {
    val ChipBorderWidth = 1.dp
}
