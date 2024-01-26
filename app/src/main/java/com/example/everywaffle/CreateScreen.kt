package com.example.everywaffle

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    val recentpost = remember { mutableStateMapOf<String, String>() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("글 쓰기") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.Close, contentDescription = "닫기")
                    }
                },
                actions = {
                    Button(onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val newPostId = viewModel.createpost(title, content)
                                    newPostId?.let {
                                        val newPostDetail = viewModel.getpost(it)
                                        if (newPostDetail != null) {
                                            viewModel.postChanged()
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
