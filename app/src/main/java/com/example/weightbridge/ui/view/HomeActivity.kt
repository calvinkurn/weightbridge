package com.example.weightbridge.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weightbridge.ui.state.HomeAction
import com.example.weightbridge.ui.state.HomeState
import com.example.weightbridge.ui.theme.WeightBridgeTheme
import com.example.weightbridge.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
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
            viewModel.state.collectLatest {
                when(it) {
                    is HomeState.OpenDataInputState -> {
                        startInputActivity()
                    }
                    is HomeState.OpenDataPreviewState -> {
                        startPreviewActivity()
                    }
                }
            }
        }
    }

    private fun inputClicked() {
        viewModel.setAction(HomeAction.ClickInputButton)
    }

    private fun previewClicked() {
        viewModel.setAction(HomeAction.ClickPreviewButton)
    }

    private fun startPreviewActivity() {
        val intent = Intent(this, PreviewActivity::class.java)
        startActivity(intent)
    }

    private fun startInputActivity() {
        val intent = Intent(this, InputActivity::class.java)
        startActivity(intent)
    }
}