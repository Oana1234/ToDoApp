package com.example.todoapp.di.module

import android.app.Application
import android.content.Context
import com.example.todoapp.annotation.ApplicationContext
import com.example.todoapp.app.ToDoApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val toDoApplication: ToDoApplication) {

    @Provides
    internal fun provideApplication() : ToDoApplication = toDoApplication

    @Provides
    @ApplicationContext
    internal fun provideContext(app : ToDoApplication): Context = app.applicationContext


}