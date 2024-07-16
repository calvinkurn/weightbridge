package com.example.weightbridge.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weightbridge.di.WeightCheckerApplication
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.ui.state.PreviewAction
import com.example.weightbridge.ui.state.PreviewState
import com.example.weightbridge.ui.utils.Constant
import com.example.weightbridge.viewmodel.PreviewViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreviewActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PreviewViewModel by viewModels { viewModelFactory }

    private val editPageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.setAction(PreviewAction.FetchRemoteData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()

        setContent {
            Column {
                PreviewSortFilterView(
                    viewModel,
                    onSort = { keyword, targetField -> sortTicketList(keyword, targetField) },
                    onSearch = { keyword, targetField ->
                        filterTicketList(keyword, targetField)
                    }
                )
                PreviewView(viewModel, onCardEdit = { editTicket(it) })
            }
        }

        observeState()
        viewModel.setAction(PreviewAction.FetchData)
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    is PreviewState.FetchFailed -> {
                        Toast.makeText(
                            this@PreviewActivity,
                            Constant.FETCH_DATA_FAILED_MESSAGE,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                    is PreviewState.FetchComplete -> {
                        Toast.makeText(
                            this@PreviewActivity,
                            Constant.FETCH_DATA_SUCCESS_MESSAGE,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is PreviewState.LocalData -> {
                        Toast.makeText(
                            this@PreviewActivity,
                            Constant.FETCH_DATA_LOCAL,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun editTicket(data: WeightDataModel) {
        val intent = Intent(this, InputActivity::class.java)
        intent.putExtra(InputActivity.INPUT_EXTRA, data)
        editPageLauncher.launch(intent)
    }

    private fun sortTicketList(isDescending: Boolean, targetField: Int) {
        viewModel.setAction(PreviewAction.SortData(isDescending, targetField))
    }

    private fun filterTicketList(keyword: String, targetField: Int) {
        viewModel.setAction(PreviewAction.FilterData(keyword, targetField))
    }

    private fun inject() {
        (application as WeightCheckerApplication).appComponent.inject(this)
    }
}