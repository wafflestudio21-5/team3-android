package com.example.everywaffle

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable

fun ChatScreen(navController: NavHostController) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    var isBoatSelected by remember { mutableStateOf(true) }
    val sessions = remember { mutableStateListOf<SessionDetail>() }

    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    var backpressed = false

    LaunchedEffect(Unit){
        sessions.clear()
        CoroutineScope(Dispatchers.Main).launch {
            val result = mainViewModel.getsessions()
            if(result!=null) sessions.addAll(result)
        }
    }

    BackHandler {
        if(backpressed) {
            activity?.finish()
        }
        else{
            Toast.makeText(context, "한 번 더 뒤로가기를 눌러 앱을 종료하세요.", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.Main).launch {
                backpressed=true
                delay(1000)
                backpressed=false
            }
        }
    }

    Surface(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 18.dp, start = 18.dp),
            ) {
                Text(
                    text = "랜덤쪽지  ",
                    color = if (isBoatSelected) Color.Black else Color(0xFFD3D3D3),
                    fontWeight = if (isBoatSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable {
                            isBoatSelected = true
                        }
                )
                Text(
                    text = "쪽지",
                    color = if (isBoatSelected) Color(0xFFD3D3D3) else Color.Black,
                    fontWeight = if (isBoatSelected) FontWeight.Normal else FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable {
                            isBoatSelected = false
                        }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.1f)
                    .background(Color.White)
            ){
                sessions.forEach{
                    item{
                        SessionPreview(it, navController)
                    }
                }
            }

            LowBar(navController = navController, key = 4) // TODO : 위치 조정 필요
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.background(Color.Transparent)
        ){
            if (isBoatSelected) {
                BottomAppBar( // TODO: 이 bottomappbar로 인해 Lowbar가 클릭이 되지 않는 현상 수정 필요
                    modifier = Modifier.padding(16.dp),
                    contentPadding = PaddingValues(0.dp),
                    containerColor = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.randommessage),
                            contentDescription = "랜덤쪽지 보내기",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(200.dp)
                                .clickable {
                                    navController.navigate(
                                        "SendScreen/0/${
                                            MyApplication.prefs
                                                .getString("userid")
                                                .toInt()
                                        }"
                                    ) {
                                        popUpTo("Chat")
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SessionPreview(
    session:SessionDetail,
    navController: NavHostController
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 18.dp)
            .clickable {
                navController.navigate("Message/${session.sessionId}") {
                    popUpTo("Chat")
                }
            }
    ){
        Spacer(modifier = Modifier.height(11.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = "익명", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = timetoprint(session.lastMessage.createdAt), fontSize = 12.sp, color = Color(0xFF979797))
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = session.lastMessage.content, fontSize = 13.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(13.dp))
    }
}

@Composable
fun MessageView(
    sessionid:Int?,
    navController: NavHostController
){
    val mainViewModel = hiltViewModel<MainViewModel>()
    val messages = remember { mutableStateListOf<MessageDetail>() }

    LaunchedEffect(Unit){
        messages.clear()
        CoroutineScope(Dispatchers.Main).launch {
            val result = mainViewModel.getmessages(sessionid!!)
            if(result!=null) messages.addAll(result)
        }
    }

    Surface(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.width(100.dp),
                    horizontalArrangement = Arrangement.Start
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.backarrow),
                        contentDescription = "",
                        modifier = Modifier
                            .width(11.dp)
                            .height(18.dp)
                            .clickable { navController.navigate("Chat") }
                    )
                }
                Text(text = "익명", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Row(
                    modifier = Modifier.width(100.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.messagesend),
                        contentDescription = "",
                        modifier = Modifier
                            .width(18.dp)
                            .height(18.dp)
                            .clickable {
                                navController.navigate(
                                    "SendScreen/${sessionid}/${
                                        MyApplication.prefs
                                            .getString("userid")
                                            .toInt()
                                    }"
                                ) {
                                    popUpTo("Message/${sessionid}")
                                }
                            }
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.threedots),
                        contentDescription = "",
                        modifier = Modifier
                            .width(3.dp)
                            .height(19.dp)
                            .clickable {}
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.1f)
                    .background(Color.White)
            ){
                messages.forEach{
                    item{
                        MessageContent(it)
                    }
                }
            }
        }
    }
}

@Composable
fun MessageContent(
    message:MessageDetail
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 18.dp)
    ){
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(fontSize = 13.sp, fontWeight = FontWeight.Bold,
                text = if(message.senderId == MyApplication.prefs.getString("userid").toInt()) "보낸 쪽지" else "받은 쪽지",
                color = if(message.senderId == MyApplication.prefs.getString("userid").toInt()) Color(0xFF05BCBC) else Color(0xFFFFD330))
            Text(text = timetoprint(message.createdAt), fontSize = 12.sp, color = Color(0xFF979797))
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = message.content, fontSize = 13.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(13.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun SendScreen(
    navController: NavController,
    sessionid:Int?,
    senderid:Int?
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "쪽지 보내기",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {navController.popBackStack()}) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "닫기",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val result = mainViewModel.sendmessage(SendMessage(sessionId = sessionid!!, senderId = senderid!!, content = message))
                                    if (result==null){
                                        Toast.makeText(context,"쪽지 전송에 실패했습니다. 잠시 후 다시 시도해 주세요.",Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        Toast.makeText(context,"쪽지 전송에 성공했습니다.",Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.send),
                                contentDescription = "전송",
                                modifier = Modifier
                                    .size(250.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                TextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    placeholder = { Text("내용을 입력하세요.", color = Color.Gray) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        cursorColor = Color.Black,
                        textColor = Color.Black
                    ),
                    singleLine = false,
                    maxLines = Int.MAX_VALUE,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction=ImeAction.Default)
                )
            }
        }
    }
}
