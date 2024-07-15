package com.example.weightbridge.ui.state

import android.content.Context

open class PreviewUiState

class PreviewState {
    object InitialState: PreviewUiState()
    object LocalData: PreviewUiState()
    object FetchComplete: PreviewUiState()
    object FetchFailed: PreviewUiState()
}

class PreviewAction {
    data class FetchData(val context: Context?): PreviewUiState()
    data class SortData(val isDescending: Boolean, val targetField: Int): PreviewUiState()
    data class FilterData(val keyword: String, val targetField: Int): PreviewUiState()
}