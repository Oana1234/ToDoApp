package com.example.todoapp.di.component

import com.example.todoapp.annotation.PerViewModel
import com.example.todoapp.di.module.ViewModelModule
import dagger.Subcomponent

@PerViewModel
@Subcomponent(modules = [(ViewModelModule::class)])
interface ViewModelComponent {

}