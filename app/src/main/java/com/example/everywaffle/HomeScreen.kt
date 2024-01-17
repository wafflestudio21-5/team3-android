package com.example.everywaffle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Dashboard
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.ManageAccounts
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
//@Preview
fun HomeScreen(
    navController: NavHostController,
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