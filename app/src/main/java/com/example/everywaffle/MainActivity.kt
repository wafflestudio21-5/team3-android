package com.example.everywaffle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.West
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.Dashboard
import androidx.compose.material.icons.sharp.DeviceUnknown
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.ManageAccounts
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
                onNavigateToHome = {navController.navigate("Home")}
            )
        }
        composable("Signup"){
            SignupScreen(
                onNavigateToInit = {navController.navigate("Init")}
            )
        }
        composable("Home"){
            HomeScreen(
                onNavigateToBoard = {navController.navigate("Board")},
                onNavigateToUser = {navController.navigate("User")}
            )
        }
        composable("Board"){
            BoardScreen()
        }
        composable("User"){
            UserScreen(
                onNavigateToBoard = {navController.navigate("Board")},
                onNavigateToHome = {navController.navigate("Home")}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Preview
@Composable
fun InitScreen(
    onNavigateToSignup: () -> Unit ={},
    onNavigateToHome: () -> Unit ={}
){
    val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var signinid by remember { mutableStateOf("") }
    var signinpw by remember { mutableStateOf("") }

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
                        mainViewModel.signin(signinid, signinpw)
                    }
                    onNavigateToHome()
                },
                colors = ButtonDefaults.buttonColors(Color(0xDFF00000)),
                shape = RoundedCornerShape(10.dp),
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SignupScreen(
    onNavigateToInit : () -> Unit = {}
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var signupdone by remember { mutableStateOf(false) }
    var signupid by remember { mutableStateOf("") }
    var signuppw by remember { mutableStateOf("") }
    var signupemail by remember { mutableStateOf("") }

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
                        mainViewModel.signup(signupid, signuppw, signupemail)
                    }
                    signupdone=true // TODO: 체크 필요
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
                SignupAlertDialog(onConfirmation = onNavigateToInit)
            }
        }
    }
}
@Composable
fun SignupAlertDialog(
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Outlined.Check, contentDescription = "Example Icon")
        },
        title = {
            Text(text = "회원가입 성공")
        },
        text = {
            Text(text = "회원가입이 정상적으로 완료되었습니다.")
        },
        onDismissRequest = {},
        confirmButton = {
            Button(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("확인")
            }
        }
    )
}

@Composable
@Preview
fun HomeScreen(
    onNavigateToBoard : () -> Unit = {},
    onNavigateToUser : () -> Unit = {}
){
    //val mainViewModel = hiltViewModel<MainViewModel>()

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(.1f)
            ){
                Row(
                    modifier = Modifier.weight(.1f)
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "",
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)
                            .border(width = 3.dp, color = Color.Black)
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Sharp.Search, contentDescription = "")
                }
                IconButton(onClick = onNavigateToUser) {
                    Icon(imageVector = Icons.Sharp.ManageAccounts, contentDescription = "")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
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
@Preview
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

@Composable
fun BoardScreen(
    onNavigateToBoard : () -> Unit = {},
    onNavigateToUser : () -> Unit = {}
) {
    //val mainViewModel = hiltViewModel<MainViewModel>()

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {}
}

@Composable
@Preview
fun UserScreen(
    onNavigateToBoard : () -> Unit = {},
    onNavigateToHome : () -> Unit = {}
) {
    //val mainViewModel = hiltViewModel<MainViewModel>()

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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
    }
}