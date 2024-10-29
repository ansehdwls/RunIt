package com.zoku.ui.componenet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.random.Random
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


@Composable
fun RecordGraph(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        Text(text = title)
        // 예시 데이터
        LineChartView()
    }
}

@Composable
fun LineChartView() {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(16.dp),
        factory = { context ->

            LineChart(context).apply {
                // X축 설정
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f  // X축 레이블 간격

                // Y축 설정
                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(false)

                // 기타 설정
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                animateX(1000)  // X축 애니메이션
            }
        },
        update = { lineChart ->
            val entries = (1..10).map { Entry(it.toFloat(), Random.nextFloat() * 10) }
            val lineDataSet = LineDataSet(entries, "Sample Data").apply {
                color = com.zoku.ui.BaseYellow.toArgb()
                lineWidth = 2f
                circleRadius = 4f
                setDrawCircleHole(false)
                setDrawCircles(false)
                setDrawValues(false)  // 각 점의 값 숨기기
            }
            lineChart.data = LineData(lineDataSet)
            lineChart.invalidate()  // 차트 갱신
        }
    )
}