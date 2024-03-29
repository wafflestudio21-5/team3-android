package com.example.everywaffle

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.SmsFailed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun UserScreen(
    onNavigateToBoard: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    navController: NavHostController
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    var towithdraw by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var showAlert by remember{mutableStateOf(false)}
    val onFeaturedNotAvailable={showAlert=true}

    LaunchedEffect(Unit){
        accountOptions[0] = Pair("아이디",MyApplication.prefs.getString("id"))
    }

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
                    {navController.navigate("ChangeEmail")},
                    {navController.navigate("Board/myposts")},
                    {navController.navigate("Board/mycommented")},
                    {navController.navigate("Board/myscrapped")},
                    {towithdraw = true}
                )

                Header(onNavigateToHome)
                UserInfoSection(
                    realname = MyApplication.prefs.getString("realname"),
                    nickname = MyApplication.prefs.getString("nickname"),
                    department = MyApplication.prefs.getString("department"),
                    studentId = MyApplication.prefs.getString("studentid")
                )
                UserOptionsSection("계정", accountOptions, onclicklist,onFeaturedNotAvailable)
                UserOptionsSection("게시글", postOptions, onclicklist,onFeaturedNotAvailable)
                UserOptionsSection("커뮤니티", communityOptions, onclicklist,onFeaturedNotAvailable)
                UserOptionsSection("앱 설정", appSettingsOptions, onclicklist,onFeaturedNotAvailable)
                UserOptionsSection("이용 안내", usageOptions, onclicklist,onFeaturedNotAvailable)
                UserOptionsSection("기타", otherOptions, onclicklist,onFeaturedNotAvailable)
            }
        }
        if(towithdraw) WithdrawDialog(
            ondismiss = {towithdraw = false},
            onagree = {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = mainViewModel.withdraw()
                    if(result==null) {
                        Toast.makeText(context,"탈퇴에 실패했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        MyApplication.prefs.reset()
                        navController.navigate("Init")
                    }
                }
            }
        )

        if (showAlert) {
            AlertFeatureNotAvailableDialog(onDismissRequest = { showAlert = false })
        }
    }
}

@Composable
fun UserInfoSection(
    realname: String,
    nickname: String,
    department: String,
    studentId: String
) {
    Column( modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 16.dp)
        .background(
            Color.White,
            shape = RoundedCornerShape(12.dp)
        )
        .shadow(1.dp, shape = RoundedCornerShape(12.dp))) {
        Card(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.mypagename),
                    tint = Color.Unspecified,
                    contentDescription = "User Icon",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = realname,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "$department $studentId 학번",
                        fontSize = 15.sp
                    )
                }

            }
        }
        Card(
            shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = nickname,
                    fontSize = 15.sp
                )
            }
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onNavigateToHome) {
            Icon(painter = painterResource(id = R.drawable.backarrow), contentDescription = "Search",
                tint=Color.Unspecified, modifier = Modifier
                    .height(18.dp)
                    .width(18.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "내 정보",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable

fun UserOptionsSection(title: String, options: MutableList<Pair<String, Any?>>, onclicklist:List<() -> Unit>, onFeatureNotAvailable: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp) // Adjust padding as needed
            .background(
                Color.White,
                shape = RoundedCornerShape(12.dp)
            ) // Set the corner radius here
            .shadow(1.dp, shape = RoundedCornerShape(12.dp)), // Optional shadow
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            options.forEach { (buttonText, command) ->
                if(command==null){
                    OptionButton(buttonText = buttonText, onclick = onFeatureNotAvailable)
                }
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
            .clickable(onClick = onclick)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        color = Color.Black,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun AlertFeatureNotAvailableDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text("확인")
            }
        },
        text = {
            Text("추후 개발 예정인 버튼입니다!")
        }
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
//@Preview

fun PasswordChangeScreen(
    onNavigateToUser : () -> Unit
) {
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
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "비밀번호 변경",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,

                    )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = onNavigateToUser
                ) {
                    Icon(painter = painterResource(id = R.drawable.ex), contentDescription = "Search",
                        tint=Color.Unspecified, modifier = Modifier
                            .height(18.dp)
                            .width(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = "새 비밀번호",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(80.dp))
                    Text(
                        text = "영문, 숫자, 특문이 2종류 이상 조합된 8~20자",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }



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

                TextField(
                    value = newpassword,
                    onValueChange = { newpassword = it },
                    placeholder = { Text(text = "새 비밀번호 확인", fontSize = 15.sp) },
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

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = "현재 비밀번호",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
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

                Spacer(modifier = Modifier.height(20.dp))

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
                    shape = RoundedCornerShape(15.dp),
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
                            //onNavigateToUser()
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
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EmailChangeScreen(
    onNavigateToUser: () -> Unit
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var newemail by remember { mutableStateOf("") }
    var changedone by remember { mutableStateOf(false) }
    val currentEmail = remember { mutableStateOf(MyApplication.prefs.getString("mail", "")) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(15.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "이메일 변경",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    )
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = onNavigateToUser
                ) {
                    Icon(painter = painterResource(id = R.drawable.ex), contentDescription = "Search",
                        tint=Color.Unspecified, modifier = Modifier
                            .height(18.dp)
                            .width(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {
                Text(
                    text = "이메일",
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            TextField(
                value = currentEmail.value,
                onValueChange = { currentEmail.value = it },
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

            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {
                Text(
                    text = "새 이메일",
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            TextField(
                value = newemail,
                onValueChange = { newemail = it },
                placeholder = { Text(text = "새 이메일", fontSize = 15.sp) },
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


            Spacer(modifier = Modifier.height(16.dp))

            // Submit button
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
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "이메일 변경",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            // Alert Dialog
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
