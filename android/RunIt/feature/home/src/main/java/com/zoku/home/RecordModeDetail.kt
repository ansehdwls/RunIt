package com.zoku.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.ui.componenet.RecordDetailInfo
import com.zoku.ui.componenet.RecordGraph
import com.zoku.ui.componenet.RecordMap

@Composable
fun RecordModeDetail(modifier: Modifier = Modifier, runRecord: RunRecordDetail? = null) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(com.zoku.ui.BaseGray)
    ) {
        // title
        RecordDetailTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        )

        // 지도 경로 표시
        Box(
            modifier.weight(3f)
        ) {
            RecordMap()
        }

        // 세부 기록 표시
        Box(
            modifier.weight(7f)
        ) {
            RecordDetailInfo(runRecord = runRecord
            )
        }
    }
}

@Composable
fun RecordDetailTitle(modifier: Modifier = Modifier) {
    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("사용자 코스") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 수정 버튼 클릭 시
                if (isEditing) {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.background(Color.Transparent),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .padding(4.dp)
                            ) {
                                if (title.isEmpty()) {
                                    Text(
                                        text = "코스명을 입력하세요",
                                        color = Color.Gray,
                                        style = TextStyle(fontSize = 20.sp),
                                        fontFamily = com.zoku.ui.ZokuFamily
                                    )
                                }
                                innerTextField()  // 실제 입력 필드
                            }
                        }
                    )
                    IconButton(onClick = { isEditing = false }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
                else {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = com.zoku.ui.ZokuFamily
                    )
                    IconButton(
                        onClick = { isEditing = true }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_record_detail_icon),
                            tint = Color.White,
                            modifier = Modifier
                                .width(25.dp)
                                .height(25.dp),
                            contentDescription = null
                        )
                    }
                }


            }
        }

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.delete_record_detail_icon),
                contentDescription = null,
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp),
                tint = Color.Red
            )
        }
    }
}
