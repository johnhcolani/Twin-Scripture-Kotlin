package com.johncolani.twinscripturekotlin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.johncolani.twinscripturekotlin.ui.screen.SplashScreen
import com.johncolani.twinscripturekotlin.ui.screens.BookSelectionScreen
import com.johncolani.twinscripturekotlin.ui.screens.ChapterSelectionScreen
import com.johncolani.twinscripturekotlin.ui.screens.MainScreen
import com.johncolani.twinscripturekotlin.ui.screens.VerseDisplayScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
        composable("book_selection/{testament}") { backStackEntry ->
            val testament = backStackEntry.arguments?.getString("testament") ?: "old"
            BookSelectionScreen(navController, testament)
        }
        composable("chapter_selection/{bookId}/{bookName}/{testament}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            val bookName = backStackEntry.arguments?.getString("bookName") ?: ""
            val testament = backStackEntry.arguments?.getString("testament") ?: "old"
            ChapterSelectionScreen(navController, bookId, bookName, testament)
        }
        composable("verse_display/{chapterId}/{bookName}/{testament}") { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: ""
            val bookName = backStackEntry.arguments?.getString("bookName") ?: ""
            val testament = backStackEntry.arguments?.getString("testament") ?: "old"
            VerseDisplayScreen(chapterId, bookName, testament)
        }
    }
}