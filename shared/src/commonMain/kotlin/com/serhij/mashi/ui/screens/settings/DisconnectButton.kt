package com.serhij.mashi.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mashi.shared.generated.resources.Res
import mashi.shared.generated.resources.discord_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun DisconnectButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(27),
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
                text = "Disconnect",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.weight(1F))
        }
    }
}