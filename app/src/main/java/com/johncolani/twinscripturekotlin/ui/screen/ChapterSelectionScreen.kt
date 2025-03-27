package com.johncolani.twinscripturekotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.johncolani.twinscripturekotlin.viewModel.BibleViewModel

@Composable
fun ChapterSelectionScreen(navController: NavController, bookId: String, bookName: String, testament: String) {
    val viewModel: BibleViewModel = viewModel()
    val chapters by viewModel.chapters.observeAsState(emptyList())

    LaunchedEffect(bookId) {
        viewModel.fetchChapters(bookId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = bookName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.fillMaxSize()
        ) {
            items(chapters) { chapter ->
                Button(
                    onClick = {
                        navController.navigate("verse_display/${chapter.id}/$bookName/$testament")
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .size(60.dp)
                ) {
                    Text(text = chapter.number)
                }
            }
        }
    }
}