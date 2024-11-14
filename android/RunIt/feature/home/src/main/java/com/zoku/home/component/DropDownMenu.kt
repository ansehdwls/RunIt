package com.zoku.home.component

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.zoku.ui.theme.BaseDarkBackground
import com.zoku.ui.theme.ZokuFamily

@Composable
fun DropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,   // 메뉴가 닫힐 때 호출되는 콜백
    onItemSelected: (String) -> Unit,  // 아이템 선택 시 호출되는 콜백,
    itemList : Array<String>
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest, // 메뉴 외부를 클릭하면 닫히도록 처리
        modifier = Modifier.background(BaseDarkBackground)
    ) {


        for(s in itemList){
            DropdownMenuItem(
                text = { Text(s, color = Color.White, fontFamily = ZokuFamily) },
                onClick = {
                    onItemSelected(s) // 아이템이 선택되었을 때 콜백 호출
                    onDismissRequest()     // 메뉴 닫기
                },
                modifier = Modifier.background(BaseDarkBackground)
            )
        }

    }
}