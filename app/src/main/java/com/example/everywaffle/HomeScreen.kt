package com.example.everywaffle

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.Dashboard
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.ManageAccounts
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    onNavigateToBoard : () -> Unit = {},
    onNavigateToUser : () -> Unit = {},
    onNavigateToCreate: () -> Unit = {}

){
    val mainViewModel = hiltViewModel<MainViewModel>()
    val recentpost= remember{mutableStateMapOf<String,String>()}
    val trendpost= remember{mutableStateListOf<PostDetail>()}

    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.Main).launch {
            trendpost.clear()
            trendpost.addAll(mainViewModel.gettrending()!!)
        }
        boardnames.forEach{ kor,eng ->
            CoroutineScope(Dispatchers.Main).launch {
                val result = mainViewModel.getrecent(eng)
                if(result!=null) recentpost[kor] = result.title
            }
        }
    }

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
                        painter = painterResource(id = R.drawable.everywafflelogo),
                        contentDescription = "",
                        tint= Color.Unspecified,
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)
                    )
                }
                IconButton(onClick = { navController.navigate("Search/whole") }) {
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
                    BoardList(navController = navController, recent = recentpost, trend = trendpost)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButtonWithText(
                    imageVector = Icons.Sharp.Home,
                    text = "홈"
                )
                Spacer(Modifier.width(80.dp))
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

fun BoardList(navController: NavHostController, recent:Map<String,String>, trend:List<PostDetail>) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        when (name in boardnames.keys) {
                            true -> {
                                navController.navigate("Board/${boardnames[name]}")
                            }

                            false -> {}
                        }

                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                   text = recent.getOrDefault(name, ""),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Divider(modifier = Modifier.padding(top = 4.dp, bottom = 20.dp))
        Text(
            text = "실시간 인기 글",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        trend.forEach {
          PopularPost(navController, it)
        }
    }
}

@Composable
fun BoardScreen(
    boardid:String?,
    navController: NavHostController,
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val page by remember { mutableStateOf(1) }
    val loading = remember { mutableStateOf(false) }
    val itemList = remember { mutableStateListOf<PostDetail>() }
    val listState = rememberLazyListState()


    LaunchedEffect(Unit) {
        itemList.clear()
        itemList.addAll(mainViewModel.getpostcategory(boardid!!, 0)!!)
    }

    LaunchedEffect(page) {
        loading.value = true
        delay(1000)
        itemList.addAll(mainViewModel.getpostcategory(boardid!!, page)!!)
        loading.value = false
    }

    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "Back")
                }

                Text(text = boardnames.filterValues { it == boardid!! }.keys.firstOrDefault(), fontSize = 12.sp, fontWeight = FontWeight.Bold)

                Row() {
                    IconButton(onClick = { navController.navigate("Search/${boardid!!}") }) {
                        Icon(imageVector = Icons.Sharp.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Sharp.MoreVert, contentDescription = "")
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemList.forEach {
                    item { PostPreview(it, navController) }
                }
                item {
                    if (loading.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.width(50.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
            if (boardid in boardnames.values) CreatePost(navController = navController, category = boardid!!)
        }
    }
}

@Composable
fun PostPreview(
    post: PostDetail,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        shape = RectangleShape
    ) {

        Column(
            modifier = Modifier
                .background(Color.White)
                .clickable {
                    navController.navigate("Post/${post.postId}")
                }
                .padding(horizontal = 16.dp)
        ) {
            Text(post.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(post.content, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReactionNumberView("Like", post.likes, "Normal")
                Spacer(modifier = Modifier.width(8.dp))
                ReactionNumberView("Comment", post.comments, "Normal")
                Spacer(modifier = Modifier.width(8.dp))
                Text(timetoprint(post.createdAt), fontSize = 12.sp, color = Color.Gray)
            }
        }
        Divider()
    }
}

@Composable
fun CreatePost(navController: NavController, category: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate("CreatePost/$category") {
                        popUpTo("BoardScreen") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .height(50.dp)
                    .clip(RoundedCornerShape(50)),
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "글 쓰기",
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "글 쓰기",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}