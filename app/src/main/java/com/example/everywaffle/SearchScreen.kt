package com.example.everywaffle

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    boardid: String?
){
    var searchQuery by remember { mutableStateOf("") }
    var keyword by remember { mutableStateOf("") }

    Surface (modifier = Modifier.fillMaxSize()){
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {navController.popBackStack()}) {
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
                label = { Text("검색어를 입력하세요. ") },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick ={
                        keyword = searchQuery
                    }){
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {keyword = searchQuery}
                )
            )

            if (keyword!="") SearchBoardScreen(boardid = "Search_${boardid!!}", navController = navController, key = keyword)
        }
    }
}

@Composable
fun SearchBoardScreen(
    boardid:String?,
    navController: NavHostController,
    key:String = ""
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val page by remember { mutableStateOf(1) }
    val loading = remember { mutableStateOf(false) }
    val itemList = remember { mutableStateListOf<PostDetail>() }
    val listState = rememberLazyListState()
    var keyword by remember { mutableStateOf("") }

    keyword = key
    LaunchedEffect(Unit, keyword) {
        itemList.clear()
        itemList.addAll(mainViewModel.getpostcategory(boardid!!, 0, keyword = keyword)!!)
    }

    LaunchedEffect(page) {
        loading.value = true
        delay(1000)
        itemList.addAll(mainViewModel.getpostcategory(boardid!!, page, keyword = keyword)!!)
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
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemList.forEach {
                    item {
                        if ("whole" in boardid!!) SearchPostPreview(it, navController)
                        else PostPreview(it, navController)
                    }
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
fun SearchPostPreview(
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
            Text(boardnames.filterValues { it == post.category }.keys.first(), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Blue)
            Spacer(modifier = Modifier.height(4.dp))
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