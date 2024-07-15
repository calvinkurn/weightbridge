package com.example.weightbridge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightbridge.ui.state.HomeAction
import com.example.weightbridge.ui.state.HomeState
import com.example.weightbridge.ui.state.HomeUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private var _state = MutableSharedFlow<HomeUiState>()
    val state get() =  _state

    fun setAction(action: HomeUiState) {
        when(action){
            is HomeAction.ClickInputButton -> {
                updateState(HomeState.OpenDataInputState)
            }
            is HomeAction.ClickPreviewButton -> {
                updateState(HomeState.OpenDataPreviewState)
            }
        }
    }

    private fun updateState(stateParam: HomeUiState) {
        viewModelScope.launch {
            _state.emit(stateParam)
        }
    }
}