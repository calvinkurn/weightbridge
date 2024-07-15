package com.example.weightbridge.domain.model

import com.google.firebase.database.DatabaseError

data class FetchResultDataModel(
    var data: List<WeightDataModel>,
    var error: DatabaseError?
)