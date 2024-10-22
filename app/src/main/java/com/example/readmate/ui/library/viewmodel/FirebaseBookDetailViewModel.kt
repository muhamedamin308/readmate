package com.example.readmate.ui.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.repo.remote.firebase.book.FirebaseBookRepository
import com.example.readmate.data.repo.remote.firebase.user.UserServicesRepository
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class FirebaseBookDetailViewModel(
    private val userServicesRepository: UserServicesRepository,
    private val bookServicesRepository: FirebaseBookRepository
) : ViewModel() {
    private val _isInBookcase = MutableStateFlow(false)
    val isInBookcase = _isInBookcase.asStateFlow()
    private val _isInMyBooks = MutableStateFlow(false)
    val isInMyBooks = _isInMyBooks.asStateFlow()
    private val _addBookToBookcase = MutableStateFlow<AppState<Book>>(AppState.Ideal())
    val addBookToBookcase = _addBookToBookcase.asStateFlow()
    private val _removeBookFromBookcase = MutableStateFlow<AppState<String>>(AppState.Ideal())
    val removeBookFromBookcase = _removeBookFromBookcase.asStateFlow()
    private val _similarBooks = MutableStateFlow<AppState<List<Book>>>(AppState.Ideal())
    val similarBooks = _similarBooks.asStateFlow()


    fun checkIfBookIsBookcase(bookId: String) {
        userServicesRepository.isInBookcase(bookId) { inBookcase ->
            viewModelScope.launch {
                _isInBookcase.emit(inBookcase)
            }
        }
    }

    fun checkIfBookIsMyBooks(bookId: String) {
        userServicesRepository.isInMyBooks(bookId) { inMyBooks ->
            viewModelScope.launch {
                _isInMyBooks.emit(inMyBooks)
            }
        }
    }

    fun addToBookcase(book: Book) {
        viewModelScope.launch { _addBookToBookcase.emit(AppState.Loading()) }
        userServicesRepository.addBookToBookcase(book) { newBook, exception ->
            exception?.let {
                viewModelScope.launch {
                    _addBookToBookcase.emit(
                        AppState.Error(
                            exception.message ?: "An error occurred"
                        )
                    )
                }
            } ?: viewModelScope.launch {
                _addBookToBookcase.emit(AppState.Success(newBook!!))
                _isInBookcase.emit(true)
            }
        }
    }

    fun removeFromBookcase(bookId: String) {
        viewModelScope.launch { _removeBookFromBookcase.emit(AppState.Loading()) }
        userServicesRepository.removeBookFromBookcase(bookId) { exception ->
            exception?.let {
                viewModelScope.launch {
                    _removeBookFromBookcase.emit(
                        AppState.Error(
                            exception.message ?: "An error occurred"
                        )
                    )
                }
            } ?: viewModelScope.launch {
                _removeBookFromBookcase.emit(AppState.Success("Book removed from bookcase."))
                _isInBookcase.emit(false)
            }
        }
    }

    fun fetchSimilarBooks(currentBookRating: Float) {
        viewModelScope.launch { _similarBooks.emit(AppState.Loading()) }
        bookServicesRepository.fetchSimilarBooks(currentBookRating) { books, exception ->
            exception?.let {
                viewModelScope.launch {
                    _similarBooks.emit(
                        AppState.Error(
                            exception.message ?: "An error occurred"
                        )
                    )
                }
            } ?: viewModelScope.launch {
                _similarBooks.emit(AppState.Success(books ?: emptyList()))
            }
        }
    }
}