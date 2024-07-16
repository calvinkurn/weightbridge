package com.example.weightbridge.di

import com.example.weightbridge.di.module.RepositoryModule
import com.example.weightbridge.di.module.ViewModelModule
import com.example.weightbridge.ui.view.InputActivity
import com.example.weightbridge.ui.view.PreviewActivity
import dagger.Component
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(application: WeightCheckerApplication)
    fun inject(activity: InputActivity)
}