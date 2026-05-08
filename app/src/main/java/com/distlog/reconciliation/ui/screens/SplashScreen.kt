package com.distlog.reconciliation.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.distlog.reconciliation.ui.theme.DarkBackground
import com.distlog.reconciliation.ui.theme.NeonBlue
import kotlinx.coroutines.delay

import androidx.compose.ui.tooling.preview.Preview
import com.distlog.reconciliation.ui.theme.DistLogTheme

@Preview
@Composable
fun SplashPreview() {
    DistLogTheme {
        SplashScreen(onNavigateToLogin = {})
    }
}

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1.2f else 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500)
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Hub,
                contentDescription = null,
                tint = NeonBlue,
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "DISTLOG",
                style = MaterialTheme.typography.displayLarge,
                color = NeonBlue,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 8.sp
            )
            Text(
                text = "RECONCILIATION SYSTEM",
                style = MaterialTheme.typography.labelSmall,
                color = NeonBlue.copy(alpha = 0.7f),
                letterSpacing = 2.sp
            )
        }
    }
}
