package com.example.everywaffle

import android.view.RoundedCorner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun Createpost(navController: NavHostController, category: String?) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }

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
                                text = "글 쓰기",
                                color = Color.Black
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack()}) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "닫기",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                              if (title.isNotBlank() && content.isNotBlank()) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        val result = mainViewModel.createpost(title, content, category!!)
                                        if (result != null) {
                                            navController.navigate("Board/${category}")
                                        }
                                    }
                                }
                            },
                            colors= ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .height(50.dp)
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "완료",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    ),
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp)) {
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    cursorBrush = SolidColor(Color.Black),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    decorationBox = { innerTextField ->
                        Column {
                            if (title.isEmpty()) {
                                Text("제목", style =
                                TextStyle(
                                    color = Color.Gray,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            innerTextField()
                            Spacer(modifier = Modifier.height(0.dp))
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    cursorBrush = SolidColor(Color.Black),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    decorationBox = { innerTextField ->
                        Column {
                            if (title.isEmpty()) {
                                Text("내용을 입력하세요. ", style =
                                TextStyle(
                                    color = Color.Gray,
                                    fontSize = 15.sp
                                )
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePost(navController: NavHostController, category:String?, postid:Int?, pretitle:String?, precontent:String?) {

    val mainViewModel = hiltViewModel<MainViewModel>()
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit){
        title = pretitle!!
        content = precontent!!
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("글 수정") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.Close, contentDescription = "닫기")
                    }
                },
                actions = {
                    Button(onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            CoroutineScope(Dispatchers.Main).launch {
                                val result = mainViewModel.updatepost(title, content, category!!, postid!!)
                                if (result!=null){
                                    navController.navigate("Board/${category}")
                                }
                            }

                            /*
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val newPostId = mainViewModel.createpost(title, content)
                                    newPostId?.let {
                                        val newPostDetail = mainViewModel.getpost(it)
                                        if (newPostDetail != null) {
                                            //mainViewModel.postChanged()
                                            CoroutineScope(Dispatchers.Main).launch {
                                                navController.navigate("Board/${newPostDetail.category}") {
                                                    popUpTo("BoardScreen") { inclusive = true }
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("CreateScreen", "Error creating post: $e")
                                }
                            }
                             */
                        }
                    }) {
                        Text("완료")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("내용을 입력하세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 500.dp),
                maxLines = 10,
                textStyle = TextStyle(fontSize = 16.sp)
            )
        }
    }
}