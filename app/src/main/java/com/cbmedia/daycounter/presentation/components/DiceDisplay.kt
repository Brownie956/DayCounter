package com.cbmedia.daycounter.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cbmedia.daycounter.data.model.DiceRoll

@Composable
fun DiceDisplay(
    diceRoll: DiceRoll?,
    baseFirstDice: Int? = null,
    isRolling: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedDice(
            value = baseFirstDice ?: diceRoll?.dice1 ?: 0,
            label = if (baseFirstDice != null) "Days (Fixed)" else "Days",
            modifier = Modifier.weight(1f),
            isFixed = baseFirstDice != null,
            isRolling = isRolling,
            animationDelay = 0
        )
        AnimatedDice(
            value = diceRoll?.dice2 ?: 0,
            label = "Task",
            modifier = Modifier.weight(1f),
            isRolling = isRolling,
            animationDelay = 200
        )
        AnimatedDice(
            value = diceRoll?.dice3 ?: 0,
            label = "Multiplier",
            modifier = Modifier.weight(1f),
            scaledValue = diceRoll?.dice3?.let { when(it) { 1 -> "0"; 2 -> "0.25"; 3 -> "0.5"; 4 -> "0.75"; 5 -> "1"; 6 -> "2"; else -> "?" } },
            isRolling = isRolling,
            animationDelay = 400
        )
    }
}

@Composable
private fun AnimatedDice(
    value: Int,
    label: String,
    modifier: Modifier = Modifier,
    isFixed: Boolean = false,
    scaledValue: String? = null,
    isRolling: Boolean = false,
    animationDelay: Int = 0
) {
    // Simple animation approach
    val rotation by animateFloatAsState(
        targetValue = if (isRolling) 720f else 0f,
        animationSpec = if (isRolling) {
            tween(1000, delayMillis = animationDelay, easing = EaseInOut)
        } else {
            tween(0)
        },
        label = "rotation"
    )

    val scale = remember { Animatable(1f) }

    LaunchedEffect(isRolling) {
        if (isRolling) {
            scale.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
        } else {
            // Ensure it returns to 1f when not rolling
            scale.animateTo(1f)
        }
    }
    
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .rotate(rotation)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isFixed) MaterialTheme.colorScheme.primaryContainer else Color.White)
                .border(
                    width = 2.dp,
                    color = if (isFixed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (value > 0) value.toString() else "?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (value > 0) Color.Black else Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (scaledValue != null) {
            Text(
                text = "→ $scaledValue",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun Dice(
    value: Int,
    label: String,
    modifier: Modifier = Modifier,
    isFixed: Boolean = false,
    scaledValue: String? = null
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isFixed) MaterialTheme.colorScheme.primaryContainer else Color.White)
                .border(
                    width = 2.dp,
                    color = if (isFixed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (value > 0) value.toString() else "?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (value > 0) Color.Black else Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (scaledValue != null) {
            Text(
                text = "→ $scaledValue",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}