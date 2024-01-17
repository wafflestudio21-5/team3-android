package com.example.everywaffle

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.West
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.Dashboard
import androidx.compose.material.icons.sharp.DeviceUnknown
import androidx.compose.material.icons.sharp.ErrorOutline
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.ManageAccounts
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material.icons.sharp.SmsFailed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.RememberObserver
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            MaterialTheme {
                val navController = rememberNavController()
                MyAppNavHost(
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination:String = "Init"
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ){
        composable("Init"){
            InitScreen(
                onNavigateToSignup = { navController.navigate("Signup") },
                onNavigateToHome = {navController.navigate("Home")},
                onNavigateToDetail = {navController.navigate("Detail")}
            )
        }
        composable("Signup"){
            SignupScreen(
                onNavigateToInit = {navController.navigate("Init")},
                onNavigateToDetail = {navController.navigate("Detail")}
            )
        }
        composable("Home"){
            HomeScreen(
                navController = navController,
                onNavigateToBoard = {navController.navigate("AllBoards")},
                onNavigateToUser = {navController.navigate("User")}
            )
        }
        composable("User"){
            UserScreen(
                onNavigateToBoard = {navController.navigate("AllBoards")},
                onNavigateToHome = {navController.navigate("Home")}
            )
        }
        composable("Search"){
            SearchScreen(
                onNavigateBack={navController.popBackStack()},
                onNavigateToHome = {navController.navigate("Home")}
            )
        }
        composable("AllBoards"){
            AllBoards(
                navController = navController,
                onNavigateToHome = {navController.navigate("Home")},
                onNavigateToUser = {navController.navigate("User")}
            )
        }
        composable("Detail"){
            DetailScreen(
                onNavigateToInit = {navController.navigate("Init")}
            )
        }
    }
}

suspend fun UserApiClient.Companion.loginWithKakao(context: Context): OAuthToken {
    return if (instance.isKakaoTalkLoginAvailable(context)) {
        try {
            loginWithKakaoTalk(context)
        } catch (e: ClientError) {
            if (e.reason == ClientErrorCause.Cancelled) {
                throw e
            } else {
                loginWithKakaoAccount(context)
            }
        } catch (e: Throwable) {
            loginWithKakaoAccount(context)
        }
    } else {
        loginWithKakaoAccount(context)
    }
}

suspend fun UserApiClient.Companion.loginWithKakaoTalk(context: Context): OAuthToken {
    return suspendCancellableCoroutine { continuation ->
        instance.loginWithKakaoTalk(context) { token, error ->
            when {
                error != null -> continuation.cancel(error)
                token != null -> continuation.resume(token)
                else -> continuation.cancel(RuntimeException("Fail to access kakao"))
            }
        }
    }
}
suspend fun UserApiClient.Companion.loginWithKakaoAccount(context: Context): OAuthToken {
    return suspendCancellableCoroutine { continuation ->
        instance.loginWithKakaoAccount(context) { token, error ->
            when {
                error != null -> continuation.cancel(CancellationException("Kakao login failed", error))
                token != null -> continuation.resume(token)
                else -> continuation.cancel(RuntimeException("Fail to access Kakao."))
            }
        }
        continuation.invokeOnCancellation {}
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Preview
@Composable
fun InitScreen(
    onNavigateToSignup: () -> Unit ={},
    onNavigateToHome: () -> Unit ={},
    onNavigateToDetail: () -> Unit ={}
){
    val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var signinid by remember { mutableStateOf("") }
    var signinpw by remember { mutableStateOf("") }
    var signinfail by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ){
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
                onValueChange = {signinid = it},
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
                onValueChange = {signinpw = it},
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

            Button(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val oAuthToken = UserApiClient.loginWithKakao(context)
                            Log.i("KakaoLogin", "Login successful: $oAuthToken")
                            onNavigateToDetail()  // 성공시 Detail 화면으로 이동
                        } catch (error: Throwable) {
                            Log.e("KakaoLogin", "Login failed", error)
                            //TODO: 로그인 실패시 처리 로직
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFFFEB3B)),
                shape = RectangleShape,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ){
                Text(
                    text = "에브리와플 로그인",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    /*TODO: 카카오 로그인()*/
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFFFEB3B)),
                shape = RectangleShape,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ){
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

            if(signinfail){
                MakeAlertDialog(
                    onConfirmation = {signinfail=false},
                    icon = Icons.Sharp.SmsFailed,
                    title = "로그인 실패",
                    content = "잘못된 아이디 또는 비밀번호입니다.",
                    confirmtext = "다시 입력하기"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Preview
@Composable
fun DetailScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    onNavigateToInit: () -> Unit = {}
){
    //val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var realname by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier.padding(15.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "사용자 정보 입력",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            TextField(
                value = realname,
                onValueChange = {realname = it},
                placeholder = { Text(text = "이름", fontSize = 15.sp) },
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
                value = nickname,
                onValueChange = {nickname = it},
                placeholder = { Text(text = "닉네임", fontSize = 15.sp) },
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
                value = department,
                onValueChange = {department = it},
                placeholder = { Text(text = "학과", fontSize = 15.sp) },
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
                value = studentId,
                onValueChange = {
                    if(it.isNumber()) studentId = it},
                placeholder = { Text(text = "학번", fontSize = 15.sp) },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
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
                    /*
                    CoroutineScope(Dispatchers.Main).launch {
                        mainViewModel.signup(signupid, signuppw, signupemail)
                    }
                    signupdone=false // TODO: 체크 필요
                    */
                    CoroutineScope(Dispatchers.IO).launch{
                        mainViewModel.updateUserInfo(realname,nickname,department,studentId)
                    }
                    onNavigateToInit()
                },
                colors = ButtonDefaults.buttonColors(Color(0xDFF00000)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ){
                Text(
                    text = "정보 입력 완료",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            /*
            if(signupdone==true){
                MakeAlertDialog(
                    onConfirmation = onNavigateToInit,
                    icon = Icons.Outlined.Check,
                    title = "회원가입 성공",
                    content = "회원가입이 정상적으로 완료되었습니다.",
                    confirmtext = "확인"
                )
            }
            */
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SignupScreen(
    onNavigateToInit : () -> Unit = {},
    onNavigateToDetail : () -> Unit = {}
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var signupdone by remember { mutableStateOf(false) }
    var signupfail by remember { mutableStateOf(false) }
    var signupid by remember { mutableStateOf("") }
    var signuppw by remember { mutableStateOf("") }
    var signupemail by remember { mutableStateOf("") }

    var realname by remember{ mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier.padding(15.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "회원가입",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onNavigateToInit
                ) {
                    Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "")
                }
            }

            TextField(
                value = signupid,
                onValueChange = {signupid = it},
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
                value = signuppw,
                onValueChange = {signuppw = it},
                placeholder = { Text(text = "비밀번호", fontSize = 15.sp) },
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
                value = signupemail,
                onValueChange = {signupemail = it},
                placeholder = { Text(text = "메일", fontSize = 15.sp) },
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

            Button(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = mainViewModel.signup(signupid, signuppw, signupemail)
                        if (result!=null){
                            mainViewModel.updateUserInfo(realname,nickname,department,studentId)
                            signupdone=true
                            onNavigateToDetail()
                        }
                        else{
                            signupfail=true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xDFF00000)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ){
                Text(
                    text = "에브리와플 회원가입",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            if(signupdone==true){
                MakeAlertDialog(
                    onConfirmation = {
                        onNavigateToDetail()
                        signupdone=false},
                    icon = Icons.Outlined.Check,
                    title = "회원가입 성공",
                    content = "회원가입이 정상적으로 완료되었습니다.",
                    confirmtext = "사용자 정보 입력"
                )
            }

            if(signupfail==true){
                MakeAlertDialog(
                    onConfirmation = {
                        signupfail=false},
                    icon = Icons.Sharp.ErrorOutline,
                    title = "회원가입 실패",
                    content = "이미 사용되고 있는 아이디입니다.",
                    confirmtext = "다시하기"
                )
            }
        }
    }
}

@Composable
fun MakeAlertDialog(
    onConfirmation: () -> Unit,
    icon:ImageVector,
    title:String,
    content:String,
    confirmtext:String = "확인"
){
    AlertDialog(
        icon = {
            Icon(imageVector = icon, contentDescription = "Icon")
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = content)
        },
        onDismissRequest = {},
        confirmButton = {
            Button(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(confirmtext)
            }
        }
    )
}

@Composable
//@Preview
fun HomeScreen(
    navController:NavHostController,
    onNavigateToBoard : () -> Unit = {},
    onNavigateToUser : () -> Unit = {}
){
    //val mainViewModel = hiltViewModel<MainViewModel>()

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Row() {
                Row(
                    modifier = Modifier.weight(.1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "",
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)
                            .border(width = 3.dp, color = Color.Black)
                    )
                }
                IconButton(onClick = { navController.navigate("Search") }) {
                    Icon(imageVector = Icons.Sharp.Search, contentDescription = "Search")
                }
                IconButton(onClick = onNavigateToUser) {
                    Icon(imageVector = Icons.Sharp.ManageAccounts, contentDescription = "")
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(580.dp)
            ){
                item {
                    BoardList(navController = navController)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButtonWithText(
                    imageVector = Icons.Sharp.Home,
                    text = "홈"
                )
                IconButtonWithText(
                    imageVector = Icons.Sharp.Dashboard,
                    text = "게시판",
                    onclick = onNavigateToBoard
                )
                IconButtonWithText(
                    imageVector = Icons.Sharp.ManageAccounts,
                    text = "마이페이지",
                    onclick = onNavigateToUser
                )
            }
        }
    }
}

@Composable
fun IconButtonWithText(
    imageVector: ImageVector = Icons.Sharp.DeviceUnknown,
    text:String = "",
    onclick : () -> Unit = {}
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        IconButton(onClick = onclick) {
            Icon(imageVector = imageVector, contentDescription = "")
        }
        Text(
            text = text
        )
    }
}



@Preview
@Composable
fun UserScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    onNavigateToBoard: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val userInfo = UserInfo("","","","")
    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Header(onNavigateToHome)
                UserInfoSection(
                    realname = userInfo.name,
                    nickname = userInfo.nickname,
                    department = userInfo.department,
                    studentId = userInfo.studentId
                )
                UserOptionsSection("계정", accountOptions)
                UserOptionsSection("커뮤니티", communityOptions)
                UserOptionsSection("앱 설정", appSettingsOptions)
                UserOptionsSection("이용 안내", usageOptions)
                UserOptionsSection("기타", otherOptions)
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
fun UserOptionsSection(title: String, options: List<Pair<String, String>>) {
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
            options.forEach { (_, buttonText) ->
                OptionButton(buttonText = buttonText)
            }
        }
    }
}

@Composable
fun OptionButton(buttonText: String) {
    Text(
        text = buttonText,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: 화면 이동 로직 */ }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        color = Color.Black,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
}

val accountOptions = listOf(
    "아이디" to "아이디",
    "실명 프로필 사진 변경" to "실명 프로필 사진 변경",
    "학교 인증" to "학교 인증",
    "학과 설정" to "학과 설정",
    "학적 처리 내역" to "학적 처리 내역",
    "비밀번호 변경" to "비밀번호 변경",
    "이메일 변경" to "이메일 변경"
)

val communityOptions = listOf(
    "닉네임 설정" to "닉네임 설정",
    "게시판 프로필 사진 변경" to "게시판 프로필 사진 변경",
    "이용 제한 내역" to "이용 제한 내역",
    "쪽지 설정" to "쪽지 설정",
    "커뮤니티 이용규칙" to "커뮤니티 이용규칙"
)

val appSettingsOptions = listOf(
    "다크 모드" to "다크 모드",
    "알림 설정" to "알림 설정",
    "암호 잠금" to "암호 잠금",
    "캐시 삭제" to "캐시 삭제"
)

val usageOptions = listOf(
    "앱 버전" to "앱 버전",
    "문의하기" to "문의하기",
    "공지사항" to "공지사항",
    "서비스 이용약관" to "서비스 이용약관",
    "개인정보 처리방침" to "개인정보 처리방침",
    "청소년 보호정책" to "청소년 보호정책",
    "오픈소스 라이선스" to "오픈소스 라이선스"
)

val otherOptions = listOf(
    "정보 동의 설정" to "정보 동의 설정",
    "회원 탈퇴" to "회원탈퇴",
    "로그아웃" to "로그아웃"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack:()->Unit,
    onNavigateToHome : () -> Unit = {}
){
    var searchQuery by remember { mutableStateOf("") }
    Surface (modifier = Modifier.fillMaxSize()){
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateToHome) {
                    Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            OutlinedTextField(
                value = searchQuery ,
                onValueChange = { newText ->
                    searchQuery = newText
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = {Text("검색어를 입력하세요. ")},
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick ={/*TODO*/}){
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {/*TODO*/}
                )
            )
            //TODO:검색 결과 표시
        }
    }
}

@Composable
fun BoardList(navController: NavHostController) {
    val boardNames = listOf("자유게시판", "비밀게시판", "졸업생게시판", "새내기게시판", "시사·이슈", "장터게시판", "정보게시판")

    Column(modifier = Modifier.padding(bottom = 100.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "즐겨찾는 게시판",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "더 보기 >",
                fontSize = 10.sp,
                modifier = Modifier
                    .clickable {
                        navController.navigate("AllBoards")
                    }
            )
        }
        Divider(modifier = Modifier.padding(vertical = 4.dp))
        boardNames.forEach { name ->
            Text(
                text = name,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        when (name) {
                            "자유게시판" -> navController.navigate("FreeBoardContent")
                            "비밀게시판" -> navController.navigate("SecretBoardContent")
                            "졸업생게시판" -> navController.navigate("GraduateBoardContent")
                            "새내기게시판" -> navController.navigate("NewbieBoardContent")
                            "시사·이슈" -> navController.navigate("IssueBoardContent")
                            "장터게시판" -> navController.navigate("MarketBoardContent")
                            "정보게시판" -> navController.navigate("InformationBoardContent")
                            else -> {
                                // 아무 동작도 하지 않음
                            }
                        }
                    }
                    .padding(vertical = 8.dp),
                color = Color.Black
            )
        }
        Divider(modifier = Modifier.padding(vertical = 20.dp))
        Text(
            text = "실시간 인기 글",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        for (i in 0 until 2) {
            PopularPost()
        }
    }
}
/*
@Composable
fun FreeBoardContent() {
    Text(
        text = "자유게시판",
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.Black
    )
}

@Composable
fun SecretBoardContent() {
    Text(
        text = "비밀게시판",
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.Black
    )
}

@Composable
fun GraduateBoardContent() {
    Text(
        text = "졸업생게시판",
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.Black
    )
}
*/
@Composable
fun PopularPost() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        //elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .clickable {
                    // TODO: 상세 게시글로 넘어가는 로직
                }
                .padding(16.dp)
        ) {
            Text("게시글 제목", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("게시글 내용 미리보기...", color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("01/02 01:22", fontSize = 12.sp, color = Color.Gray)
                Row {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Likes", tint = Color.Red)
                    Text("공감", modifier = Modifier.padding(start = 4.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = Icons.Default.Comment, contentDescription = "Comments", tint = Color.Gray)
                    Text("댓글", modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}

@Composable
fun AllBoards(
    navController: NavHostController,
    onNavigateToHome : () -> Unit = {},
    onNavigateToUser : () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 100.dp)
            .padding(15.dp)
    ) {
        val boardList = listOf("내가 쓴 글", "댓글 단 글", "스크랩", "HOT 게시판", "BEST 게시판")
        val boardNames = listOf("자유게시판", "비밀게시판", "졸업생게시판", "새내기게시판", "시사·이슈", "장터게시판", "정보게시판")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(540.dp)
        ){
            item {
                boardList.forEach { boardName ->
                    Text(
                        text = boardName,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: 각 게시물로 이동하는 로직
                            }
                            .padding(vertical = 8.dp),
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Divider()
                }

                Text(
                    text = "즐겨찾는 게시판",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                boardNames.forEach { name ->
                    Text(
                        text = name,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: 각 게시판으로 이동하는 로직
                            }
                            .padding(vertical = 8.dp),
                        color = Color.Black
                    )
                    Divider()
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButtonWithText(
                imageVector = Icons.Sharp.Home,
                text = "홈",
                onclick = onNavigateToHome
            )
            IconButtonWithText(
                imageVector = Icons.Sharp.Dashboard,
                text = "게시판"
            )
            IconButtonWithText(
                imageVector = Icons.Sharp.ManageAccounts,
                text = "마이페이지",
                onclick = onNavigateToUser
            )
        }
    }
}

fun String.isNumber(): Boolean {
    val v = toIntOrNull()
    return when(v) {
        null -> false
        else -> true
    }
}