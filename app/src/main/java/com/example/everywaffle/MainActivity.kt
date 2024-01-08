package com.example.everywaffle

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
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.ManageAccounts
import androidx.compose.material.icons.sharp.Search
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
import androidx.navigation.NavGraph.Companion.findStartDestination

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
                        // TODO: 게시판으로 넘어가는 로직
                    }
                    .padding(vertical = 8.dp),
                color = Color.Black
            )
            //Divider()
        }

        Divider(modifier = Modifier.padding(vertical = 20.dp))

        Text(
            text = "실시간 인기 글",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        for(i in 0 until 2){
            PopularPost()
        }

        //Spacer(modifier = Modifier.height(16.dp))
    }
}

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

