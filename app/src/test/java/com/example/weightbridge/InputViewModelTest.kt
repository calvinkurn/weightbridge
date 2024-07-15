package com.example.weightbridge

import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.domain.repository.FirebaseRepository
import com.example.weightbridge.ui.state.InputAction
import com.example.weightbridge.viewmodel.InputViewModel
import com.google.firebase.database.DatabaseError
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InputViewModelTest {
    private lateinit var viewModel: InputViewModel
    private val firebaseRepository: FirebaseRepository = mockk<FirebaseRepository>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        viewModel = InputViewModel(firebaseRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should set edit data`() = runTest(coroutineTestRule.testDispatcher) {
        val driverName = "Test Driver Name"
        val editData = WeightDataModel(
            driverName = driverName
        )

        viewModel.setAction(InputAction.EditData(editData))

        val result = viewModel.editData.first()
        Assert.assertEquals(result?.driverName, driverName)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should write input data to firebase`() = runTest(coroutineTestRule.testDispatcher) {
        val driverName = "Test Driver Name"
        val editData = WeightDataModel(
            driverName = driverName
        )

        val mockData = mockk<WeightDataModel>()
        val onErrorSlot = slot<(DatabaseError) -> Unit>()
        val onSuccessSlot = slot<() -> Unit>()

        every { firebaseRepository.writeData(data = mockData, onError = capture(onErrorSlot), onSuccess = capture(onSuccessSlot)) }

        viewModel.setAction(InputAction.SubmitData(editData))

        delay(500)

        // Check that the slots have captured the lambda expressions
        verify {firebaseRepository.writeData(any(), any(), any())}
    }
}