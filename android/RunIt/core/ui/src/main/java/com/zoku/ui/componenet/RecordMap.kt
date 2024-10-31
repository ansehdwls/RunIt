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
import com.zoku.ui.R
import kotlin.random.Random

private const val TAG = "RecordMap"

@Composable
fun RecordMap(modifier: Modifier = Modifier) {


    KakaoMap(
        locationX = 37.5665,  // 서울 시청
        locationY = 126.9780     // 경복궁
    )

}
@Composable
fun KakaoMap(
    modifier: Modifier = Modifier,
    locationX: Double,
    locationY: Double,
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            Log.e("KakaoMap", "지도를 불러오는데 실패했습니다.")
                        }

                        override fun onMapError(exception: Exception?) {
                            Log.e("KakaoMap", "지도를 불러오는 중 알 수 없는 에러가 발생했습니다. $exception")
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            // 카메라 설정
                            val cameraUpdate = com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition(LatLng.from(locationX, locationY))
                            kakaoMap.moveCamera(cameraUpdate)

                            // PolylineOverlay를 사용한 경로 표시
                            val polyline = PolylineOverlay().apply {
                                coords = generateRandomPath(locationX, locationY, 10)
                                color = android.graphics.Color.RED
                                outlineWidth = 10f
                            }
                            kakaoMap.addPolylineOverlay(polyline)
                        }
                    }
                )
            }
        }
    )
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