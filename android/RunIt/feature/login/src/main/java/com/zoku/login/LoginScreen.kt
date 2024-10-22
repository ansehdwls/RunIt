package com.zoku.login

import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(
            color = com.zoku.ui.BaseDarkBackground
        )
    ) {
        Spacer(modifier = Modifier.padding(top = 60.dp))
            Box(  modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .wrapContentWidth()
                .wrapContentHeight()
        ){
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Title",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)

                )
            }
        Image(
            painter = painterResource(id = R.drawable.main_logo),
            contentDescription = "Logo Image"
        )

        KakaoLoginButton(
            onKakaoLoginClick = {
                Toast.makeText(context, "로그인 버튼 클릭됨", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
                // 여기에 카카오 로그인 로직을 추가할 수 있습니다.
            }
        )
    }

}

@Composable
fun KakaoLoginButton(onKakaoLoginClick: () -> Unit) {
    Card(
        onClick = { onKakaoLoginClick() },
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.kakao_login_medium_wide),  // 카카오 로그인 아이콘
            contentDescription = "Kakao Login",
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        )
    }
}

