package com.johncolani.twinscripturekotlin.data.model

data class BookResponse(val data: List<Book>)
data class Book(val id: String, val name: String)

data class ChapterResponse(val data: List<Chapter>)
data class Chapter(val id: String, val number: String)

data class ChapterContentResponse(val data: ChapterContent)
data class ChapterContent(val id: String, val content: String)
