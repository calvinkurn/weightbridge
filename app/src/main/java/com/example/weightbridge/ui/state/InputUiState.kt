package com.example.weightbridge.ui.state

import com.example.weightbridge.domain.model.WeightDataModel

open class InputUiState

class InputAction {
    data class SubmitData(val data: WeightDataModel): InputUiState()
    data class EditData(val data: WeightDataModel) : InputUiState()
}

class InputState {
    object SubmitSuccess: InputUiState()
    object SubmitFailed: InputUiState()
    object InitialState: InputUiState()
}