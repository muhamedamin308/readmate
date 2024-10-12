package com.example.readmate.ui.explore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.responses.BookDetailsResponse
import com.example.readmate.data.model.responses.BookResponse
import com.example.readmate.data.repo.remote.api.ApiBookRepository
import com.example.readmate.data.service.remote.api.ApiResult
import com.example.readmate.util.AppState
import com.example.readmate.util.famousCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class ExploreViewModel(
    private val apiBookRepository: ApiBookRepository
) : ViewModel() {

    private val _apiRecentBookState = MutableStateFlow<ApiResult<BookResponse>>(ApiResult.Loading)
    val apiRecentBookState = _apiRecentBookState.asStateFlow()

    private val _categoriesState = MutableStateFlow<AppState<List<String>>>(AppState.Ideal())
    val categoriesState = _categoriesState.asStateFlow()

    private val _queriedBookState = MutableStateFlow<ApiResult<BookResponse>>(ApiResult.Loading)
    val queriedBookState = _queriedBookState.asStateFlow()

    private val _bookDetailsState =
        MutableStateFlow<AppState<BookDetailsResponse>>(AppState.Ideal())
    val bookDetailsState = _bookDetailsState.asStateFlow()

    init {
        fetchRecentBooks()
        fetchBookCategories()
    }

    private fun fetchRecentBooks() = viewModelScope.launch {
        updateApiState(_apiRecentBookState, apiBookRepository.getRecentBooks())
    }

    private fun fetchBookCategories() = viewModelScope.launch {
        _categoriesState.emit(AppState.Loading())
        _categoriesState.emit(AppState.Success(famousCategories))
    }

    fun getQueriedBooks(query: String) = viewModelScope.launch {
        updateApiState(_queriedBookState, apiBookRepository.searchBooks(query))
    }

    fun getBookDetails(bookId: String) = viewModelScope.launch {
        _bookDetailsState.emit(AppState.Loading())
        when (val result = apiBookRepository.getBookDetails(bookId)) {
            is ApiResult.Success -> _bookDetailsState.emit(AppState.Success(result.data))
            is ApiResult.Error -> _bookDetailsState.emit(AppState.Error(result.message))
            ApiResult.Loading -> Unit
        }
    }

    private suspend fun <T> updateApiState(
        stateFlow: MutableStateFlow<ApiResult<T>>,
        result: ApiResult<T>
    ) {
        stateFlow.emit(result)
    }
}
