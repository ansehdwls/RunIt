package com.zoku.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.ui.Black


@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, viewModel: LoginViewModel) {
    
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLogin) {
        if (uiState.isLogin) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(
                color = Black
            )
            .verticalScroll(rememberScrollState())
            .systemBarsPadding(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(modifier = Modifier.padding(top = 60.dp))
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo Title",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)

            )
        }
        Image(
            painter = painterResource(id = R.drawable.main_logo),
            contentDescription = "Logo Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        KakaoLoginButton(
            onKakaoLoginClick = {
                viewModel.handleKaKaoLogin()
            }
        )
    }

}

@Composable
fun KakaoLoginButton(onKakaoLoginClick: () -> Unit) {

    Surface(
        onClick = { onKakaoLoginClick() },
        modifier = Modifier.fillMaxWidth(),
        color = Black
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_kakao),  // 카카오 로그인 아이콘
            contentDescription = "Kakao Login",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .height(50.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun previewLogin() {
    LoginScreen(onLoginSuccess = {}, viewModel = hiltViewModel())

}