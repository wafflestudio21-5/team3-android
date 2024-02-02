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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
@Preview
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

    val context = LocalContext.current

    var isLoggingIn by remember { mutableStateOf(false) }
    var loginSuccess by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }


    // 토큰에 로그인 정보가 있는 경우, 앱 시작시 홈 화면으로 바로 이동
    LaunchedEffect(Unit) {
        if (MyApplication.prefs.getString("id") != "-1") {
            onNavigateToHome()
        }
    }

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(106.dp))

            Text(
                text = "함께하는 와플생활",
                modifier = Modifier.height(18.dp).fillMaxWidth(),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.everywafflelogo),
                    tint = Color.Unspecified,
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
                Text(
                    text = "에브리와플",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }

            Spacer(modifier = Modifier.height(55.dp))

            TextField(
                value = signinid,
                onValueChange = { signinid = it },
                placeholder = { Text(text = "아이디", fontSize = 15.sp) },
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(13.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    placeholderColor = Color(0xFFBBBBBB)
                )
            )

            TextField(
                value = signinpw,
                onValueChange = { signinpw = it },
                placeholder = { Text(text = "비밀번호", fontSize = 15.sp) },
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(13.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    placeholderColor = Color(0xFFBBBBBB)
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
                            onNavigateToHome()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFF91F15)),
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
                    .height(42.dp)
            ) {

                Text(
                    text = "에브리와플 로그인",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
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
                colors = ButtonDefaults.buttonColors(Color(0xFFFEE500)),
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .fillMaxWidth()
                    .height(42.dp)
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.kakaologo),
                    contentDescription = "KaKao Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.height(20.dp)
                )
                Spacer(modifier= Modifier.width(8.dp))
                Text(
                    text = "카카오로 로그인",
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
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