package com.example.weightbridge.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weightbridge.domain.mapper.toCardData
import com.example.weightbridge.domain.model.FetchResultDataModel
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.domain.repository.FirebaseRepository
import com.example.weightbridge.domain.repository.PreferenceRepository
import com.example.weightbridge.ui.state.PreviewState
import com.example.weightbridge.ui.utils.Constant
import com.example.weightbridge.viewmodel.PreviewViewModel
import com.google.firebase.database.DatabaseError

@Composable
fun PreviewView(
    viewModel: PreviewViewModel,
    onCardEdit: (WeightDataModel) -> Unit = {}
) {
    val data by viewModel.data.collectAsState()
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is PreviewState.InitialState -> {
            Text(text = Constant.FETCH_DATA_UI_MESSAGE)
        }

        is PreviewState.FetchFailed -> {
            Text(text = Constant.FETCH_DATA_FAILED_UI_MESSAGE)
        }

        is PreviewState.FetchComplete, PreviewState.LocalData -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(data) {
                    val cardData = it.toCardData()
                    OutlinedCard(
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            cardData.forEach { dataForeach ->
                                val (fieldName, fieldValue) = dataForeach
                                Row {
                                    Text(text = fieldName, Modifier.weight(0.30f))
                                    Text(
                                        text = ":",
                                        Modifier.weight(0.05f),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(text = fieldValue.toString(), Modifier.weight(0.65f))
                                }
                            }
                        }
                        Button(
                            onClick = { onCardEdit(it) },
                            enabled = state != PreviewState.LocalData,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Edit")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun SamplePreviewView() {
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

    PreviewView(viewModel = PreviewViewModel(
        firebaseRepository = mockFirebaseRepo,
        preferenceRepository = mockPreferenceRepo
    ))
}