package com.example.todoapp.di.module

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule(private val viewModule: ViewModel) {

    @Provides
    internal fun providesViewModel() : ViewModel = viewModule

}