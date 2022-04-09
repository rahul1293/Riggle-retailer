package com.riggle.ui.other.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riggle.data.DataManager
import com.riggle.data.network.ApiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchActivityViewModel(var remoteDataSource: DataManager) : ViewModel() {
    private val _apiStateFlow = MutableStateFlow<ApiState>(ApiState.Empty)
    val apiStateFlow : StateFlow<ApiState> = _apiStateFlow


    fun getTopSearches() {
        /*viewModelScope.launch {
            remoteDataSource.getTopSearches().onStart {
                _apiStateFlow.value = ApiState.Loading
            }.catch { e->
                _apiStateFlow.value = ApiState.Failure(e)
            }.collect {
                _apiStateFlow.value = ApiState.Success(it.data,"search")
            }
        }*/

    }
}