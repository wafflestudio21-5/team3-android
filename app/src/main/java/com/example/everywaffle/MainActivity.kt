package com.example.everywaffle

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
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


        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash",keyHash)
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
                onNavigateToDetail = {navController.navigate("Detail")},
                onNavigateToHome = {navController.navigate("Home")}
            )
        }
        /*composable("KakaoSignup/{idToken}",arguments= listOf(navArgument("idToken"){type= NavType.StringType})){
            KakaoSignupScreen(
                onNavigateToInit = {navController.navigate("Init")},
                onNavigateToDetail = {navController.navigate("Detail")},
                onNavigateToHome = {navController.navigate("Home")}
            )
        }*/
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
                onNavigateToHome = {navController.navigate("Home")},
                navController = navController
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
        composable("Detail"){
            DetailScreen(
                onNavigateToInit = {navController.navigate("Init")}
            )
        }
        composable("ChangePassword"){
            PasswordChangeScreen(
                onNavigateToUser = {navController.navigate("User")}
            )
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

// option에서 두번째 요소가 String이면 String 2개 배치 (실제 앱에서 아이디가 이 경우에 속함)
// option에서 두번째 요소가 Int이면 그 숫자에 해당하는 함수 호출
// 함수 목록은 UserScreen.kt의 UserScreen 함수에서 onclicklist에 있고, 이를 UserOptionsSection 함수에서 감지 및 실행

val accountOptions = listOf(
    "아이디" to "",
    "실명 프로필 사진 변경" to "실명 프로필 사진 변경",
    "학교 인증" to "학교 인증",
    "학과 설정" to "학과 설정",
    "학적 처리 내역" to "학적 처리 내역",
    "비밀번호 변경" to 0, //
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
    "로그아웃" to 1
)
/*
@Composable전
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
/*
fun String.isNumber(): Boolean {
    val v = toIntOrNull()
    return when(v) {
        null -> false
        else -> true
    }
}
*/
fun String.isNumber():Boolean = toIntOrNull()!=null
