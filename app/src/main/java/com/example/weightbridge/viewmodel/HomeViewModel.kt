package com.example.weightbridge.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weightbridge.ui.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel: ViewModel() {
    private var _actionState = MutableStateFlow(HomeUiState())
    val actionState = _actionState

    fun setAction(action: HomeUiState) {
        _actionState.value = action
    }
}