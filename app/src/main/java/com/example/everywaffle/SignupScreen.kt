package com.example.everywaffle

import android.util.Log
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
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.sharp.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
@Preview

fun SignupScreen(
    onNavigateToInit : () -> Unit = {},
    onNavigateToDetail : () -> Unit = {},
    onNavigateToHome : () -> Unit = {}
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var signupdone by remember { mutableStateOf(false) }
    var signupfail by remember { mutableStateOf(false) }
    var signupid by remember { mutableStateOf("") }
    var signuppw by remember { mutableStateOf("") }
    var signuppwcheck by remember { mutableStateOf("") }
    var signupemail by remember { mutableStateOf("") }
    var signupable by remember { mutableStateOf(1) } // 1이면 빈 입력 칸이 있음, 2면 비밀번호와 확인 불일치, 3이면 회원가입 가능

    LaunchedEffect(signupid, signuppw, signuppwcheck, signupemail){
        signupable = when{
            (signupid.isEmpty() || signuppw.isEmpty() || signuppwcheck.isEmpty() || signupemail.isEmpty()) -> 1
            (signuppwcheck != signuppw) -> 2
            else -> 3
        }
    }

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "", modifier = Modifier.width(40.dp))
                Text(
                    text = "회원가입",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onNavigateToInit
                ){
                    Icon(painter = painterResource(id = R.drawable.union), "")
                }
            }

            Spacer(Modifier.height(23.dp))

            Text(
                text = "아이디",
                color = Color(0xFF616161),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 28.dp, bottom = 8.dp)
            )

            TextField(
                value = signupid,
                onValueChange = { signupid = it },
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

            Spacer(Modifier.height(20.dp))

            Text(
                text = "이메일",
                color = Color(0xFF616161),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 28.dp, bottom = 8.dp)
            )

            TextField(
                value = signupemail,
                onValueChange = { signupemail = it },
                placeholder = { Text(text = "everywaffle@happy.com", fontSize = 15.sp) },
                modifier = Modifier
                    .padding(horizontal = 18.dp)
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

            Spacer(Modifier.height(20.dp))

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "비밀번호",
                        color = Color(0xFF616161),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "영문, 숫자, 특문이 2종류 이상 조합된 8~20자",
                        color = Color(0xFF979797),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextField(
                    value = signuppw,
                    onValueChange = { signuppw = it },
                    placeholder = { Text(text = "비밀번호", fontSize = 15.sp) },
                    modifier = Modifier
                        .padding(horizontal = 18.dp, vertical = 5.dp)
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
                    value = signuppwcheck,
                    onValueChange = { signuppwcheck = it },
                    placeholder = { Text(text = "비밀번호 확인", fontSize = 15.sp) },
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

                if(signupable==2){
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = "비밀번호가 일치하지 않습니다.",
                        color = Color(0xFFF91F15),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .padding(horizontal = 25.dp)
                    )
                }

                Spacer(Modifier.height(40.dp))
                Button(
                    onClick = {
                        if(signupable==3) {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            CoroutineScope(Dispatchers.Main).launch {
                                val result = mainViewModel.signup(signupid, signuppw, signupemail)
                                if (result != null) {
                                    signupdone = true
                                } else {
                                    signupfail = true
                                }
                            }
                        }
                    },
                    colors = if(signupable==3) ButtonDefaults.buttonColors(Color(0xFFF91F15))
                    else ButtonDefaults.buttonColors(Color(0xFFF2F2F2)),
                    shape = RoundedCornerShape(13.dp),
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .fillMaxWidth()
                        .height(42.dp)
                ) {
                    Text(
                        text = "에브리와플 회원가입",
                        color = if(signupable==3) Color.White else Color(0xFFBBBBBB),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (signupdone == true) {
                    MakeAlertDialog(
                        onConfirmation = {
                            onNavigateToDetail()
                            signupdone = false
                        },
                        icon = Icons.Outlined.Check,
                        title = "회원가입 성공",
                        content = "회원가입이 정상적으로 완료되었습니다.",
                        confirmtext = "사용자 정보 입력"
                    )
                }

                if (signupfail == true) {
                    MakeAlertDialog(
                        onConfirmation = {
                            signupfail = false
                        },
                        icon = Icons.Sharp.ErrorOutline,
                        title = "회원가입 실패",
                        content = "이미 사용되고 있는 아이디/이메일 입니다.",
                        confirmtext = "다시하기"
                    )
                }
            }
        }
    }
}