package com.example.everywaffle

import android.util.Log
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable

fun PostScreen(postid:Int?, navController: NavHostController){
    val mainViewModel = hiltViewModel<MainViewModel>()
    var post by remember { mutableStateOf(PostDetail(-1,0,"","","","",0, 0,0)) }
    val comments = remember { mutableStateListOf<ParentComment>() }
    var dropmenuexpanded by remember { mutableStateOf(false) }
    var parentcommentid by remember { mutableStateOf(0) }
    val postchangedkey by MainViewModel.postchanged.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit, postchangedkey){
        Log.d("aaaa","abc")
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
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "Back")
                }

                Text(text = boardnames.filterValues { it == post.category }.keys.firstOrDefault(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                
                IconButton(onClick = {
                    if(post.userId == MyApplication.prefs.getString("userid").toInt()) dropmenuexpanded = true
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
                                            // TODO:
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
                                            // TODO:
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
                            // TODO:  
                        }
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
                            navController.navigate("Board/${post.category}")
                        }
                    }
                }
            )
        }
    }
}

@Composable
@Preview
fun PostView(
    post:PostDetail = PostDetail(postId=1, userId=35, title="waffle", content="waffle", category="FREE_BOARD", createdAt="2024-01-18T19:13:04.000+00:00", likes=1, 0,0)
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
            ReactionNumberView("Like",post.likes,"Small")
            Spacer(modifier = Modifier.width(5.dp))
            ReactionNumberView("Comment",post.comments,"Small")
            Spacer(modifier = Modifier.width(5.dp))
            ReactionNumberView("Scrap",post.scraps,"Small")
        }
    }
}

@Composable
fun ParentCommentView(
    comment: ParentComment = ParentComment(parentcommentid=2, userId=8, postId=1, content="comment2", createdAt="2024-01-19T15:03:58.000+00:00", childComments=listOf(ChildComment(childcommentid=3, userId=8, postId=1, content="comment3", parentCommentId=2, createdAt="2024-01-19T15:04:15.000+00:00", likes=0), ChildComment(childcommentid=4, userId=8, postId=1, content="comment4", parentCommentId=2, createdAt="2024-01-19T15:04:21.000+00:00", likes=0)), likes=0),
    onclickcomment: () -> Unit = {},
    onclicklike: () -> Unit = {}
){
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
                    imageVector = Icons.Sharp.AccountBox,
                    contentDescription = "User",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
                Text(
                    text = "익명",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 5.dp, top = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 5.dp, end = 10.dp)
                    .background(color = Color(0xBEE6E6E6), shape = RoundedCornerShape(5.dp))
            ) {
                Icon(
                    imageVector = Icons.Sharp.ChatBubbleOutline,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            onclickcomment()
                        }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .height(14.dp),
                    tint = Color.Gray
                )
                Divider(modifier = Modifier
                    .height(22.dp)
                    .width(1.dp))
                Icon(
                    imageVector = Icons.Sharp.FavoriteBorder,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            onclicklike()
                        }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .height(14.dp),
                    tint = Color.Gray
                )
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

                ChildCommentView(it)
            }
        }
    }
}

@Composable
fun ChildCommentView(comment: ChildComment = ChildComment(childcommentid=3, userId=8, postId=1, content="comment3", parentCommentId=2, createdAt="2024-01-19T15:04:15.000+00:00", likes=0)) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
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
                text = "익명",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp, top = 8.dp)
            )
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
            ReactionNumberView("Like",comment.likes,"Small")
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

    Icon(
        imageVector = icon,
        contentDescription = "",
        tint = color,
        modifier = Modifier
            .width(dpsize)
            .padding(start = padding)
    )
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