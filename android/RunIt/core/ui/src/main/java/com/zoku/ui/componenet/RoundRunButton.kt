package com.zoku.ui.componenet

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zoku.ui.BaseYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.min

@Composable
fun RoundRunButton(
    modifier: Modifier = Modifier,
    containerColor: Color,
    resourceId: Int,
    resourceColor: Color,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        modifier = modifier.size(100.dp),
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Button",
            modifier = Modifier.size(44.dp),
            contentScale = ContentScale.Fit,
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(resourceColor)
        )
    }
}

@Composable
fun RoundStopButton(
    containerColor: Color,
    resourceId: Int,
    resourceColor: Color,
    onStopLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    var pressStartTime by remember { mutableLongStateOf(0L) }
    var pressDuration by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            pressStartTime = System.currentTimeMillis()
            while (isActive && isPressed) {
                pressDuration = System.currentTimeMillis() - pressStartTime
                if(pressDuration >= 1100){
                    onStopLongPress()
                }
                delay(16)
            }
        } else {
            pressDuration = 0L
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(100.dp)
            .background(containerColor, shape = CircleShape)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()

                        if (event.changes.any { it.pressed }) {
                            isPressed = true
                        }

                        if (event.changes.any { it.changedToUpIgnoreConsumed() }) {
                            if (pressDuration >= 1000) {
                                onStopLongPress()
                            }
                            isPressed = false
                        }
                    }
                }
            }
    ) {
        val angle = min(pressDuration, 1000L) / 1000f * 360f

//        원 테두리 그리기
        Canvas(modifier = Modifier.size(100.dp)) {
            if (isPressed || pressDuration > 0) {
                drawArc(
                    color = BaseYellow,
                    startAngle = 270f,
                    sweepAngle = angle,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx())
                )
            }
        }

//        // 원 전체그리기
//        Canvas(modifier = Modifier.size(1.dp)) {
//            if (isPressed || pressDuration > 0) {
//                drawArc(
//                    color = Color.White.copy(alpha = 0.3f),
//                    startAngle = 270f,
//                    sweepAngle = angle,
//                    useCenter = false,
//                    style = Stroke(width = 100.dp.toPx())
//                )
//            }
//        }


        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Button",
            modifier = Modifier.size(44.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(resourceColor)
        )
    }
}