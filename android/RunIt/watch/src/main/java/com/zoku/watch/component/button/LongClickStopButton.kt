package com.zoku.watch.component.button

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.tooling.preview.devices.WearDevices
import com.zoku.ui.BaseYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LongClickStopButton(
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 80.dp,
    animationDuration: Int = 1000
) {
    var progress by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = LinearEasing
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(buttonSize)
            .clip(CircleShape)
            .background(Color.Black)
            .border(
                width = 2.dp,
                color = BaseYellow,
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { // onPress를 사용하여 눌림과 해제를 감지
                        val pressJob = scope.launch {
                            progress = 1f
                            delay(animationDuration.toLong())
                            onStop()
                            progress = 0f
                        }
                        tryAwaitRelease() // 사용자가 손을 떼면
                        if (pressJob.isActive) {
                            pressJob.cancel() // 애니메이션 취소
                            progress = 0f // 상태 초기화
                        }
                    }
                )
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawArc(
                color = BaseYellow,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 100f)
            )
        }

        Icon(
            imageVector = Icons.Default.Stop,
            contentDescription = "Stop",
            tint = BaseYellow,
            modifier = Modifier.size(buttonSize/2)
        )
    }
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun LongClickStopPreview() {

    LongClickStopButton(
        modifier = Modifier.fillMaxSize(),
        onStop = {},
        buttonSize = 80.dp
    )

}