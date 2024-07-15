package com.example.weightbridge.domain.mapper

import com.example.weightbridge.domain.model.WeightDataModel
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun DataSnapshot.toWeightModel(): List<WeightDataModel>? {
    val result = mutableListOf<WeightDataModel>()

    if (this.value == null) return null

    (this.value as Map<*,*>).forEach { (ticketID, value) ->
        val weightData = Gson().fromJson(Gson().toJson(value),WeightDataModel::class.java)
        weightData.ticketID = ticketID.toString()
        result.add(weightData)
    }
    return result
}

fun WeightDataModel.toFirebaseMap(): Map<String, Any> {
    val json = Gson().toJson(this)
    val mapType = object : TypeToken<Map<String, Any>>() {}.type
    val result: MutableMap<String, Any> = Gson().fromJson(json, mapType)
    result.remove("ticketID")

    return result
}

fun WeightDataModel.toCardData(): Map<String,Any> {
    return mapOf(
        "Date" to date,
        "Driver Name" to driverName,
        "License Number" to licenseNumber,
        "Inbound Weight" to inboundWeight,
        "Outbound Weight" to outboundWeight,
        "Net Weight" to netWeight
    )
}