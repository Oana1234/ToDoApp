package com.example.todoapp.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.example.todoapp.di.component.AppComponent
import com.example.todoapp.di.component.DaggerAppComponent
import com.example.todoapp.di.module.AppModule
import com.example.todoapp.utils.AppLogger

class ToDoApplication : MultiDexApplication() {

    companion object {
        lateinit var instance: ToDoApplication

        fun get(context: Context): ToDoApplication {
            return context.applicationContext as ToDoApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        AppLogger.init()

    }

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this)).build()
    }
}