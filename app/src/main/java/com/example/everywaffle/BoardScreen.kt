package com.example.everywaffle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Dashboard
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.ManageAccounts
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

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
