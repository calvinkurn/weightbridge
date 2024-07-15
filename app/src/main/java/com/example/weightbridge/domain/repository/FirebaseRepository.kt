package com.example.weightbridge.domain.repository

import com.example.weightbridge.domain.mapper.toFirebaseMap
import com.example.weightbridge.domain.mapper.toWeightModel
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.domain.model.FetchResultDataModel
import com.example.weightbridge.ui.utils.Constant
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object FirebaseRepository {
    private const val DATABASE_PATH =
        "https://weightbridge-bca74-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val database = Firebase.database(DATABASE_PATH)
    private val databaseReference = database.getReference()

    suspend fun fetchData(): FetchResultDataModel = suspendCancellableCoroutine { coroutine ->
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                coroutine.resume(FetchResultDataModel(listOf(), error))
                databaseReference.removeEventListener(this)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.toWeightModel().also {
                    if (it == null) {
                        coroutine.resume(
                            FetchResultDataModel(
                                listOf(),
                                DatabaseError.fromException(object : Exception(
                                    Constant.EMPTY_DATA_ERROR_MESSAGE
                                ) {})
                            )
                        )
                    } else {
                        coroutine.resume(FetchResultDataModel(it, null))
                    }
                }
                databaseReference.removeEventListener(this)
            }
        })
    }

    suspend fun writeData(
        data: WeightDataModel,
        onError: (error: DatabaseError) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        val firebaseDataMap = data.toFirebaseMap()
        databaseReference.child(data.ticketID).setValue(
            firebaseDataMap
        ) { error, _ ->
            error?.let {
                onError(it)
            } ?: run {
                onSuccess()
            }
        }
    }
}