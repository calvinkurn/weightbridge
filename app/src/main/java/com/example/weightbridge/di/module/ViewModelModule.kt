package com.example.weightbridge.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weightbridge.di.ViewModelFactory
import com.example.weightbridge.di.ViewModelKey
import com.example.weightbridge.viewmodel.InputViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InputViewModel::class)
    abstract fun bindInputViewModel(viewModel: InputViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(PreviewViewModel::class)
//    abstract fun bindPreviewViewModel(viewModel: PreviewViewModel): ViewModel
}