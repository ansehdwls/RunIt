package com.zoku.ui.componenet

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapType
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.zoku.network.model.response.RouteInfo
import com.zoku.ui.routeColor
import kotlin.random.Random

private const val TAG = "RecordMap"

@Composable
fun RecordMap(modifier: Modifier = Modifier,
              routeList : List<RouteInfo> = emptyList()
) {

    if(routeList.size > 0)
        KakaoMapViewWithRandomRoute()
    else
        KakaoMapViewWithRandomRoute(initialLatitude = routeList[0].latitude,
            initialLongitude = routeList[0].longitude,
            numberOfPoints = routeList.size,
            routeList = routeList
            )

}

@Composable
fun KakaoMapViewWithRandomRoute(
    modifier: Modifier = Modifier,
    initialLatitude: Double = 37.5665, // 시작 지점의 위도 (예: 서울)
    initialLongitude: Double = 126.9780, // 시작 지점의 경도 (예: 서울)
    numberOfPoints: Int = 0, // 생성할 경로 점의 개수,
    routeList : List<RouteInfo> = emptyList()
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    //  랜던 값 배치
    val randomLocationList = remember {
        generateRandomRoute(initialLatitude, initialLongitude, numberOfPoints, routeList = routeList)
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { _ ->
            mapView.apply {
                start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            Log.d("확인", "카카오 맵이 파괴되었습니다")
                        }

                        override fun onMapError(error: Exception?) {
                            Log.e("확인", "카카오 맵 에러", error)
                            Toast.makeText(context, "맵 로딩에 실패했습니다", Toast.LENGTH_SHORT).show()
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            kakaoMap.changeMapType(MapType.NORMAL)
                            val routeLineManager = kakaoMap.routeLineManager!!
                            val routeLineLayer: RouteLineLayer = routeLineManager.layer

                            drawRouteOnMap(kakaoMap, routeLineLayer, randomLocationList)
                        }
                    },
                )
            }
        }
    )
}

// 랜덤 경로 생성 함수
private fun generateRandomRoute(
    startLat: Double,
    startLng: Double,
    points: Int,
    routeList: List<RouteInfo>
): List<LocationData> {
    return List(points) {
        // 임의의 범위 내에서 위도 및 경도 변동을 설정.
        val randomLat = routeList[points].latitude
        val randomLng = routeList[points].longitude
        LocationData(randomLat, randomLng)
    }
}

// 경로 그리기 함수
private fun drawRouteOnMap(
    kakaoMap: KakaoMap,
    routeLineLayer: RouteLineLayer,
    totalLocationList: List<LocationData>
) {
    val latLngList = totalLocationList.map {
        LatLng.from(it.latitude, it.longitude)
    }
    if (latLngList.size > 1) {
        val routeLineStyles = RouteLineStyles.from(
            RouteLineStyle.from(10f, routeColor.toArgb())
        )
        val stylesSet = RouteLineStylesSet.from(routeLineStyles)

        val segment = RouteLineSegment.from(latLngList)
            .setStyles(stylesSet.getStyles(0))

        val routeLineOptions = RouteLineOptions.from(segment)
            .setStylesSet(stylesSet)

        routeLineLayer.removeAll()  // 이전 경로 제거
        routeLineLayer.addRouteLine(routeLineOptions)  // 새 경로 추가

        // 경로의 중간 지점으로 카메라 이동
        val centerLat = latLngList.map { it.latitude }.average()
        val centerLng = latLngList.map { it.longitude }.average()
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(
            LatLng.from(centerLat, centerLng)
        )
        kakaoMap.moveCamera(cameraUpdate)
    }
}

data class LocationData(
    val latitude: Double,
    val longitude: Double,
)