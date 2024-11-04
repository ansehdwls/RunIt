package com.zoku.ui.componenet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.shape.Polygon
import com.kakao.vectormap.shape.Polyline
import com.kakao.vectormap.shape.MapPoints
import com.zoku.ui.R
import kotlin.random.Random

private const val TAG = "RecordMap"

@Composable
fun RecordMap(modifier: Modifier = Modifier) {
 //  랜던 값 배치
    val polylinePoints = listOf(
        MapPoints.fromLatLng(LatLng.from(37.5665, 126.9780), // 서울 시청
            LatLng.from(37.5651, 126.9895), // 종로구
            LatLng.from(37.5700, 126.9920) // 경복궁
        )
    )
    KakaoMap(
        polylinePoints  = polylinePoints
    )

}
@Composable
fun KakaoMap(
    modifier: Modifier = Modifier,
    polylinePoints: List<MapPoints>
) {

    val context = LocalContext.current
    val mapView = remember { MapView(context) }


}
// 랜덤 경로 생성 함수
fun generateRandomPath(startX: Double, startY: Double, numPoints: Int): List<LatLng> {
    val path = mutableListOf<LatLng>()
    var currentX = startX
    var currentY = startY

    repeat(numPoints) {
        currentX += Random.nextDouble(-0.005, 0.005)
        currentY += Random.nextDouble(-0.005, 0.005)
        path.add(LatLng.from(currentX, currentY))
    }
    return path
}