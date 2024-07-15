package com.example.weightbridge

import com.example.weightbridge.domain.model.FetchResultDataModel
import com.example.weightbridge.domain.model.WeightDataModel
import com.example.weightbridge.domain.repository.FirebaseRepository
import com.example.weightbridge.domain.repository.PreferenceRepository
import com.example.weightbridge.domain.repository.PreferenceRepositoryImpl
import com.example.weightbridge.ui.state.PreviewAction
import com.example.weightbridge.ui.view.FILTER_ITEM_DRIVER_NAME
import com.example.weightbridge.viewmodel.PreviewViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PreviewViewModelTest {
    private lateinit var viewModel: PreviewViewModel
    private val firebaseRepository: FirebaseRepository = mockk<FirebaseRepository>(relaxed = true)
    private val preferenceRepository: PreferenceRepository =
        mockk<PreferenceRepository>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        viewModel = PreviewViewModel(firebaseRepository, preferenceRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should fetch data from local and remote`() = runTest(coroutineTestRule.testDispatcher) {
        val driverName = "Test Fetch"
        val dummyRemoteData = WeightDataModel(
            driverName = driverName
        )

        every { preferenceRepository.getPreferences(any()) } returns STRING_JSON
        coEvery { firebaseRepository.fetchData() } returns FetchResultDataModel(
            listOf(
                dummyRemoteData,
                WeightDataModel()
            ), null
        )

        viewModel.setAction(PreviewAction.FetchData(null))

        viewModel.data.take(2).collectIndexed { index, value ->
            if (index == 0) {
                Assert.assertEquals(1, value.size)
            } else if (index == 1) {
                Assert.assertEquals(2, value.size)
                Assert.assertEquals(driverName, value.first().driverName)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should filter data base on driver name`() = runTest(coroutineTestRule.testDispatcher) {
        val driverName = "Test Fetch"
        val dummyRemoteData = WeightDataModel(
            driverName = driverName
        )

        every { preferenceRepository.getPreferences(any()) } returns STRING_JSON
        coEvery { firebaseRepository.fetchData() } returns FetchResultDataModel(
            listOf(
                dummyRemoteData,
                WeightDataModel()
            ), null
        )

        viewModel.setAction(PreviewAction.FetchData(null))

        viewModel.data.take(3).collectIndexed { index, value ->
            if (index == 1) {
                viewModel.setAction(PreviewAction.FilterData(driverName, FILTER_ITEM_DRIVER_NAME))
            }

            if (index == 2) {
                Assert.assertEquals(value.size, 1)
                Assert.assertEquals(value.first().driverName, driverName)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should sort data ascending base on driver name`() = runTest(coroutineTestRule.testDispatcher) {
        val driverName = listOf("Budi", "Ujang", "Anton")
        val dummyRemoteData = mutableListOf<WeightDataModel>()
        driverName.forEach {
            dummyRemoteData.add(
                WeightDataModel(
                    driverName = it
                )
            )
        }

        every { preferenceRepository.getPreferences(any()) } returns STRING_JSON
        coEvery { firebaseRepository.fetchData() } returns FetchResultDataModel(
            dummyRemoteData, null
        )

        viewModel.setAction(PreviewAction.FetchData(null))

        viewModel.data.take(3).collectIndexed { index, value ->
            if (index == 1) {
                viewModel.setAction(PreviewAction.SortData(isDescending = false, FILTER_ITEM_DRIVER_NAME))
            }

            if (index == 2) {
                Assert.assertEquals(value.size, dummyRemoteData.size)
                Assert.assertEquals(value.first().driverName, driverName.last())
            }
        }
    }

    private companion object {
        private const val STRING_JSON =
            "[{\"date\":\"2024-07-14||21:30\",\"driverName\":\"asd-edited\",\"inboundWeight\":33,\"licenseNumber\":\"A 123 sd\",\"netWeight\":21,\"outboundWeight\":12,\"ticketID\":\"1720967453250\"}]"
    }
}