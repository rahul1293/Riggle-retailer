package com.riggle.ui.profile.earnings

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riggle.data.models.response.CoinsEarning
import com.riggle.data.network.ApiState
import com.riggle.ui.profile.editprofile.ProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyEarningsViewModel(var repository: ProfileRepository) : ViewModel() {

    var page: Int = 1

    private val _apiStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val apiStateFlow: StateFlow<ApiState> = _apiStateFlow

    var earningsList = ObservableArrayList<CoinsEarning>()

    var availableCoins = ObservableField(0)

    var name: String? = null


    var dataList = mutableListOf<Int>(1, 2, 3, 4)
    var recommendList = mutableListOf<Int>()
    var normaList = mutableListOf<Int>()

    fun updateList() {

        recommendList = dataList.filter {
            it % 2 == 0
            //user.type==admin
        }.toMutableList()

        normaList = dataList.filter {
            it % 1 == 0
            //user.type==admin
        }.toMutableList()


    }


    fun getMyEarnings(id: Int) {

        viewModelScope.launch {
            repository.getUserEarnings(id).onStart {
                _apiStateFlow.value = ApiState.Loading
            }.catch { e ->
                _apiStateFlow.value = ApiState.Failure(e)
                e.printStackTrace()
            }.collect {
                it.earnings?.let { list ->
                    earningsList.addAll(list)
                }
                it.available_coins?.let {
                    availableCoins.set(it)
                }
                _apiStateFlow.value = ApiState.Success(it, "get_earnings")
            }

        }

    }

}