package com.example.weightbridge.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weightbridge.ui.state.HomeUiState
import com.example.weightbridge.ui.theme.WeightBridgeTheme
import com.example.weightbridge.ui.view.HomeView
import com.example.weightbridge.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WeightBridgeTheme {
                HomeView(
                    onInputClicked = { inputClicked() },
                    onPreviewClicked = { previewClicked() }
                )
            }
        }

        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.actionState.collect {
                when(it) {
                    is HomeUiState.Companion.OpenDataInputState -> {
                        Toast.makeText(this@HomeActivity, "DataInput", Toast.LENGTH_LONG).show()
                    }
                    is HomeUiState.Companion.OpenDataPreviewState -> {
                        Toast.makeText(this@HomeActivity, "DataPreview", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun inputClicked() {
        viewModel.setAction(HomeUiState.Companion.OpenDataInputState)
    }

    private fun previewClicked() {
        viewModel.setAction(HomeUiState.Companion.OpenDataPreviewState)
    }
}