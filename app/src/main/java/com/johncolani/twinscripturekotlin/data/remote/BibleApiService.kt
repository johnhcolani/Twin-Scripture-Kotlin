package com.johncolani.twinscripturekotlin.data.remote
import com.johncolani.twinscripturekotlin.data.model.BookResponse
import com.johncolani.twinscripturekotlin.data.model.ChapterContentResponse
import com.johncolani.twinscripturekotlin.data.model.ChapterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BibleApiService {
    @GET("bibles/{bibleId}/books")
    suspend fun getBooks(@Path("bibleId") bibleId: String): Response<BookResponse>

    @GET("bibles/{bibleId}/books/{bookId}/chapters")
    suspend fun getChapters(
        @Path("bibleId") bibleId: String,
        @Path("bookId") bookId: String
    ): Response<ChapterResponse>

    @GET("bibles/{bibleId}/chapters/{chapterId}")
    suspend fun getChapterContent(
        @Path("bibleId") bibleId: String,
        @Path("chapterId") chapterId: String
    ): Response<ChapterContentResponse>
}