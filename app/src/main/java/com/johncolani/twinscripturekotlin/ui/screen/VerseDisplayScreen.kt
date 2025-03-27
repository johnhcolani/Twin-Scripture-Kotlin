
package com.johncolani.twinscripturekotlin.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.johncolani.twinscripturekotlin.utils.HtmlUtils
import com.johncolani.twinscripturekotlin.viewModel.BibleViewModel

@Composable
fun VerseDisplayScreen(chapterId: String, bookName: String, testament: String) {
    val viewModel: BibleViewModel = viewModel()
    val chapterContentPersian by viewModel.chapterContentPersian.observeAsState("")
    val chapterContentEnglish by viewModel.chapterContentEnglish.observeAsState("")

    LaunchedEffect(chapterId) {
        viewModel.fetchChapterContent(chapterId, testament)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Add the book name and chapter number as the title
        item {
            Text(
                text = "$bookName - ${chapterId.split(".").last()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Format and display the chapter content
        if (chapterContentPersian.isEmpty() && chapterContentEnglish.isEmpty()) {
            item {
                Text(
                    text = "Loading verses...",
                    fontSize = 16.sp
                )
            }
        } else {
            // Format Persian content
            val formattedPersian = if (chapterContentPersian.isNotEmpty()) {
                HtmlUtils.formatChapterContent(chapterContentPersian)
            } else {
                "Persian content not available"
            }
            val persianLines = formattedPersian.split("\n\n")

            // Format English content
            val formattedEnglish = if (chapterContentEnglish.isNotEmpty()) {
                HtmlUtils.formatChapterContent(chapterContentEnglish)
            } else {
                "English content not available"
            }
            val englishLines = formattedEnglish.split("\n\n")

            // Determine the maximum number of lines (verses) to iterate over
            val maxLines = maxOf(persianLines.size, englishLines.size)

            itemsIndexed((0 until maxLines).toList()) { index, _ ->
                // Get the Persian and English lines for the current index
                val persianLine = if (index < persianLines.size) persianLines[index] else ""
                val englishLine = if (index < englishLines.size) englishLines[index] else ""

                // Format English line
                val englishAnnotatedString = buildAnnotatedString {
                    if (index == 0) {
                        // Chapter title
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                            append(englishLine)
                        }
                    } else {
                        // Verse: Split into verse number and text
                        val parts = englishLine.split(" ", limit = 2)
                        if (parts.size == 2) {
                            val verseNumber = parts[0]
                            val verseText = parts[1]
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("$verseNumber ")
                            }
                            append(verseText)
                        } else {
                            append(englishLine)
                        }
                    }
                }

                // Format Persian line
                val persianAnnotatedString = buildAnnotatedString {
                    if (index == 0) {
                        // Chapter title
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                            append(persianLine)
                        }
                    } else {
                        // Verse: Split into verse number and text
                        val parts = persianLine.split(" ", limit = 2)
                        if (parts.size == 2) {
                            val verseNumber = parts[0]
                            val verseText = parts[1]
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("$verseNumber ")
                            }
                            append(verseText)
                        } else {
                            append(persianLine)
                        }
                    }
                }

                // Display English verse followed by Persian verse
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // English verse (LTR)
                    Text(
                        text = englishAnnotatedString,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Start // LTR
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Persian verse (RTL)
                    Text(
                        text = persianAnnotatedString,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.End // RTL
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}