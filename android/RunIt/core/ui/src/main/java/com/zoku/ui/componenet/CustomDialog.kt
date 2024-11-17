package com.zoku.ui.componenet

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.zoku.ui.model.ExpInfoData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    onClickOk: () -> Unit,
    composable: @Composable () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { onClickOk() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {

        Surface(
            modifier = Modifier
                .width(400.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.Black
        ) {
            composable()
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCustomDialog() {
    val list = ExpInfoData.DEFAULT
    CustomDialog(onClickOk = { }) {
        ShowExpInfo(
            pagerState = rememberPagerState {
                list.size
            }) {

        }
    }

}