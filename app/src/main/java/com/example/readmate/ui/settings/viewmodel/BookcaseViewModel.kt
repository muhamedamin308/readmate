package com.example.readmate.ui.settings.viewmodel

import android.util.Log
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.repo.remote.firebase.user.UserServicesRepository
import com.example.readmate.ui.base.BaseViewModel
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author Muhamed Amin Hassan on 02,November,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class BookcaseViewModel(
    private val userServicesRepository: UserServicesRepository
) : BaseViewModel() {
    private val _bookcaseBooks = MutableStateFlow<AppState<List<Book>>>(AppState.Ideal())
    val bookcaseBooks = _bookcaseBooks.asStateFlow()

    init {
        fetchBookcaseBooks()
    }

    fun fetchBookcaseBooks() {
        updateAppState(_bookcaseBooks, AppState.Loading())
        userServicesRepository.getBookcaseBooks { books, exception ->
            handleResult(_bookcaseBooks, books, exception)
        }
    }

    fun removeFromBookcase(bookId: String) {
        userServicesRepository.removeBookFromBookcase(bookId) {
            Log.i("BookcaseViewModel", "bookId: $bookId deleted!")
        }
    }
}