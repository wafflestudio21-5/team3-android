package com.example.everywaffle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun VoteDialog(
    ondismiss : () -> Unit,
    onagree : () -> Unit,
    ondisagree : () -> Unit
){
    var chosen by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = ondismiss) {
        Surface(
            modifier = Modifier
                .height(211.dp)
                .width(280.dp)
                .clip(shape = RoundedCornerShape(13.dp))
                .background(Color.White)
                .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(13.dp))
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ){
                Text(
                    text = "투표하기",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "찬성 또는 반대에 투표해주세요.",
                    fontSize = 13.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {chosen=1}
                ){
                    Icon(
                        painter = if(chosen==1) painterResource(id = R.drawable.vote_chosen) else painterResource(id = R.drawable.vote_notchosen),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.width(19.dp).height(19.dp)
                    )
                    Spacer(modifier = Modifier.width(11.dp))
                    Text(
                        text = "찬성",
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {chosen=2}
                ){
                    Icon(
                        painter = if(chosen==2) painterResource(id = R.drawable.vote_chosen) else painterResource(id = R.drawable.vote_notchosen),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.width(19.dp).height(19.dp)
                    )
                    Spacer(modifier = Modifier.width(11.dp))
                    Text(
                        text = "반대",
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "확인",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .height(34.dp)
                        .fillMaxWidth()
                        .background(color = if(chosen==0) Color(0xFFF2F2F2) else Color(0xFF6C59E4))
                        .clickable {
                            when(chosen){
                                1 -> {
                                    onagree()
                                    ondismiss()
                                }
                                2 -> {
                                    ondisagree()
                                    ondismiss()
                                }
                                else -> {}
                            }
                        }
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    color = if(chosen==0) Color(0xFFBBBBBB) else Color.White
                )
            }
        }
    }
}

@Composable
fun WithdrawDialog(
    ondismiss : () -> Unit = {},
    onagree : () -> Unit
){
    var chosen by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = ondismiss) {
        Surface(
            modifier = Modifier
                .height(145.dp)
                .width(280.dp)
                .clip(shape = RoundedCornerShape(13.dp))
                .background(Color.White)
                .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(13.dp))
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ){
                Text(
                    text = "회원 탈퇴",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "정말로 탈퇴하시겠습니까?",
                    fontSize = 13.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(15.dp))

                Row {
                    Text(
                        text = "네",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .height(34.dp)
                            .weight(1f)
                            .clip(shape = RoundedCornerShape(3.dp))
                            .background(
                                color = Color(0xFFF91F15)
                            )
                            .clickable {
                                onagree()
                            }
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        color = Color.White
                    )
                    Spacer(modifier=Modifier.width(20.dp))
                    Text(
                        text = "아니오",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .height(34.dp)
                            .weight(1f)
                            .clip(shape = RoundedCornerShape(3.dp))
                            .background(
                                color = Color(0xFFF2F2F2)
                            )
                            .clickable {
                                ondismiss()
                            }
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        color = Color(0xFFBBBBBB)
                    )
                }
            }
        }
    }
}