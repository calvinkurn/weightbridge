package com.example.weightbridge.ui.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.ui.components.InputTextFieldView
import com.example.weightbridge.ui.utils.Constant
import com.example.weightbridge.ui.utils.isDriverNameError
import com.example.weightbridge.ui.utils.isInBoundError
import com.example.weightbridge.ui.utils.isLicenseNumberError
import com.example.weightbridge.ui.utils.isOutBoundError
import com.example.weightbridge.viewmodel.InputViewModel
import java.time.Duration
import java.time.LocalDate
import java.util.Calendar

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun InputView(
    viewModel: InputViewModel = InputViewModel(),
    onSubmit: (data: WeightDataModel) -> Unit = {}
) {
    val date = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    var dateStringFormat by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(date) {
        dateStringFormat = millisToDate(date)
    }

    var showTimePicker by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    var timeStringFormat by remember {
        mutableStateOf("")
    }

    LaunchedEffect(timePickerState) {
        timeStringFormat = "${String.format("%02d", timePickerState.hour)}:${String.format("%02d", timePickerState.minute)}"
    }

    var valueInboundWeight by remember { mutableStateOf("") }
    var isInboundWeightError by remember { mutableStateOf(false) }

    var valueOutboundWeight by remember { mutableStateOf("") }
    var isOutboundWeightError by remember { mutableStateOf(false) }

    var valueDriver by remember { mutableStateOf("") }
    var isDriverError by remember { mutableStateOf(false) }

    var valueLicense by remember { mutableStateOf("") }
    var isLicenseError by remember { mutableStateOf(false) }

    var netWeight by remember { mutableStateOf(0) }
    LaunchedEffect(valueInboundWeight, valueOutboundWeight) {
        netWeight = try {
            (valueInboundWeight.toInt() - valueOutboundWeight.toInt()).let {
                if (it < 0) 0 else it
            }
        } catch (_: Exception) {
            0
        }
    }

    val context = LocalContext.current

    val editData by viewModel.editData.collectAsState()
    LaunchedEffect(editData) {
        editData?.let {
            it.date.split("||").let { dateTime ->
                dateStringFormat = dateTime.first()
                timeStringFormat = dateTime.last()
            }
            valueDriver = it.driverName
            valueLicense = it.licenseNumber
            valueInboundWeight = it.inboundWeight.toString()
            valueOutboundWeight = it.outboundWeight.toString()
        }
    }

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                DatePicker(state = date)
            }
        }
        if (showTimePicker) {
            ModalBottomSheet(onDismissRequest = { showTimePicker = false }) {
                TimePicker(state = timePickerState)
            }
        }

        // ==========
        Row {
            Button(onClick = { showBottomSheet = true }) {
                Text(text = Constant.CHOOSE_DATE_BUTTON_TEXT)
            }
            Text(
                text = dateStringFormat,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp, 0.dp)
            )
        }
        // ==========
        Row {
            Button(onClick = { showTimePicker = true }) {
                Text(text = Constant.CHOOSE_TIME_BUTTON_TEXT)
            }
            Text(
                text = timeStringFormat,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp, 0.dp)
            )
        }
        // ==========
        InputTextFieldView(
            title = Constant.DRIVER_TITLE_TEXT,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            value = valueDriver,
            onValueChange = { valueDriver = it },
            errorValidator = {
                isDriverNameError(valueDriver).also {
                    isDriverError = it
                }
            },
            errorMessage = Constant.DRIVER_ERROR_TEXT,
            modifier = Modifier.padding(top = 16.dp),
            isError = isDriverError
        )
        // ==========
        InputTextFieldView(
            title = Constant.LICENSE_TITLE_TEXT,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            value = valueLicense,
            onValueChange = { valueLicense = it },
            modifier = Modifier.padding(top = 16.dp),
            isError = isLicenseError,
            errorMessage = Constant.LICENSE_ERROR_TEXT,
            errorValidator = {
                isLicenseNumberError(valueLicense).also {
                    isLicenseError = it
                }
            }
        )
        // ==========
        InputTextFieldView(
            title = Constant.INBOUND_TITLE_TEXT,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            value = valueInboundWeight,
            onValueChange = { valueInboundWeight = it },
            modifier = Modifier.padding(top = 16.dp),
            isError = isInboundWeightError,
            errorMessage = Constant.INBOUND_ERROR_TEXT,
            errorValidator = {
                isInBoundError(valueInboundWeight).also {
                    isInboundWeightError = it
                }
            }
        )
        // ==========
        InputTextFieldView(
            title = Constant.OUTBOUND_TITLE_TEXT,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
                autoCorrect = false
            ),
            value = valueOutboundWeight,
            onValueChange = { valueOutboundWeight = it },
            modifier = Modifier.padding(top = 16.dp),
            isError = isOutboundWeightError,
            errorMessage = Constant.OUTBOUND_ERROR_TEXT,
            errorValidator = {
                isOutBoundError(netWeight, valueOutboundWeight).also {
                    isOutboundWeightError = it
                }
            }
        )
        // ==========
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Text(
                text = "Net Weight = $netWeight",
                modifier = Modifier.padding(0.dp, 8.dp),
                fontSize = TextUnit(24f, TextUnitType.Sp)
            )
        }
        // ==========
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            if (isLicenseError || isDriverError || isInboundWeightError || isOutboundWeightError) {
                Toast.makeText(context, Constant.SUBMIT_VALIDATION_FAILED, Toast.LENGTH_LONG)
                    .show()
            } else if (valueDriver.isEmpty() || valueLicense.isEmpty() || valueInboundWeight.isEmpty() || valueOutboundWeight.isEmpty()) {
                Toast.makeText(context, Constant.SUBMIT_EMPTY_FIELD, Toast.LENGTH_LONG)
                    .show()
            } else {
                val formatedDate = dateStringFormat
                val data = WeightDataModel(
                    date = "$formatedDate||$timeStringFormat",
                    driverName = valueDriver,
                    licenseNumber = valueLicense.uppercase(),
                    inboundWeight = valueInboundWeight.toInt(),
                    outboundWeight = valueOutboundWeight.toInt(),
                    netWeight = netWeight
                )
                editData?.let {
                    data.ticketID = it.ticketID
                }
                onSubmit(data)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Submit")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun millisToDate(dateState: DatePickerState): String {
    return (dateState.selectedDateMillis?.let {
        LocalDate.ofEpochDay(Duration.ofMillis(it).toDays())
    } ?: "-").toString()
}