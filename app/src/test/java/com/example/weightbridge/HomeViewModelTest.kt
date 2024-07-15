package com.example.weightbridge

import com.example.weightbridge.ui.state.HomeAction
import com.example.weightbridge.ui.state.HomeState
import com.example.weightbridge.viewmodel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        viewModel = HomeViewModel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should update UI state to OpenDataInputState`() = runTest(coroutineTestRule.testDispatcher) {
        viewModel.setAction(HomeAction.ClickInputButton)

        val result = viewModel.state.first()
        Assert.assertEquals(result, HomeState.OpenDataInputState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should update UI state to OpenDataPreviewState`() = runTest(coroutineTestRule.testDispatcher) {
        viewModel.setAction(HomeAction.ClickPreviewButton)

        val result = viewModel.state.first()
        Assert.assertEquals(result, HomeState.OpenDataPreviewState)
    }
}