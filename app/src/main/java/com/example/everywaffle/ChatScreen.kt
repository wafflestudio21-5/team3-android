package com.example.everywaffle

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
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
import kotlinx.coroutines.launch

@Composable

fun ChatScreen(navController: NavController) {
    var isBoatSelected by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text(
                text = "랜덤쪽지  ",
                color = if (isBoatSelected) Color.Black else Color.Gray,
                fontWeight = if (isBoatSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable {
                        isBoatSelected = true

                    }
            )
            Text(
                text = "쪽지",
                color = if (isBoatSelected) Color.Gray else Color.Black,
                fontWeight = if (isBoatSelected) FontWeight.Normal else FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable {
                        isBoatSelected = false

                    }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isBoatSelected) {
            BottomAppBar(
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
                                navController.navigate("SendScreen")
                            }
                    )
                }
            }
        }
        if(!isBoatSelected){
            BottomAppBar(
                modifier = Modifier.padding(16.dp),
                contentPadding = PaddingValues(0.dp),
                containerColor = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.normalmessage),
                        contentDescription = "쪽지 보내기",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                navController.navigate("SendScreen")
                            }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun SendScreen(navController: NavController) {
    var message by remember { mutableStateOf("") }

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
                        IconButton(onClick = {navController.navigate("ChatScreen")}) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "닫기",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {navController.navigate("ChatScreen")}
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
