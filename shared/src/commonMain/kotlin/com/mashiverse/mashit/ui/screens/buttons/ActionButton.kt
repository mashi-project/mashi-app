package com.mashiverse.mashit.ui.screens.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.SmallIconSize
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ActionButton(
    icon: ImageVector,
    text: String? = null,
    onClick: () -> Unit,
    isAnimated: Boolean = false,
    isRed: Boolean = false
) {
    var targetColor by remember { mutableStateOf(ContentAccentColor) }
    LaunchedEffect(isAnimated) {
        if (isAnimated) {
            while (true) {
                targetColor = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat(),
                    alpha = 1f
                )
                delay(600.milliseconds)
            }
        } else {
            targetColor = ContentAccentColor
        }
    }

    val animatedTint by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500),
        label = "RandomColorAnimation"
    )

    IconButton(
        modifier = Modifier
            .size(
                40
                    .dp
            ),
        shape = RoundedCornerShape(90),
        colors = IconButtonDefaults.iconButtonColors().copy(
            containerColor = if (!isRed) {
                Secondary
            } else {
                MaterialTheme.colorScheme.error
            },
            contentColor = ContentAccentColor,
        ),
        onClick = onClick,
    ) {
        if (text != null) {
            Text(
                text = text,
                color = animatedTint,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            Icon(
                modifier = Modifier
                    .size(SmallIconSize),
                imageVector = icon,
                tint = animatedTint,
                contentDescription = null
            )
        }
    }
}