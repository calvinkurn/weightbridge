package com.example.weightbridge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.domain.repository.FirebaseRepository
import com.example.weightbridge.domain.repository.FirebaseRepositoryImpl
import com.example.weightbridge.domain.usecase.GetWeightDataUseCase
import com.example.weightbridge.ui.state.InputAction
import com.example.weightbridge.ui.state.InputState
import com.example.weightbridge.ui.state.InputUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class InputViewModel(
    private val firebaseRepositoryImpl: FirebaseRepository = FirebaseRepositoryImpl()
): ViewModel() {
    private var _state = MutableStateFlow<InputUiState>(InputState.InitialState)
    val state get() = _state

    private var _editData = MutableStateFlow<WeightDataModel?>(null)
    val editData get() = _editData

    fun setAction(action: InputUiState) {
        when (action) {
            is InputAction.SubmitData -> {
                submitData(action.data)
            }
            is InputAction.EditData -> {
                _editData.value = action.data
            }
        }
    }

    private fun submitData(data: WeightDataModel) {
        viewModelScope.launch {
            GetWeightDataUseCase(firebaseRepositoryImpl).writeData(
                data,
                onSuccess = {
                    _state.value = InputState.SubmitSuccess
                },
                onError = {
                    _state.value = InputState.SubmitFailed
                }
            )
        }
    }
}