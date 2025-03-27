package com.johncolani.twinscripturekotlin.utils

import android.text.Html
import android.text.Spanned

object HtmlUtils {
    fun stripHtml(html: String): String {
        // Use Android's Html.fromHtml to convert HTML to plain text
        val spanned: Spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
        return spanned.toString().trim()
    }

    // Function to parse the chapter content and format it as "verseNumber verseText"
    fun formatChapterContent(htmlContent: String): String {
        // Split the content by <p> tags
        val paragraphs = htmlContent.split("<p class=\"p\">", "<p class=\"b\">").filter { it.isNotBlank() }
        val formattedVerses = mutableListOf<String>()

        // Extract the chapter title (from <p class="s1">)
        val chapterTitleMatch = Regex("<p class=\"s1\">(.*?)</p>").find(htmlContent)
        val chapterTitle = chapterTitleMatch?.groupValues?.get(1)?.trim() ?: ""

        // Add the chapter title as the first line
        if (chapterTitle.isNotEmpty()) {
            formattedVerses.add(chapterTitle)
        }

        // Process each paragraph to extract verse number and text
        paragraphs.forEach { paragraph ->
            // Skip the chapter title paragraph if it exists
            if (paragraph.contains("<p class=\"s1\">")) return@forEach

            // Extract verse number using regex
            val verseNumberMatch = Regex("<span data-number=\"(\\d+)\"[^>]*>(.*?)</span>").find(paragraph)
            val verseNumber = verseNumberMatch?.groupValues?.get(1) ?: ""
            val verseText = verseNumberMatch?.let {
                // Remove the span tag and everything before it, then strip remaining HTML
                val textAfterSpan = paragraph.substringAfter("</span>")
                stripHtml(textAfterSpan).trim()
            } ?: stripHtml(paragraph).trim()

            if (verseNumber.isNotEmpty() && verseText.isNotEmpty()) {
                formattedVerses.add("$verseNumber $verseText")
            }
        }

        return formattedVerses.joinToString("\n\n")
    }
}