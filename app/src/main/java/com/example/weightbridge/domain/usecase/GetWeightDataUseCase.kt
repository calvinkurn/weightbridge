package com.example.weightbridge.domain.usecase

import com.google.firebase.database.DatabaseError
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.domain.repository.FirebaseRepository

class GetWeightDataUseCase {
    suspend fun getData(
        onFailed: (error: DatabaseError) -> Unit = {},
        onSuccess: (result: List<WeightDataModel>) -> Unit = {}
    ) {
        FirebaseRepository.fetchData().let {
            it.error?.let { error ->
                onFailed(error)
            } ?: run {
                onSuccess(it.data)
            }
        }
    }

    suspend fun writeData(
        data: WeightDataModel,
        onError: (error: DatabaseError) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        FirebaseRepository.writeData(data, onError, onSuccess)
    }
}