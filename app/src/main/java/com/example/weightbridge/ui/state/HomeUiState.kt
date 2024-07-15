package com.example.weightbridge.ui.state

open class HomeUiState

class HomeAction {
    object ClickInputButton : HomeUiState()
    object ClickPreviewButton: HomeUiState()
}
class HomeState {
    object OpenDataInputState : HomeUiState()
    object OpenDataPreviewState: HomeUiState()
}