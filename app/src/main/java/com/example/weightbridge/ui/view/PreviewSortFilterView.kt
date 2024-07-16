package com.example.weightbridge.ui.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.weightbridge.domain.model.FetchResultDataModel
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.domain.repository.FirebaseRepository
import com.example.weightbridge.domain.repository.PreferenceRepository
import com.example.weightbridge.ui.state.PreviewState
import com.example.weightbridge.viewmodel.PreviewViewModel
import com.google.firebase.database.DatabaseError

private val filterItems = listOf("Driver Name", "License Number", "Date")
const val FILTER_ITEM_DRIVER_NAME = 0
const val FILTER_ITEM_LICENSE_NUMBER = 1
const val FILTER_ITEM_DATE = 2

@Composable
fun PreviewSortFilterView(
    viewModel: PreviewViewModel,
    isDescending: Boolean = true,
    onSort: (isDescending: Boolean, targetField: Int) -> Unit = { _, _ -> },
    onSearch: (keyword: String, targetField: Int) -> Unit = { _, _ -> }
) {
    var selectedIndex by remember {
        mutableIntStateOf(viewModel.targetField.intValue)
    }
    var expanded by remember { mutableStateOf(false) }
    var keyWordValue by remember {
        mutableStateOf("")
    }
    var sortDescending by remember {
        mutableStateOf(isDescending)
    }

    val state = viewModel.uiState.collectAsState()
    val localFocusManager = LocalFocusManager.current

    if (state.value == PreviewState.FetchComplete) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Black)
                .padding(8.dp)
        ) {
            Row {
                Text(
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = "Target Field = ${filterItems[selectedIndex]}"
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Option"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    filterItems.forEachIndexed { index, s ->
                        DropdownMenuItem(
                            text = { Text(s) },
                            onClick = {
                                selectedIndex = index
                                expanded = !expanded
                                onSort(sortDescending, selectedIndex)
                                keyWordValue = ""
                                localFocusManager.clearFocus()
                            }
                        )
                    }
                }
            }
            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(0.9f),
                    value = keyWordValue,
                    onValueChange = { keyWordValue = it },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            localFocusManager.clearFocus()
                        }
                    )
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        onSearch(keyWordValue, selectedIndex)
                        localFocusManager.clearFocus()
                    }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        sortDescending = !sortDescending
                        onSort(sortDescending, selectedIndex)
                        localFocusManager.clearFocus()
                    }) {
                    Icon(
                        imageVector = if (sortDescending) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = "Sort"
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun SamplePreviewSortFilterView() {
    val mockPreferenceRepo = object: PreferenceRepository {
        override fun getPreferences(): String { return ""}
        override fun savePreferences(data: String) {}
    }
    val mockFirebaseRepo = object: FirebaseRepository {
        override suspend fun fetchData(): FetchResultDataModel {
            return FetchResultDataModel(listOf(), null)
        }

        override fun writeData(
            data: WeightDataModel,
            onError: (error: DatabaseError) -> Unit,
            onSuccess: () -> Unit
        ) {}
    }

    val viewModel = PreviewViewModel(
        firebaseRepository = mockFirebaseRepo,
        preferenceRepository = mockPreferenceRepo
    )

    viewModel.uiState.tryEmit(PreviewState.FetchComplete)

    PreviewSortFilterView(viewModel = viewModel)
}