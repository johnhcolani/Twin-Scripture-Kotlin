package com.johncolani.twinscripturekotlin.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johncolani.twinscripturekotlin.Constants
import com.johncolani.twinscripturekotlin.data.model.Book
import com.johncolani.twinscripturekotlin.data.model.Chapter

import com.johncolani.twinscripturekotlin.data.remote.BibleApiService
import com.johncolani.twinscripturekotlin.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Response

class BibleViewModel : ViewModel() {
    private val apiService: BibleApiService = RetrofitClient.retrofit.create(BibleApiService::class.java)

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    private val _chapters = MutableLiveData<List<Chapter>>()
    val chapters: LiveData<List<Chapter>> = _chapters

    private val _chapterContentPersian = MutableLiveData<String>()
    val chapterContentPersian: LiveData<String> = _chapterContentPersian

    private val _chapterContentEnglish = MutableLiveData<String>()
    val chapterContentEnglish: LiveData<String> = _chapterContentEnglish

    private val oldTestamentBookIds = listOf(
        "GEN", "EXO", "LEV", "NUM", "DEU", "JOS", "JDG", "RUT", "1SA", "2SA",
        "1KI", "2KI", "1CH", "2CH", "EZR", "NEH", "EST", "JOB", "PSA", "PRO",
        "ECC", "SNG", "ISA", "JER", "LAM", "EZK", "DAN", "HOS", "JOL", "AMO",
        "OBA", "JON", "MIC", "NAM", "HAB", "ZEP", "HAG", "ZEC", "MAL"
    )

    private val newTestamentBookIds = listOf(
        "MAT", "MRK", "LUK", "JHN", "ACT", "ROM", "1CO", "2CO", "GAL", "EPH",
        "PHP", "COL", "1TH", "2TH", "1TI", "2TI", "TIT", "PHM", "HEB", "JAS",
        "1PE", "2PE", "1JN", "2JN", "3JN", "JUD", "REV"
    )

    fun fetchBooks(testament: String) {
        viewModelScope.launch {
            try {
                val bibleId = if (testament == "old") Constants.PERSIAN_BIBLE_ID else Constants.ENGLISH_BIBLE_ID
                Log.d("BibleViewModel", "Fetching books for bibleId: $bibleId")
                val response: Response<com.johncolani.twinscripturekotlin.data.model.BookResponse> = apiService.getBooks(bibleId)
                if (response.isSuccessful) {
                    val bookResponse = response.body()
                    val allBooks = bookResponse?.data ?: emptyList()
                    // Filter books based on testament
                    val filteredBooks = if (testament == "old") {
                        allBooks.filter { it.id in oldTestamentBookIds }
                    } else {
                        allBooks.filter { it.id in newTestamentBookIds }
                    }
                    Log.d("BibleViewModel", "Books fetched successfully: ${filteredBooks.size} books")
                    _books.value = filteredBooks
                } else {
                    Log.e("BibleViewModel", "Failed to fetch books: ${response.code()} - ${response.message()}")
                    _books.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("BibleViewModel", "Error fetching books: ${e.message}", e)
                _books.value = emptyList()
            }
        }
    }

    fun fetchChapters(bookId: String) {
        viewModelScope.launch {
            try {
                val response: Response<com.johncolani.twinscripturekotlin.data.model.ChapterResponse> = apiService.getChapters(Constants.PERSIAN_BIBLE_ID, bookId)
                if (response.isSuccessful) {
                    _chapters.value = response.body()?.data ?: emptyList()
                } else {
                    Log.e("BibleViewModel", "Failed to fetch chapters: ${response.code()} - ${response.message()}")
                    _chapters.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("BibleViewModel", "Error fetching chapters: ${e.message}", e)
                _chapters.value = emptyList()
            }
        }
    }

    fun fetchChapterContent(chapterId: String, testament: String) {
        // Fetch Persian version
        viewModelScope.launch {
            try {
                Log.d("BibleViewModel", "Fetching Persian chapter content for bibleId: ${Constants.PERSIAN_BIBLE_ID}, chapterId: $chapterId")
                val response: Response<com.johncolani.twinscripturekotlin.data.model.ChapterContentResponse> = apiService.getChapterContent(Constants.PERSIAN_BIBLE_ID, chapterId)
                if (response.isSuccessful) {
                    val content = response.body()?.data?.content ?: ""
                    Log.d("BibleViewModel", "Persian chapter content fetched successfully: ${content.take(100)}...")
                    _chapterContentPersian.value = content
                } else {
                    Log.e("BibleViewModel", "Failed to fetch Persian chapter content: ${response.code()} - ${response.message()}")
                    _chapterContentPersian.value = ""
                }
            } catch (e: Exception) {
                Log.e("BibleViewModel", "Error fetching Persian chapter content: ${e.message}", e)
                _chapterContentPersian.value = ""
            }
        }

        // Fetch English version
        viewModelScope.launch {
            try {
                val bibleId = Constants.ENGLISH_BIBLE_ID
                Log.d("BibleViewModel", "Fetching English chapter content for bibleId: $bibleId, chapterId: $chapterId")
                val response: Response<com.johncolani.twinscripturekotlin.data.model.ChapterContentResponse> = apiService.getChapterContent(bibleId, chapterId)
                if (response.isSuccessful) {
                    val content = response.body()?.data?.content ?: ""
                    Log.d("BibleViewModel", "English chapter content fetched successfully: ${content.take(100)}...")
                    _chapterContentEnglish.value = content
                } else {
                    Log.e("BibleViewModel", "Failed to fetch English chapter content: ${response.code()} - ${response.message()}")
                    _chapterContentEnglish.value = ""
                }
            } catch (e: Exception) {
                Log.e("BibleViewModel", "Error fetching English chapter content: ${e.message}", e)
                _chapterContentEnglish.value = ""
            }
        }
    }
}