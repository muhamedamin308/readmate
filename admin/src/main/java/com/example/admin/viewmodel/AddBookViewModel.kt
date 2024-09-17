package com.example.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin.model.Book
import com.example.admin.service.FirebaseOperationService
import com.example.admin.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 14,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class AddBookViewModel(
    private val firebaseService: FirebaseOperationService
) : ViewModel() {
    private val _addBookState = MutableStateFlow<AppState<Book>>(AppState.Ideal())
    val addBookState = _addBookState.asStateFlow()

    fun addBook(book: Book) {
        viewModelScope.launch { _addBookState.emit(AppState.Loading()) }
        firebaseService.addBook(book) { newBook, error ->
            viewModelScope.launch {
                error?.let {
                    _addBookState.emit(AppState.Error(error.message.toString()))
                } ?: newBook?.let { _addBookState.emit(AppState.Success(newBook)) }
            }
        }
    }
}