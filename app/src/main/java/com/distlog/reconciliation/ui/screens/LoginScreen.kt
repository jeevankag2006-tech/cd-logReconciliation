package com.distlog.reconciliation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.distlog.reconciliation.ui.components.NeonButton
import com.distlog.reconciliation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("admin_user") }
    var password by remember { mutableStateOf("********") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Secure Login",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your credentials to access the dashboard",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Operator ID") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = CardBackground,
                    focusedLabelColor = NeonBlue,
                    cursorColor = NeonBlue
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Access Key") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = CardBackground,
                    focusedLabelColor = NeonBlue,
                    cursorColor = NeonBlue
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            NeonButton(
                text = "AUTHENTICATE",
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            TextButton(onClick = { }) {
                Text("Forgot Access Key?", color = TextSecondary)
            }
        }
    }
}
