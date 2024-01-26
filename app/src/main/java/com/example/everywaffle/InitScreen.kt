package com.example.everywaffle

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.SmsFailed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun savetoken(context: Context, token: String) {
    val sharedPref = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    with (sharedPref.edit()) {
        putString("Token", token)
        apply()
    }
}

fun checkloginstatus(context: Context): Boolean {
    val sharedPref = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    return sharedPref.getString("Token", null) != null
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Preview
@Composable


fun InitScreen(
    onNavigateToSignup: () -> Unit ={},
    onNavigateToHome: () -> Unit ={},
    onNavigateToDetail: () -> Unit ={}
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val viewModel:MainViewModel= hiltViewModel()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var signinid by remember { mutableStateOf("") }
    var signinpw by remember { mutableStateOf("") }
    var signinfail by remember { mutableStateOf(false) }

    //val kakaologin = Kakaologin()
    val context = LocalContext.current

    var isLoggingIn by remember { mutableStateOf(false) }
    var loginSuccess by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }


    // 토큰에 로그인 정보가 있는 경우, 앱 시작시 홈 화면으로 바로 이동
    LaunchedEffect(Unit) {
        if (MyApplication.prefs.getString("token") != "-1") {
            onNavigateToHome()
        }
    }

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(70.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    modifier = Modifier
                        .height(50.dp)
                        .border(width = 3.dp, color = Color.Black)
                )
                Text(
                    text = "에브리와플",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            TextField(
                value = signinid,
                onValueChange = { signinid = it },
                placeholder = { Text(text = "아이디", fontSize = 15.sp) },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0x10000000),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    placeholderColor = Color.Gray
                )
            )

            TextField(
                value = signinpw,
                onValueChange = { signinpw = it },
                placeholder = { Text(text = "비밀번호", fontSize = 15.sp) },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0x10000000),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    placeholderColor = Color.Gray
                )
            )

            // 일반 로그인
            Button(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = mainViewModel.signin(signinid,signinpw)
                        if(result==null){ // 로그인 실패
                            signinfail=true
                        }
                        else{
                            val result2 = mainViewModel.getUserInfo()
                            if(result2==null){ // 입력된 사용자 정보가 없는 경우
                                onNavigateToDetail()
                            }
                            else {
                                onNavigateToHome()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xF0FF0000)),
                shape = RectangleShape,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "에브리와플 로그인",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            // 카카오 로그인
            Button(
                onClick = {
                    viewModel.loginWithKakao(context,
                        onSuccess = {
                            onNavigateToHome()
                        },
                        onError = { errorMessage ->
                            Log.e("LOGIN", errorMessage)
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFFFEB3B)),
                shape = RectangleShape,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            )
            {
                Text(
                    text = "카카오계정으로 로그인",
                    color = Color.Black,
                    fontSize = 15.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onNavigateToSignup,
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = "회원가입",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (signinfail) { // 로그인 실패시 뜨는 alert
                MakeAlertDialog(
                    onConfirmation = { signinfail = false },
                    icon = Icons.Sharp.SmsFailed,
                    title = "로그인 실패",
                    content = "잘못된 아이디 또는 비밀번호입니다.",
                    confirmtext = "다시 입력하기"
                )
            }
        }
    }
}