package com.distlog.reconciliation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.distlog.reconciliation.ui.theme.*

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    glowColor: Color = NeonBlue,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground.copy(alpha = 0.8f))
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(glowColor.copy(alpha = 0.5f), Color.Transparent)
                ),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    trend: String,
    icon: ImageVector,
    glowColor: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.height(140.dp),
        glowColor = glowColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = glowColor,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = trend,
                style = MaterialTheme.typography.labelSmall,
                color = if (trend.contains("+") || trend.contains("Constant")) SuccessGreen else WarningOrange
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = NeonBlue
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.1f),
            contentColor = color
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}
