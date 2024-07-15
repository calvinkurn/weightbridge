package com.example.weightbridge.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.domain.repository.FirebaseRepository
import com.example.weightbridge.domain.repository.FirebaseRepositoryImpl
import com.example.weightbridge.domain.repository.PreferenceRepository
import com.example.weightbridge.domain.repository.PreferenceRepositoryImpl
import com.example.weightbridge.domain.usecase.GetWeightDataUseCase
import com.example.weightbridge.ui.state.PreviewAction
import com.example.weightbridge.ui.state.PreviewState
import com.example.weightbridge.ui.state.PreviewUiState
import com.example.weightbridge.ui.utils.sortByField
import com.example.weightbridge.ui.view.FILTER_ITEM_DRIVER_NAME
import com.example.weightbridge.ui.view.FILTER_ITEM_LICENSE_NUMBER
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PreviewViewModel(
    private val firebaseRepository: FirebaseRepository = FirebaseRepositoryImpl(),
    private val preferenceRepository: PreferenceRepository = PreferenceRepositoryImpl()
) : ViewModel() {

    private var _uiState = MutableStateFlow<PreviewUiState>(PreviewState.InitialState)
    val uiState get() = _uiState

    private var _data = MutableStateFlow<List<WeightDataModel>>(listOf())
    val data get() = _data

    private var _isDescending = true
    private var _targetField = FILTER_ITEM_DRIVER_NAME
    private var _keyword = ""
    private var _tempData: List<WeightDataModel> = listOf()

    fun setAction(action: PreviewUiState) {
        when (action) {
            is PreviewAction.FetchData -> {
                getLocalData(action.context)
            }

            is PreviewAction.SortData -> {
                _isDescending = action.isDescending
                _targetField = action.targetField
                _data.tryEmit(sortData(_tempData))
            }

            is PreviewAction.FilterData -> {
                filterData(action.keyword, action.targetField)
            }
        }
    }

    private fun getLocalData(
        context: Context?
    ) {
        val localData = preferenceRepository.getPreferences(context)
        if (localData.isNotEmpty()) {
            val listOfLocalData: MutableList<WeightDataModel> =
                Gson().fromJson(localData, object : TypeToken<List<WeightDataModel>>() {}.type)

            _data.tryEmit(listOfLocalData)
            _uiState.tryEmit(PreviewState.LocalData)
        }
        getRemoteData(context)
    }

    private fun getRemoteData(context: Context?) {
        viewModelScope.launch(Dispatchers.IO) {
            // replicate network load
            delay(1500)

            // need to improve using dagger injection
            GetWeightDataUseCase(firebaseRepository).getData(onSuccess = { fetchData ->
                sortData(fetchData).also {
                    _tempData = it
                    _data.tryEmit(it)
                }
                _uiState.tryEmit(PreviewState.FetchComplete)

                preferenceRepository.savePreferences(context, Gson().toJson(fetchData))
            }, onFailed = {
                _uiState.tryEmit(PreviewState.FetchFailed)
            })
        }
    }

    private fun filterData(keyword: String, targetField: Int) {
        _keyword = keyword
        _targetField = targetField

        val filterResult = if (_data.value.size > 1 && _keyword.isNotEmpty()) {
            _tempData.filter {
                when (targetField) {
                    FILTER_ITEM_DRIVER_NAME -> filterValidator(it.driverName)
                    FILTER_ITEM_LICENSE_NUMBER -> filterValidator(it.licenseNumber)
                    else -> filterValidator(it.date)
                }
            }
        } else {
            _tempData
        }

        viewModelScope.launch {
            _data.emit(filterResult)
        }
    }

    // Filter can be adjust between exact keyword / contains here
    private fun filterValidator(data: String): Boolean {
        return data == _keyword
    }

    private fun sortData(data: List<WeightDataModel>): List<WeightDataModel> {
        return if (data.size <= 1) {
            data
        } else {
            data.sortByField({
                when (_targetField) {
                    FILTER_ITEM_DRIVER_NAME -> it.driverName
                    FILTER_ITEM_LICENSE_NUMBER -> it.licenseNumber
                    else -> it.date
                }
            }, _isDescending)
        }
    }
}