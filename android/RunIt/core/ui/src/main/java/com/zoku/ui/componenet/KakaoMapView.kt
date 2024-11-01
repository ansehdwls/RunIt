package com.zoku.ui.componenet

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapType
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory

@Composable
fun KakaoMapView(
    modifier: Modifier = Modifier,
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

                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                LatLng.from(
                                    38.2070795,
                                    128.5168879
                                )
                            )

                            kakaoMap.moveCamera(cameraUpdate)
                            kakaoMap.changeMapType(MapType.NORMAL)


                        }
                    },
                )
            }

        }
    )

}