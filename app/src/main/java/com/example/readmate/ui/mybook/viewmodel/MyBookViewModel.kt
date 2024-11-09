package com.example.readmate.ui.mybook.viewmodel

import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.data.repo.remote.firebase.user.UserServicesRepository
import com.example.readmate.ui.base.BaseViewModel
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class MyBookViewModel(
    private val userServicesRepository: UserServicesRepository,
) : BaseViewModel() {
    private val _userBooks = MutableStateFlow<AppState<List<MyBook>>>(AppState.Ideal())
    val userBooks = _userBooks.asStateFlow()

    init {
        fetchUserBooks()
    }

    fun fetchUserBooks() {
        updateAppState(_userBooks, AppState.Loading())
        userServicesRepository.getMyBooks { books, error ->
            handleResult(_userBooks, books, error)
        }
    }

}
