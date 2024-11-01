package com.zoku.ui.componenet

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapType
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

@Composable
fun KakaoMapView(
    modifier: Modifier = Modifier,
//    latitude: Double = 36.2070795,
//    longitude: Double = 128.5168879,
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
                            Toast.makeText(context, "지도를 불러오는데 실패하였습니다", Toast.LENGTH_SHORT).show()
                        }

                        override fun onMapError(p0: Exception?) {
                            Toast.makeText(context, "지도를 불러오는데 실패하였습니다", Toast.LENGTH_SHORT).show()
                        }
                    },

                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                LatLng.from(
                                    36.2070795,
                                    128.5168879
                                )
                            )

                            val style = kakaoMap.labelManager?.addLabelStyles(
                                LabelStyles.from(
                                    //마커 drawable 표시
                                    LabelStyle.from()
                                )
                            )

                            val options =
                                LabelOptions.from(LatLng.from(36.2070795, 128.5168879)).setStyles(style)

                            val layer = kakaoMap.labelManager?.layer

                            kakaoMap.moveCamera(cameraUpdate)
                            kakaoMap.changeMapType(MapType.NORMAL)

                            layer?.addLabel(options)
                        }

                        override fun getPosition(): LatLng {
                            return LatLng.from(36.2070795, 128.5168879)
                        }
                    },
                )
            }

        }
    )

}