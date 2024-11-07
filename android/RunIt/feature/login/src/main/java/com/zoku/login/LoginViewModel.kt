package com.zoku.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.zoku.data.NetworkResult
import com.zoku.data.model.LoginData
import com.zoku.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.user.model.User
import com.zoku.data.model.PreferencesKeys.USER_REFRESH
import com.zoku.data.model.PreferencesKeys.USER_TOKEN
import com.zoku.data.model.UserData
import com.zoku.data.repository.DataStoreRepository
import com.zoku.data.repository.UserRepository
import com.zoku.network.model.request.RegisterRequest
import com.zoku.network.model.response.LoginResponse
import kotlinx.coroutines.flow.map

import javax.inject.Inject

private const val TAG = "카카오 로그인"

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val loginRepository: LoginRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginData("", "", ""))

    val uiState: StateFlow<LoginData> = _uiState

    init {
        loginHistoryCheck()
    }

    private fun loginHistoryCheck(){
        // 앱 시작 시 토큰의 존재 여부를 확인하여 로그인 상태 결정
        viewModelScope.launch {
            dataStoreRepository.refreshTokenFlow.collect { refreshToken ->
                if(!refreshToken.isNullOrEmpty()){
                    Log.d(TAG, "imHere:${refreshToken} ")
                    when(val result = loginRepository.postRefresh(
                        refreshToken.removePrefix("Bearer ").trim()
                    )){
                        is NetworkResult.Success -> {
                            Log.d(TAG, "imHere: 2")
                            dataStoreRepository.saveTokens(
                                accessToken =  result.data.data.accessToken,
                                refreshToken = result.data.data.refreshToken
                            )
                            _uiState.value = _uiState.value.copy(isLogin = !refreshToken.isNullOrEmpty())
                            saveUserData()
                        }

                        is NetworkResult.Error -> {
                            Log.d("확인", "실패, 에러 ${result}")
                        }

                        is NetworkResult.Exception -> {
                            Log.d("확인", "실패, 에러 ${result}")
                            Log.d("확인", "서버 연결 에러")
                        }
                    }
                }
            }
        }
    }
    fun handleKaKaoLogin() {

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        Log.i(
                            TAG, "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                        )
                        login(user)
                    }
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(getApplication())) {
            UserApiClient.instance.loginWithKakaoTalk(getApplication()) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        getApplication(),
                        callback = callback
                    )
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        } else if (user != null) {
                            Log.i(
                                TAG, "사용자 정보 요청 성공" +
                                        "\n회원번호: ${user.id}" +
                                        "\n이메일: ${user.kakaoAccount?.email}" +
                                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                            )
                            login(user)
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(getApplication(), callback = callback)
        }
    }

    private fun login(user: User){
        viewModelScope.launch {
            when (val result = loginRepository.postLogin(user.id.toString())) {
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _uiState.value = _uiState.value.copy(
                        userId = "${user.id}",
                        nickName = "${user.kakaoAccount?.profile?.nickname}",
                        image = "${user.kakaoAccount?.profile?.thumbnailImageUrl}",
                        isLogin = true
                    )
                    dataStoreRepository.saveTokens(
                        accessToken = result.data.data.accessToken,
                        refreshToken = result.data.data.refreshToken)
                    saveUserData()
                }

                is NetworkResult.Error -> {
                    Log.d("확인", "실패, 에러 ${result}")
                    signUp(user)
                }

                is NetworkResult.Exception -> {
                    Log.d("확인", "실패, 에러 ${result}")
                    Log.d("확인", "서버 연결 에러")
                }
            }

        }
    }


    private fun signUp(user: User){
        viewModelScope.launch {
            when (val result = loginRepository.postRegister(
                RegisterRequest(
                    userName = user.kakaoAccount!!.profile!!.nickname!!,
                    userNumber = user.id.toString(),
                    userImageUrl = user.kakaoAccount!!.profile!!.thumbnailImageUrl!!

                ) )) {
                is NetworkResult.Success -> {
                    Log.d("회원가입", " 회원가입 성공 ${result}")
                    login(user)
                }

                is NetworkResult.Error -> {
                    Log.d("확인", "실패, 에러 ${result}")
                }

                is NetworkResult.Exception -> {
                    Log.d("확인", "실패, 에러 ${result}")
                    Log.d("확인", "서버 연결 에러")
                }
            }

        }
    }

    private fun saveUserData(){
        viewModelScope.launch {
            when(val result = userRepository.getUserData()){
                is NetworkResult.Success -> {

                    dataStoreRepository.saveUser(
                        UserData(
                            userId = result.data.data.userId,
                            userNumber = result.data.data.userNumber,
                            userName = result.data.data.userName,
                            imageUrl = result.data.data.imageUrl,
                            groupId = result.data.data.groupId

                        ))
                    SendFCM()
                }

                is NetworkResult.Error -> {
                    Log.d("확인", "실패, 에러 ${result}")
                }

                is NetworkResult.Exception -> {
                    Log.d("확인", "실패, 에러 ${result}")
                    Log.d("확인", "서버 연결 에러")
                }
            }
        }
    }

    private fun SendFCM(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM Token", token)
                viewModelScope.launch {
                    when(val result = userRepository.patchFCMToken(token)){
                        is NetworkResult.Success -> {
                            Log.d("확인", "fcm 성공, ${result}")
                        }

                        is NetworkResult.Error -> {
                            Log.d("확인", "실패, 에러 ${result}")
                        }

                        is NetworkResult.Exception -> {
                            Log.d("확인", "실패, 에러 ${result}")
                            Log.d("확인", "서버 연결 에러")
                        }
                    }
                }
            } else {
                Log.w("FCM Token", "Fetching FCM registration token failed", task.exception)
            }
        }
    }
}