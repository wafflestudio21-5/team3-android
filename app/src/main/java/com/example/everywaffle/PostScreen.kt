package com.example.everywaffle

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.West
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.ChatBubbleOutline
import androidx.compose.material.icons.sharp.Comment
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material.icons.sharp.Send
import androidx.compose.material.icons.sharp.StarBorder
import androidx.compose.material.icons.sharp.SubdirectoryArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable

fun PostScreen(postid:Int?, navController: NavHostController){
    val mainViewModel = hiltViewModel<MainViewModel>()
    var post by remember { mutableStateOf(PostDetail(-1,0,"","","","",0, 0,0,false,0,0,0)) }
    val comments = remember { mutableStateListOf<ParentComment>() }
    var dropmenuexpanded by remember { mutableStateOf(false) }
    var dropmenuexpanded2 by remember { mutableStateOf(false) }
    var parentcommentid by remember { mutableStateOf(0) }
    val postchangedkey by MainViewModel.postchanged.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if (expanded) 15.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    var voting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit, postchangedkey){
        post = mainViewModel.getpost(postid!!)!!
        comments.clear()
        comments.addAll(mainViewModel.getcomments(post.postId)!!)
    }

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "Back")
                }

                Text(text = boardnames.filterValues { it == post.category }.keys.firstOrDefault(), fontSize = 12.sp, fontWeight = FontWeight.Bold)

                IconButton(onClick = {
                    if(post.userId == MyApplication.prefs.getString("userid").toInt()) dropmenuexpanded = true
                    else dropmenuexpanded2 = true
                }) {
                    Icon(imageVector = Icons.Sharp.MoreVert, contentDescription = "")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    //.height(630.dp)
                    .weight(1f)
            ){
                item { PostView(post = post) }

                item{
                    Row {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 7.dp, start = 5.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .background(Color(0xBEE6E6E6))
                                .clickable {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        val result = mainViewModel.postlike(postid = postid!!)
                                        if (result == null) {
                                            Toast.makeText(context, "이미 공감한 글입니다.",Toast.LENGTH_SHORT).show()
                                        } else {
                                            post = post.copy(likes = post.likes + 1)
                                        }
                                    }
                                },
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = Icons.Sharp.FavoriteBorder,
                                    contentDescription = "",
                                    modifier = Modifier.width(15.dp)
                                )
                                Text(
                                    "공감",
                                    modifier = Modifier.padding(
                                        start = 3.dp,
                                        end = 5.dp,
                                        top = 2.dp,
                                        bottom = 2.dp
                                    )
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 7.dp, start = 5.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .background(Color(0xBEE6E6E6))
                                .clickable {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        val result = mainViewModel.postscrap(postid = postid!!)
                                        if (result == null) {
                                            Toast.makeText(context, "이미 스크랩한 글입니다.",Toast.LENGTH_SHORT).show()
                                        } else {
                                            post = post.copy(scraps = post.scraps + 1)
                                        }
                                    }
                                },
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = Icons.Sharp.StarBorder,
                                    contentDescription = "",
                                    modifier = Modifier.width(15.dp)
                                )
                                Text(
                                    "스크랩",
                                    modifier = Modifier.padding(
                                        start = 3.dp,
                                        end = 5.dp,
                                        top = 2.dp,
                                        bottom = 2.dp
                                    )
                                )
                            }
                        }

                        if(!post.isVoting) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(top = 7.dp, start = 5.dp)
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .background(Color(0xBEE6E6E6))
                                    .clickable {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            val result =
                                                mainViewModel.postmakevote(postid = postid!!)
                                            if (result == null) {
                                                Toast.makeText(context, "이미 투표 글로 선택한 글입니다.",Toast.LENGTH_SHORT).show()
                                            } else {
                                                post =
                                                    post.copy(makeVoteCnt = post.makeVoteCnt + 1) // TODO:
                                            }
                                        }
                                    },
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        painter = painterResource(id = R.drawable.voteborder),
                                        contentDescription = "",
                                        modifier = Modifier.width(15.dp),
                                        tint = Color.Unspecified
                                    )
                                    Text(
                                        text = "투표 글로",
                                        modifier = Modifier.padding(
                                            start = 3.dp,
                                            end = 5.dp,
                                            top = 2.dp,
                                            bottom = 2.dp
                                        )
                                    )
                                }
                            }
                        }

                        else{
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(top = 7.dp, start = 5.dp)
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .background(Color(0xBEE6E6E6))
                                    .clickable {
                                        voting = true
                                    },
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        painter = painterResource(id = R.drawable.voteborder),
                                        contentDescription = "",
                                        modifier = Modifier.width(15.dp),
                                        tint = Color.Unspecified
                                    )
                                    Text(
                                        text = "투표하기",
                                        modifier = Modifier.padding(
                                            start = 3.dp,
                                            end = 5.dp,
                                            top = 2.dp,
                                            bottom = 2.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                if(post.isVoting){
                    item{
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 7.dp)
                                .padding(start = 5.dp)
                                .clip(shape = RoundedCornerShape(7.dp))
                                .background(Color(0xBEE6E6E6)),
                        ){
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                                    .padding(top = 15.dp, start = 10.dp)
                            ) {
                                if (expanded) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(end = 10.dp, bottom = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "투표 결과 보기",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF616161)
                                        )
                                        IconButton(onClick = { expanded = !expanded }, modifier = Modifier.width(15.dp).height(15.dp)) {
                                            Icon(
                                                painter = if (expanded) painterResource(id = R.drawable.showless) else painterResource(
                                                    id = R.drawable.showmore
                                                ),
                                                contentDescription = if (expanded) "Show less" else "Show more",
                                                modifier = Modifier.width(15.dp),
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }
                                    if (post.agree+post.disagree ==0) {
                                        Text(
                                            text = "투표 참여자 : 0명",
                                            fontSize = 9.sp,
                                            color = Color(0xFF616161)
                                        )
                                    }
                                    else{
                                        Text(
                                            text = "투표 참여자 : ${post.agree + post.disagree}명",
                                            fontSize = 9.sp,
                                            color = Color(0xFF616161)
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Icon(
                                                painter = painterResource(id = R.drawable.vote_winning),
                                                contentDescription = "",
                                                tint = if(post.agree>=post.disagree) Color.Unspecified else Color(0xFFBBBBBB),
                                                modifier = Modifier
                                                    .width(7.dp)
                                                    .height(7.dp)
                                            )
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text(
                                                text = "찬성 : ${percentage(post.agree,post.disagree,1)}%",
                                                fontSize = 9.sp,
                                                color = Color(0xFF616161)
                                            )
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Icon(
                                                painter = painterResource(id = R.drawable.vote_winning),
                                                contentDescription = "",
                                                tint = if(post.agree<post.disagree) Color.Unspecified else Color(0xFFBBBBBB),
                                                modifier = Modifier
                                                    .width(7.dp)
                                                    .height(7.dp)
                                            )
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text(
                                                text = "반대 : ${percentage(post.agree,post.disagree,2)}%",
                                                fontSize = 9.sp,
                                                color = Color(0xFF616161)
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .height(25.dp)
                                                .fillMaxWidth()
                                                .padding(end = 15.dp)
                                                .clip(shape = RoundedCornerShape(3.dp))
                                        ){
                                            if(post.agree>0) {
                                                Text(
                                                    text = percentage(
                                                        post.agree,
                                                        post.disagree,
                                                        1
                                                    ).toString(),
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .weight(post.agree.toFloat())
                                                        .background(
                                                            color = if (post.agree >= post.disagree) Color(
                                                                0xFF6C59E4
                                                            ) else Color(0xFFBBBBBB)
                                                        ),
                                                    textAlign = TextAlign.Start,
                                                    color = Color.White
                                                )
                                            }
                                            if(post.disagree>0) {
                                                Text(
                                                    text = percentage(
                                                        post.agree,
                                                        post.disagree,
                                                        2
                                                    ).toString(),
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .weight(post.disagree.toFloat())
                                                        .background(
                                                            color = if (post.agree < post.disagree) Color(
                                                                0xFF6C59E4
                                                            ) else Color(0xFFBBBBBB)
                                                        ),
                                                    textAlign = TextAlign.End,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                                else{
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(end = 10.dp, bottom = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "투표 결과 보기",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF616161)
                                        )
                                        IconButton(onClick = { expanded = !expanded }, modifier = Modifier.width(15.dp).height(15.dp)) {
                                            Icon(
                                                painter = if (expanded) painterResource(id = R.drawable.showless) else painterResource(
                                                    id = R.drawable.showmore
                                                ),
                                                contentDescription = if (expanded) "Show less" else "Show more",
                                                modifier = Modifier.width(15.dp),
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item{Divider(modifier = Modifier.padding(vertical = 4.dp))}

                comments.forEach{
                    item{
                        ParentCommentView(it,
                            {
                                parentcommentid = it.parentcommentid
                                keyboardController?.show()
                            },
                            {
                                CoroutineScope(Dispatchers.Main).launch {
                                    if(it.content=="삭제된 댓글입니다."){
                                        Toast.makeText(context, "삭제된 댓글입니다.",Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        val result = mainViewModel.postcommentlike(it.parentcommentid)
                                        if (result == null) {
                                            Toast.makeText(
                                                context,
                                                "이미 공감한 댓글입니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            if (MainViewModel._postchanged.value) MainViewModel._postchanged.emit(
                                                false
                                            )
                                            else MainViewModel._postchanged.emit(true)
                                        }
                                    }
                                }
                            },
                            {},
                            {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val result = mainViewModel.deletecomment(it.parentcommentid)
                                    if(result==null){
                                        // TODO:
                                    }
                                    else{
                                        if(MainViewModel._postchanged.value) MainViewModel._postchanged.emit(false)
                                        else MainViewModel._postchanged.emit(true)
                                    }
                                }
                            },
                            navController,
                            post.userId
                        )
                    }
                    item{Divider()}
                }
            }


            BottomTextField(
                postid = postid!!,
                parentcommentid = parentcommentid
            )
        }

        DropdownMenu(
            modifier = Modifier.wrapContentSize(),
            expanded = dropmenuexpanded,
            onDismissRequest = {dropmenuexpanded = false},
            offset = DpOffset(260.dp, -820.dp) // 펼쳐지는 메뉴의 위치 조정
        ) {
            DropdownMenuItem(
                text = {Text("수정")},
                onClick = {
                    navController.navigate("UpdatePost/${post.category}/${post.postId}/${post.title}/${post.content}") {
                        popUpTo("BoardScreen") { inclusive = true }
                    }
                }
            )
            DropdownMenuItem(
                text = {Text("삭제")},
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = mainViewModel.deletepost(post.postId)
                        if(result!=null){
                            navController.navigate("Board/${post.category}"){
                                popUpTo("Home")
                            }
                        }
                    }
                }
            )
        }

        DropdownMenu(
            modifier = Modifier.wrapContentSize(),
            expanded = dropmenuexpanded2,
            onDismissRequest = {dropmenuexpanded2 = false},
            offset = DpOffset(260.dp, 820.dp) // 펼쳐지는 메뉴의 위치 조정
        ) {
            DropdownMenuItem(
                text = {Text("쪽지 보내기")},
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = mainViewModel.makesession(post.userId)
                        if(result==null) {} // TODO:
                        else{
                            navController.navigate("SendScreen/${result}/${MyApplication.prefs.getString("userid").toInt()}"){
                                popUpTo("Post/${postid}")
                            }
                        }
                    }
                }
            )
        }

        if(voting){
            VoteDialog(
                ondismiss = {voting = false},
                onagree = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = mainViewModel.postvote(postid = postid!!, vote = "AGREE")
                        if (result == null) {
                            // TODO :
                        }
                        else{
                            if(MainViewModel._postchanged.value) MainViewModel._postchanged.emit(false)
                            else MainViewModel._postchanged.emit(true)
                        }
                    }
                },
                ondisagree = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = mainViewModel.postvote(postid = postid!!, vote = "DISAGREE")
                        if (result == null) {
                            // TODO :
                        }
                        else{
                            if(MainViewModel._postchanged.value) MainViewModel._postchanged.emit(false)
                            else MainViewModel._postchanged.emit(true)
                        }
                    }
                }
            )
        }
    }
}

@Composable
//@Preview
fun PostView(
    post:PostDetail = PostDetail(postId=1, userId=35, title="waffle", content="waffle", category="FREE_BOARD", createdAt="2024-01-18T19:13:04.000+00:00", likes=1, 0,0,false,0,0,0)
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ){
        Row{
            Icon(
                imageVector = Icons.Sharp.AccountBox,
                contentDescription = "User",
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
            )
            Column(
                modifier = Modifier.padding(5.dp)
            ){
                Text(
                    text = "익명",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = timetoprint(post.createdAt),
                    fontSize = 12.sp,
                )
            }
        }

        Text(
            text = post.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 5.dp)
        )

        Text(
            text = post.content,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 5.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.likeicon), contentDescription = "Like", Modifier.size(15.dp),tint=Color(0xFFF91F15))
            Text(text = "${post.likes}", modifier = Modifier.padding(start = 4.dp),color=Color(0xFFF91F15))

            Spacer(modifier = Modifier.width(8.dp))

            Icon(painter = painterResource(id = R.drawable.replyicon), contentDescription = "Comment", Modifier.size(15.dp),tint = Color(0xFF05BCBC))
            Text(text = "${post.comments}", modifier = Modifier.padding(start = 4.dp),color=Color(0xFF05BCBC))

            Spacer(modifier = Modifier.width(8.dp))

            Icon(painter = painterResource(id = R.drawable.staricon), contentDescription = "Scrap", Modifier.size(15.dp),tint = Color(0xFFFFD330))
            Text(text = "${post.scraps}", modifier = Modifier.padding(start = 4.dp),color=Color(0xFFFFD330))

            Spacer(modifier = Modifier.width(8.dp))

            Icon(painter = painterResource(id = R.drawable.voteborder), contentDescription = "MakeVote", Modifier.size(15.dp),tint = Color(0xFF6C59E4))
            Text(text = "${post.makeVoteCnt}", modifier = Modifier.padding(start = 4.dp),color=Color(0xFF6C59E4))
        }
    }
}

@Composable
fun ParentCommentView(
    comment: ParentComment = ParentComment(parentcommentid=2, userId=8, postId=1, content="comment2", createdAt="2024-01-19T15:03:58.000+00:00", childComments=listOf(ChildComment(childcommentid=3, userId=8, postId=1, content="comment3", parentCommentId=2, createdAt="2024-01-19T15:04:15.000+00:00", likes=0), ChildComment(childcommentid=4, userId=8, postId=1, content="comment4", parentCommentId=2, createdAt="2024-01-19T15:04:21.000+00:00", likes=0)), likes=0),
    onclickcomment: () -> Unit = {},
    onclicklike: () -> Unit = {},
    onclickupdate : () -> Unit = {},
    onclickdelete : () -> Unit = {},
    navController: NavHostController,
    posteduserid:Int,
){

    var dropmenuexpanded by remember { mutableStateOf(false) }
    var dropmenuexpanded2 by remember { mutableStateOf(false) }
    val mainViewModel = hiltViewModel<MainViewModel>()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "User",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
                Text(
                    text = if(comment.userId == posteduserid) "익명(글쓴이)" else "익명",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 5.dp, top = 8.dp),
                    color = if(comment.userId == posteduserid) Color(0xFF05BCBC) else Color.Black
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 5.dp, end = 10.dp)
                    .background(color = Color(0xBEE6E6E6), shape = RoundedCornerShape(5.dp))
            ) {
                    Icon(painter = painterResource(id = R.drawable.replyicon),
                        contentDescription = "Comment",
                        modifier = Modifier
                            .clickable {
                                onclickcomment()
                            }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .size(14.dp),
                    tint = Color.Gray
                )
                Divider(modifier = Modifier
                    .height(22.dp)
                    .width(1.dp))
                Icon(
                    painter = painterResource(id = R.drawable.likeicon),
                    contentDescription = "Like",
                    modifier = Modifier
                        .clickable {
                            onclicklike()
                        }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .size(14.dp),
                    tint = Color.Gray
                )

                Divider(modifier = Modifier
                    .height(22.dp)
                    .width(1.dp))

                Icon(
                    painter = painterResource(id = R.drawable.threedot),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            if (comment.userId == MyApplication.prefs
                                    .getString("userid")
                                    .toInt()
                            ) dropmenuexpanded = true
                            else dropmenuexpanded2 = true
                        }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .size(14.dp),
                    tint = Color.Gray
                )

                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = dropmenuexpanded,
                    onDismissRequest = {dropmenuexpanded = false},
                    //offset = DpOffset() // 펼쳐지는 메뉴의 위치 조정
                ) {
                    DropdownMenuItem(
                        text = {Text("수정")},
                        onClick = {

                        }
                    )
                    DropdownMenuItem(
                        text = {Text("삭제")},
                        onClick = onclickdelete
                    )
                }
                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = dropmenuexpanded2,
                    onDismissRequest = {dropmenuexpanded2 = false},
                ) {
                    DropdownMenuItem(
                        text = {Text("쪽지 보내기")},
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                val result = mainViewModel.makesession(comment.userId)
                                if(result==null) {} // TODO:
                                else{
                                    navController.navigate("SendScreen/${result}/${MyApplication.prefs.getString("userid").toInt()}"){
                                        popUpTo("Post/${comment.postId}")
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        Text(
            text = comment.content,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 5.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 5.dp)
        ){
            Text(
                text = timetoprint(comment.createdAt),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 5.dp)
            )
            ReactionNumberView("Like",comment.likes,"Small")
        }

        comment.childComments.forEach{
            Row(
                modifier = Modifier.padding(top = 7.dp, start = 10.dp)
            ){
                Icon(
                    imageVector = Icons.Sharp.SubdirectoryArrowRight,
                    contentDescription = "User",
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )

                ChildCommentView(
                    comment = it,
                    onclicklike = {
                        CoroutineScope(Dispatchers.Main).launch {
                            val result = mainViewModel.postcommentlike(it.childcommentid)
                            if(result==null){
                                Toast.makeText(context, "이미 공감한 댓글입니다.",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                if(MainViewModel._postchanged.value) MainViewModel._postchanged.emit(false)
                                else MainViewModel._postchanged.emit(true)
                            }
                        }
                    },
                    onclickdelete = {
                        CoroutineScope(Dispatchers.Main).launch {
                            val result = mainViewModel.deletecomment(it.childcommentid)
                            if(result==null){
                                // TODO:
                            }
                            else{
                                if(MainViewModel._postchanged.value) MainViewModel._postchanged.emit(false)
                                else MainViewModel._postchanged.emit(true)
                            }
                        }
                    },
                    navController = navController,
                    posteduserid = posteduserid
                )
            }
        }
    }
}

@Composable
fun ChildCommentView(onclicklike: () -> Unit = {},
                     onclickdelete: () -> Unit = {},
                     comment: ChildComment = ChildComment(childcommentid=3, userId=8, postId=1, content="comment3", parentCommentId=2, createdAt="2024-01-19T15:04:15.000+00:00", likes=0),
                     navController: NavHostController,
                     posteduserid: Int) {

    var dropmenuexpanded by remember { mutableStateOf(false) }
    var dropmenuexpanded2 by remember { mutableStateOf(false) }
    val mainViewModel = hiltViewModel<MainViewModel>()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = Icons.Sharp.AccountBox,
                    contentDescription = "User",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
                Text(
                    text = if(comment.userId == posteduserid) "익명(글쓴이)" else "익명",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 5.dp, top = 8.dp),
                    color = if(comment.userId == posteduserid) Color(0xFF05BCBC) else Color.Black
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 5.dp, end = 10.dp)
                    .background(color = Color(0xBEE6E6E6), shape = RoundedCornerShape(5.dp))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.likeicon),
                    contentDescription = "Like",
                    modifier = Modifier
                        .clickable {
                            onclicklike()
                        }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .size(14.dp),
                    tint = Color.Gray
                )
                Divider(modifier = Modifier
                    .height(22.dp)
                    .width(1.dp))

                Icon(
                    painter = painterResource(id = R.drawable.threedot),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            if (comment.userId == MyApplication.prefs
                                    .getString("userid")
                                    .toInt()
                            ) dropmenuexpanded = true
                            else dropmenuexpanded2 = true
                        }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .size(14.dp),
                    tint = Color.Gray
                )
                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = dropmenuexpanded,
                    onDismissRequest = {dropmenuexpanded = false},
                    //offset = DpOffset() // 펼쳐지는 메뉴의 위치 조정
                ) {
                    DropdownMenuItem(
                        text = {Text("수정")},
                        onClick = {

                        }
                    )
                    DropdownMenuItem(
                        text = {Text("삭제")},
                        onClick = onclickdelete
                    )
                }
                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = dropmenuexpanded2,
                    onDismissRequest = {dropmenuexpanded2 = false},
                ) {
                    DropdownMenuItem(
                        text = {Text("쪽지 보내기")},
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                val result = mainViewModel.makesession(comment.userId)
                                if(result==null) {} // TODO:
                                else{
                                    navController.navigate("SendScreen/${result}/${MyApplication.prefs.getString("userid").toInt()}"){
                                        popUpTo("Post/${comment.postId}")
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        Text(
            text = comment.content,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 5.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 5.dp)
        ) {
            Text(
                text = timetoprint(comment.createdAt),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 5.dp)
            )
            ReactionNumberView("Like", comment.likes, "Small")
        }
    }
}

@Composable
fun ReactionNumberView(
    type:String = "Like",
    number:Int = 0,
    size:String = "Small"
){
    val dpsize: Dp
    val padding: Dp
    val fontsize: TextUnit
    val icon: ImageVector
    val color: Color

    when(size){
        "Big" -> {
            dpsize=30.dp
            padding=0.dp
            fontsize=20.sp
        }
        "Normal" -> {
            dpsize=15.dp
            padding=0.dp
            fontsize=12.sp
        }
        else -> {
            dpsize=15.dp
            padding=4.dp
            fontsize=12.sp
        }
    }

    when(type){
        "Like" -> {
            icon = Icons.Default.Favorite
            color = Color.Red
        }
        "Comment" -> {
            icon = Icons.Default.Comment
            color = Color(0xFF00BCD4)
        }
        else -> {
            icon = Icons.Default.Star
            color = Color(0xFFFFEB3B)
        }
    }

    Icon(painter = painterResource(id = R.drawable.likeicon),
        contentDescription = "Like",
        Modifier
            .size(10.dp),
        tint=Color(0xFFF91F15))
    Text(
        number.toString(),
        modifier = Modifier.padding(start = 4.dp),
        fontSize = fontsize
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BottomTextField(
    postid:Int,
    parentcommentid:Int,
){
    val mainViewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var value by remember { mutableStateOf("") }

    BasicTextField(
        value = value,
        onValueChange = {value = it},
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .height(40.dp),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0x10000000), shape = RoundedCornerShape(size = 10.dp))
                    .padding(all = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(
                        imageVector = Icons.Default.CheckBox,
                        contentDescription = "",
                        tint = Color.Red,
                        modifier = Modifier.width(15.dp)
                    )

                    Text(
                        text = "익명",
                        fontSize = 10.sp,
                        color = Color.Red,
                    )

                    Spacer(modifier = Modifier.width(width = 8.dp))

                    innerTextField()
                }

                IconButton(onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    CoroutineScope(Dispatchers.Main).launch {
                        mainViewModel.postcomment(
                            PostComment(
                                userid = MyApplication.prefs.getString("userid").toInt(),
                                postid = postid,
                                content = value,
                                parentcommentid = parentcommentid,
                                likes = 0
                            )
                        )
                        value = ""
                        if(MainViewModel._postchanged.value) MainViewModel._postchanged.emit(false)
                        else MainViewModel._postchanged.emit(true)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Sharp.Send,
                        contentDescription = "",
                        tint = Color.Red,
                        modifier = Modifier.width(25.dp)
                    )
                }
            }

            if (value.isEmpty()) {
                Row(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .padding(start = 55.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "댓글을 입력하세요.",
                        fontSize = 10.sp,
                        color = Color.LightGray,
                    )
                }
            }
        }
    )
}

fun percentage(n1:Int, n2:Int, key:Int=1):Double{
    return when(key){
        1 -> {
            round(n1.toDouble()/(n1+n2)*1000)/10
        }
        2 -> {
            round(n2.toDouble()/(n1+n2)*1000)/10
        }
        else -> 0.0
    }
}