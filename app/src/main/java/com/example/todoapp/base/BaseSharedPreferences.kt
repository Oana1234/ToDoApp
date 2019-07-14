package com.example.todoapp.base

import android.content.Context
import android.content.SharedPreferences
import com.example.todoapp.app.ToDoApplication

abstract class BaseSharedPreferences {


    protected abstract val preferencesName: String

    protected fun getStringEntry(key: String, defaultValue: String = ""): String {
        return getSharedPreferences().getString(key, defaultValue)
    }

    protected fun setStringEntry(key: String, value: String) {
        val prefsEditor = getPreferencesEditor()
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    protected fun setBooleanEntry(key: String, value: Boolean) {
        getPreferencesEditor().putBoolean(key, value).commit()
    }

    protected fun getBooleanEntry(key: String, defaultValue: Boolean = false): Boolean {
        return getSharedPreferences().getBoolean(key, defaultValue)
    }

    protected fun deleteEntry(key: String) {
        getPreferencesEditor().remove(key).commit()
    }

    protected fun getPreferencesEditor(): SharedPreferences.Editor {

        val prefs = ToDoApplication.instance.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return prefs.edit()
    }

    protected fun getSharedPreferences(): SharedPreferences {
        return ToDoApplication.instance.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

}