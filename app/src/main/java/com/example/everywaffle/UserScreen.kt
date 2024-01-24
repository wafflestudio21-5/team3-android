package com.example.everywaffle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.SmsFailed
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//@Preview
@Composable
fun UserScreen(
    onNavigateToBoard: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    navController: NavHostController
) {
    //val userInfo = UserInfo("","","","")
    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                val onclicklist = listOf( // Mainactivity의 option 변수 참조, 순서에 해당하는 함수 생성
                    {navController.navigate("ChangePassword")},
                    {
                        navController.navigate("Init")
                        MyApplication.prefs.reset()
                    },
                    {navController.navigate("ChangeEmail")}
                )

                Header(onNavigateToHome)
                UserInfoSection(
                    realname = MyApplication.prefs.getString("realname"),
                    nickname = MyApplication.prefs.getString("nickname"),
                    department = MyApplication.prefs.getString("department"),
                    studentId = MyApplication.prefs.getString("studentid")
                )
                UserOptionsSection("계정", accountOptions, onclicklist)
                UserOptionsSection("커뮤니티", communityOptions, onclicklist)
                UserOptionsSection("앱 설정", appSettingsOptions, onclicklist)
                UserOptionsSection("이용 안내", usageOptions, onclicklist)
                UserOptionsSection("기타", otherOptions, onclicklist)
            }
        }
    }
}

@Composable
fun UserInfoSection(
    realname:String,
    nickname: String,
    department: String,
    studentId : String
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "사용자 정보",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            UserInfoItem(label = "이름", realname)
            UserInfoItem(label = "닉네임", nickname)
            UserInfoItem(label = "학과", department)
            UserInfoItem(label = "학번", studentId)

        }
    }
}

@Composable
fun UserInfoItem(label : String, value : String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Black)
        Text(text = value, color = Color.Gray)
    }
}
@Composable
fun Header(onNavigateToHome: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onNavigateToHome) {
            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
        }
        Text(
            text = "내 정보",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun UserOptionsSection(title: String, options: List<Pair<String, Any>>, onclicklist:List<() -> Unit>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp, top = 4.dp, start = 4.dp)
            )
            options.forEach { (buttonText, command) ->
                when (command){  // 여기서 option의 형태에 따라 다른 화면 구성
                    is String -> {
                        OptionRow(Text1 = buttonText, Text2 = command)
                    }
                    is Int -> {
                        OptionButton(buttonText = buttonText,onclicklist[command])
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun OptionButton(buttonText: String, onclick: () -> Unit ={}) {
    Text(
        text = buttonText,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onclick.invoke() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        color = Color.Black,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun OptionRow(Text1: String, Text2: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(35.dp)
            .fillMaxWidth()
    ){
        Text(
            text = Text1,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp),
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = Text2,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp),
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PasswordChangeScreen(
    onNavigateToUser : () -> Unit
){
    val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var currentpassword by remember { mutableStateOf("") }
    var newpassword by remember { mutableStateOf("") }
    var passwordwrong by remember { mutableStateOf(false) }
    var changedone by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "비밀번호 변경",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onNavigateToUser
                ) {
                    Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "")
                }
            }

            TextField(
                value = currentpassword,
                onValueChange = { currentpassword = it },
                placeholder = { Text(text = "현재 비밀번호", fontSize = 15.sp) },
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
                value = newpassword,
                onValueChange = { newpassword = it },
                placeholder = { Text(text = "새 비밀번호", fontSize = 15.sp) },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
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

            Button(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    CoroutineScope(Dispatchers.Main).launch {
                        if (currentpassword != MyApplication.prefs.getString("password")) {
                            passwordwrong = true
                        } else {
                            val result = mainViewModel.changepassword(newpw = newpassword)
                            if (result != null) {
                                changedone = true
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
                    text = "비밀번호 변경",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            if (passwordwrong) {
                MakeAlertDialog(
                    onConfirmation = { passwordwrong = false },
                    icon = Icons.Sharp.SmsFailed,
                    title = "비밀번호 변경 실패",
                    content = "현재 비밀번호가 올바르지 않습니다.",
                    confirmtext = "다시 입력하기"
                )
            }

            if (changedone) {
                MakeAlertDialog(
                    onConfirmation = {
                        onNavigateToUser()
                        changedone = false
                    },
                    icon = Icons.Outlined.Check,
                    title = "비밀번호 성공",
                    content = "비밀번호가 변경되었습니다.",
                    confirmtext = "확인"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EmailChangeScreen(
    onNavigateToUser : () -> Unit
){
    val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var newemail by remember { mutableStateOf("") }
    var changedone by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "비밀번호 변경",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onNavigateToUser
                ) {
                    Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "")
                }
            }

            TextField(
                value = MyApplication.prefs.getString("mail"),
                onValueChange = {},
                placeholder = {},
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0x10000000),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    placeholderColor = Color.Gray
                )
            )

            TextField(
                value = newemail,
                onValueChange = { newemail = it },
                placeholder = { Text(text = "새 메일", fontSize = 15.sp) },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
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

            Button(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = mainViewModel.changemail(newmail = newemail)
                        if (result != null) {
                            changedone = true
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
                    text = "메일 변경",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            if (changedone) {
                MakeAlertDialog(
                    onConfirmation = {
                        onNavigateToUser()
                        changedone = false
                    },
                    icon = Icons.Outlined.Check,
                    title = "메일 변경 성공",
                    content = "메일이 변경되었습니다.",
                    confirmtext = "확인"
                )
            }
        }
    }
}
