package com.example.everywaffle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.material.icons.sharp.DeviceUnknown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

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
                navController = navController
            )
        }
        composable("User"){
            UserScreen(
                onNavigateToBoard = {navController.navigate("AllBoards")},
                onNavigateToHome = {navController.navigate("Home")},
                navController = navController
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
        composable("ChangeEmail"){
            EmailChangeScreen(
                onNavigateToUser = {navController.navigate("User")}
            )
        }
        composable("Chat"){
            ChatScreen(navController = navController)
        }
        composable(
            route = "SendScreen/{session_id}/{sender_id}",
            arguments = listOf(
                navArgument("session_id"){
                    type= NavType.IntType
                },
                navArgument("sender_id"){
                    type= NavType.IntType
                }
            )
        ){backstackEntry ->
            SendScreen(
                navController=navController,
                sessionid = backstackEntry.arguments?.getInt("session_id"),
                senderid = backstackEntry.arguments?.getInt("sender_id")
            )
        }
        composable(
            route = "Message/{session_id}",
            arguments = listOf(
                navArgument("session_id"){
                    type= NavType.IntType
                }
            )
        ){backstackEntry ->
            MessageView(
                sessionid = backstackEntry.arguments?.getInt("session_id"),
                navController = navController
            )
        }
        composable(
            route ="Search/{board_id}",
            arguments = listOf(
                navArgument("board_id"){
                    type= NavType.StringType
                }
            )
        ){backstackEntry ->
            SearchScreen(
                navController = navController,
                boardid = backstackEntry.arguments?.getString("board_id")
            )
        }
        composable(
            route="Board/{board_id}",
            arguments = listOf(
                navArgument("board_id"){
                    type= NavType.StringType
                }
            )
        ){backstackEntry ->
            BoardScreen(
                boardid = backstackEntry.arguments?.getString("board_id"),
                navController = navController
            )
        }
        composable(
            route="Post/{post_id}",
            arguments = listOf(
                navArgument("post_id"){
                    type= NavType.IntType
                }
            )
        ){backstackEntry ->
            PostScreen(
                postid = backstackEntry.arguments?.getInt("post_id"),
                navController = navController
            )
        }
        composable(
            route = "CreatePost/{category}",
            arguments = listOf(
                navArgument("category"){
                    type = NavType.StringType
                }
            )
        ){backstackEntry ->
            Createpost(
                navController = navController,
                category = backstackEntry.arguments?.getString("category")
            )
        }
        composable(
            route = "UpdatePost/{category}/{postId}/{pretitle}/{precontent}",
            arguments = listOf(
                navArgument("category"){
                    type = NavType.StringType
                },
                navArgument("postId"){
                    type = NavType.IntType
                },
                navArgument("pretitle"){
                    type = NavType.StringType
                },
                navArgument("precontent"){
                    type = NavType.StringType
                }
            )
        ){backstackEntry ->
            UpdatePost(
                navController = navController,
                category = backstackEntry.arguments?.getString("category"),
                postid = backstackEntry.arguments?.getInt("postId"),
                pretitle = backstackEntry.arguments?.getString("pretitle"),
                precontent = backstackEntry.arguments?.getString("precontent")
            )
        }
    }
}
/*
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
 */


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

// 임시

@Composable
fun IconButtonWithText(
    imageVector: Painter,
    text:String = "",
    size: Dp = 48.dp,
    onclick : () -> Unit = {}
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        IconButton(onClick = onclick) {
            Icon(painter = imageVector, contentDescription = "",
                modifier = Modifier.size(size),
                tint = Color.Unspecified)
        }
        Text(
            text = text
        )
    }
}

// option에서 두번째 요소가 String이면 String 2개 배치 (실제 앱에서 아이디가 이 경우에 속함)
// option에서 두번째 요소가 Int이면 그 숫자에 해당하는 함수 호출
// 함수 목록은 UserScreen.kt의 UserScreen 함수에서 onclicklist에 있고, 이를 UserOptionsSection 함수에서 감지 및 실행

var accountOptions: MutableList<Pair<String, Any?>> = mutableListOf(
    Pair("아이디" , null),
    "실명 프로필 사진 변경" to null ,
    "학교 인증" to null,
    "학과 설정" to null,
    "학적 처리 내역" to null,
    "비밀번호 변경" to 0, //
    "이메일 변경" to 2
)

val postOptions: MutableList<Pair<String, Any?>> = mutableListOf(
    "내가 쓴 글" to 3,
    "댓글 단 글" to 4,
    "스크랩" to 5
)

val communityOptions: MutableList<Pair<String, Any?>> = mutableListOf(
    "닉네임 설정" to null,
    "게시판 프로필 사진 변경" to null,
    "이용 제한 내역" to null,
    "쪽지 설정" to null,
    "커뮤니티 이용규칙" to null
)

val appSettingsOptions: MutableList<Pair<String, Any?>> = mutableListOf(
    "다크 모드" to null,
    "알림 설정" to null,
    "암호 잠금" to null,
    "캐시 삭제" to null
)

val usageOptions : MutableList<Pair<String, Any?>> = mutableListOf(
    "앱 버전" to null,
    "문의하기" to null,
    "공지사항" to null,
    "서비스 이용약관" to null,
    "개인정보 처리방침" to null,
    "청소년 보호정책" to null,
    "오픈소스 라이선스" to null
)

val otherOptions : MutableList<Pair<String, Any?>> = mutableListOf(
    "정보 동의 설정" to null,
    "회원 탈퇴" to 6,
    "로그아웃" to 1
)

val boardnames = mapOf(
    "자유게시판" to "FREE_BOARD",
    "비밀게시판" to "SECRET_BOARD",
    "졸업생게시판" to "GRADUATE_BOARD",
    "새내기게시판" to "FRESHMAN_BOARD",
    "정보게시판" to "INFO_BOARD",
    "시사·이슈" to "ISSUE_BOARD",
    "장터게시판" to "MARKET_BOARD",
    "투표게시판" to "VOTE_BOARD"
)

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
fun PopularPost(
    navController: NavHostController,
    post:PostDetail
) {
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
                    navController.navigate("Post/${post.postId}")
                }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Sharp.AccountBox,
                        contentDescription = "User",
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                    )
                    Text(
                        text = "익명",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 5.dp, top = 8.dp)
                    )
                }
                Row {
                    Text(timetoprint(post.createdAt), fontSize = 12.sp, color = Color.Gray)
                }
            }
            Text(post.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(post.content, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(boardnames.filterValues { it == post.category }.keys.firstOrDefault(), fontSize = 12.sp, color = Color.Gray)
                Row{
                    ReactionNumberView("Like",post.likes,"Normal")
                    Spacer(modifier = Modifier.width(8.dp))
                    ReactionNumberView("Comment",post.comments,"Normal")
                }
            }
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

fun Collection<String>.firstOrDefault(default:String = ""): String {
    return when (this.firstOrNull()==null){
        true -> default
        else -> this.first()
    }
}

fun timetoprint(t:String):String{
    try {
        val time = Instant.parse(t).atZone(ZoneId.of("Asia/Seoul"))
        val timec = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"))

        return when {
            timec.minusMinutes(1).isBefore(time) -> "방금 전"
            timec.minusHours(1).isBefore(time) -> {
                var diff = 0L
                while (true) {
                    diff += 1
                    if (timec.minusMinutes(diff).isBefore(time)) break
                }
                "${diff - 1}분 전"
            }

            timec.dayOfMonth == time.dayOfMonth -> time.toString().slice(11..15)
            timec.minusYears(1).isBefore(time) -> {
                time.toString().slice(5..9).replace("-", "/")
            }

            else -> {
                var diff = 0L
                while (true) {
                    diff += 1
                    if (timec.minusYears(diff).isBefore(time)) break
                }
                "${diff - 1}년 전"
            }
        }
    }
    catch(e:Exception) {return ""}
}
