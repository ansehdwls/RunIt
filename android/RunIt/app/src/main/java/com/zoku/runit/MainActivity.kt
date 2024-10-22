package com.zoku.runit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zoku.home.HomeScreen
import com.zoku.login.LoginScreen
import com.zoku.ranking.RankingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            com.zoku.ui.RunItTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var isUserLoggedIn by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (isUserLoggedIn) {
                // 로그인 후에만 메뉴가 나타남
                ToggleAppBar(modifier, navController)
            }
        }
    ) { innerPadding ->
        MyNavHost(navController = navController, onLoginSuccess = {
            // 로그인 성공 시, 상태 업데이트
            isUserLoggedIn = true
            navController.navigate("home")
        })
    }

}

@Composable
fun MyNavHost(navController: NavHostController, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("home") {
            // LaunchedEffect로 'home'으로 네비게이션할 때 한 번만 Toast 표시
            LaunchedEffect(Unit) {
                Toast.makeText(context, "aaa", Toast.LENGTH_SHORT).show()
            }
            HomeScreen()
        }
        composable("ranking") {
            RankingScreen()
        }
        composable("login") {
            LoginScreen(onLoginSuccess = onLoginSuccess)
        }
    }
}

@Composable
fun ToggleAppBar(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .height(36.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = com.zoku.ui.BaseGray,
                    shape = RoundedCornerShape(8.dp)
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            MenuButton(name = "정보", backgroundColor = com.zoku.ui.BaseYellow) {
                navController.navigate("home")
            }

            Spacer(modifier = Modifier.width(8.dp))

            MenuButton(name = "랭킹", backgroundColor = com.zoku.ui.BaseWhiteBackground) {
                navController.navigate("ranking")
            }

            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun MenuButton(name: String, backgroundColor: Color, onClick: (String) -> Unit) {
    Button(
        modifier = Modifier
            .height(32.dp)
            .padding(horizontal = 16.dp),
        onClick = { onClick(name) },
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = name)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    com.zoku.ui.RunItTheme {
        MainScreen()
    }
}