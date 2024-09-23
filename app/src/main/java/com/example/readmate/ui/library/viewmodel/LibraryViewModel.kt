package com.example.readmate.ui.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.repo.remote.firebase.book.FirebaseBookRepository
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class LibraryViewModel(
    private val bookRepository: FirebaseBookRepository
) : ViewModel() {

    private val _newestBooks = MutableStateFlow<AppState<List<Book>>>(AppState.Ideal())
    val newestBooks = _newestBooks.asStateFlow()

    private val _recommendedBooks = MutableStateFlow<AppState<List<Book>>>(AppState.Ideal())
    val recommendedBooks = _recommendedBooks.asStateFlow()

    private val _bookCategories = MutableStateFlow<AppState<List<String>>>(AppState.Ideal())
    val bookCategories = _bookCategories.asStateFlow()

    private val _bestSellersBooks = MutableStateFlow<AppState<List<Book>>>(AppState.Ideal())
    val bestSellersBooks = _bestSellersBooks.asStateFlow()

    private val _topRatedBooks = MutableStateFlow<AppState<List<Book>>>(AppState.Ideal())
    val topRatedBooks = _topRatedBooks.asStateFlow()

    init {
        fetchData(_newestBooks) { onAction -> bookRepository.fetchNewestBooks(onAction) }
        fetchData(_recommendedBooks) { onAction -> bookRepository.fetchRecommendedBooks(onAction) }
        fetchData(_bookCategories) { onAction -> bookRepository.fetchBookCategories(onAction) }
        fetchData(_bestSellersBooks) { onAction -> bookRepository.fetchBestSellersBooks(onAction) }
        fetchData(_topRatedBooks) { onAction -> bookRepository.fetchTopRatedBooks(onAction) }
    }

    private fun <T> fetchData(
        stateFlow: MutableStateFlow<AppState<T>>,
        fetchMethod: (onAction: (T?, Exception?) -> Unit) -> Unit
    ) {
        viewModelScope.launch { stateFlow.emit(AppState.Loading()) }
        fetchMethod { result, error ->
            viewModelScope.launch {
                if (error == null) {
                    stateFlow.emit(AppState.Success(result!!))
                } else {
                    stateFlow.emit(AppState.Error(error.message.toString()))
                }
            }
        }
    }
}
