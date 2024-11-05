package com.zoku.ui.componenet

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.kakao.vectormap.route.RouteLine
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.zoku.ui.model.LocationData
import com.zoku.ui.routeColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun KakaoMapView(
    modifier: Modifier = Modifier,
    totalLocationList: List<LocationData>,
    onCaptureReady: suspend (File) -> Unit = {}
) {

    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    AndroidView(
        modifier = modifier
            .fillMaxSize(),
        factory = { _ ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            Log.d("확인", "카카오 맵 에러")
                        }

                        override fun onMapError(p0: Exception?) {
                            Log.d("확인", "카카오 맵 에러")
                        }
                    },

                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {

                            kakaoMap.changeMapType(MapType.NORMAL)

                            val routeLineManager = kakaoMap.routeLineManager!!
                            val routeLineLayer: RouteLineLayer = routeLineManager.layer

                            val routeLineStyles = RouteLineStyles.from(
                                RouteLineStyle.from(10f, routeColor.toArgb())
                            )
                            val stylesSet = RouteLineStylesSet.from(routeLineStyles)

                            val latLngList = totalLocationList.map {
                                LatLng.from(it.latitude, it.longitude)
                            }
                            if (latLngList.size > 1) {
                                val segment = RouteLineSegment.from(latLngList)
                                    .setStyles(stylesSet.getStyles(0))

                                val routeLineOptions = RouteLineOptions.from(segment)
                                    .setStylesSet(stylesSet)

                                routeLineLayer.addRouteLine(routeLineOptions)

                                val centerLat = latLngList.map { it.latitude }.average()
                                val centerLng = latLngList.map { it.longitude }.average()
                                val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                    LatLng.from(centerLat, centerLng)
                                )
                                kakaoMap.moveCamera(cameraUpdate)
                            }
                        }
                    },
                )
            }

        }
    )

    LaunchedEffect(Unit) {
        onCaptureReady(File(context.cacheDir, "map_capture.png").apply {
            val bitmap = captureMapBitmap(mapView)
            saveBitmapAsFile(this, bitmap)
        })
    }

}

suspend fun captureMapBitmap(mapView: MapView): Bitmap = withContext(Dispatchers.Main) {
    Bitmap.createBitmap(mapView.width, mapView.height, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        mapView.draw(canvas)
    }
}

suspend fun saveBitmapAsFile(file: File, bitmap: Bitmap) = withContext(Dispatchers.IO) {
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
}