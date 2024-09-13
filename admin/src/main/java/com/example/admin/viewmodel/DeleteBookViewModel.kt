package com.example.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin.service.FirebaseOperationService
import com.example.admin.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 14,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class DeleteBookViewModel(
    private val firebaseService: FirebaseOperationService
) : ViewModel() {
    private val _deleteBook = MutableStateFlow<AppState<String>>(AppState.Ideal())
    val deleteBook = _deleteBook

    fun deleteBook(bookId: String) {
        viewModelScope.launch { _deleteBook.emit(AppState.Loading()) }
        firebaseService.deleteBook(bookId) { id, error ->
            viewModelScope.launch {
                error?.let {
                    _deleteBook.emit(AppState.Error(error.message.toString()))
                } ?: id?.let { _deleteBook.emit(AppState.Success(id)) }
            }
        }
    }
}