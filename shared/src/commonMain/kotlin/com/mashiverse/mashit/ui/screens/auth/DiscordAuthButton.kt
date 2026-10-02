package com.mashiverse.mashit.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mashi.shared.generated.resources.Res
import mashi.shared.generated.resources.discord_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun DiscordAuthButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(27),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(31, 31, 31),
            contentColor = Color.White
        ),
        border = BorderStroke(color = Color.DarkGray, width = 0.5.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(Res.drawable.discord_icon),
                contentDescription = null
            )

            Spacer(modifier = Modifier.weight(1F))

            Text(
                text = "Sign in with Discord",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.weight(1F))
        }
    }
}

@Composable
@Preview
private fun DiscordAuthButtonPreview() {
    DiscordAuthButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {}
    )
}