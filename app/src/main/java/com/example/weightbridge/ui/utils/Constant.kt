package com.example.weightbridge.ui.utils

object Constant {
    // FirebaseRepository
    const val EMPTY_DATA_ERROR_MESSAGE = "Data is Empty"

    // HomeView
    const val BUTTON_TEXT_INPUT = "Input New Data"
    const val BUTTON_TEXT_PREVIEW = "Show Detail Data"

    // InputActivity
    const val SUBMIT_SUCCESS_MESSAGE = "Submit Success"
    const val SUBMIT_FAILED_MESSAGE = "Submit Failed"
    
    // InputView
    const val CHOOSE_DATE_BUTTON_TEXT = "Choose Date"
    const val CHOOSE_TIME_BUTTON_TEXT = "Choose Time"

    const val DRIVER_TITLE_TEXT = "Driver Name"
    const val DRIVER_ERROR_TEXT = "Driver name cannot be empty"

    const val LICENSE_TITLE_TEXT = "License Number"
    const val LICENSE_ERROR_TEXT = "License number format not match"

    const val INBOUND_TITLE_TEXT = "Inbound Weight (on Ton)"
    const val INBOUND_ERROR_TEXT = "Weight should be number"

    const val OUTBOUND_TITLE_TEXT = "Outbound Weight (on Ton)"
    const val OUTBOUND_ERROR_TEXT = "Weight should be lower than Inbound & should be number"

    const val SUBMIT_VALIDATION_FAILED = "Submit Failed, please check your input"
    const val SUBMIT_EMPTY_FIELD = "Submit Failed, please fill all field"

    // PreviewActivity
    const val FETCH_DATA_FAILED_MESSAGE = "Fetch data failed"
    const val FETCH_DATA_SUCCESS_MESSAGE = "Fetch data success"
    const val FETCH_DATA_LOCAL = "Using Local Data, please wait for latest data"

    // PreviewView
    const val FETCH_DATA_UI_MESSAGE = "Fetch Data, Please Wait"
    const val FETCH_DATA_FAILED_UI_MESSAGE = "Fetch Failed, Please Check your connection & reopen this page"
}