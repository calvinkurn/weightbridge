package com.example.weightbridge.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weightbridge.di.WeightCheckerApplication
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.ui.state.InputAction
import com.example.weightbridge.ui.state.InputState
import com.example.weightbridge.ui.utils.Constant
import com.example.weightbridge.viewmodel.InputViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class InputActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: InputViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()

        getData()
        observeState()

        setContent {
            InputView(viewModel) {
                submitData(it)
            }
        }
    }

    private fun getData() {
        val intent = intent
        intent.getParcelableExtra<WeightDataModel>(INPUT_EXTRA)?.let {
            viewModel.setAction(
                InputAction.EditData(it)
            )
        }
    }

    private fun submitData(data: WeightDataModel) {
        viewModel.setAction(InputAction.SubmitData(data))
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when(it) {
                    is InputState.SubmitSuccess -> {
                        Toast.makeText(this@InputActivity, Constant.SUBMIT_SUCCESS_MESSAGE, Toast.LENGTH_LONG).show()
                        delay(1000)
                        finishActivity(RESULT_OK)
                    }
                    is InputState.SubmitFailed -> {
                        Toast.makeText(this@InputActivity, Constant.SUBMIT_FAILED_MESSAGE, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun inject() {
        (application as WeightCheckerApplication).appComponent.inject(this)
    }

    companion object {
        const val INPUT_EXTRA = "input_extra"
    }
}