package com.example.todoapp.di.component

import android.content.Context
import com.example.todoapp.annotation.ApplicationContext
import com.example.todoapp.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @ApplicationContext
    fun context() : Context

    fun viewModelComponent() : ViewModelComponent

}