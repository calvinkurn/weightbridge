package com.example.weightbridge.di

import android.app.Application
import com.example.weightbridge.di.module.RepositoryModule
import dagger.Component

@Component
interface ApplicationComponent {}

class WeightCheckerApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().repositoryModule(RepositoryModule(this)).build()
        appComponent.inject(this)
    }
}