package com.example.todoapp.base

import androidx.lifecycle.ViewModel
import com.example.todoapp.app.ToDoApplication
import com.example.todoapp.di.component.ViewModelComponent
import com.example.todoapp.extension.applySchedulers
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel: ViewModel() {

    protected val viewModelComponent: ViewModelComponent by lazy { ToDoApplication.instance.component.viewModelComponent() }
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        inject()
    }

    protected abstract fun inject()

    protected fun <T> subscribeService(
        serviceObservable: Observable<T>,
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) = compositeDisposable.add(
        serviceObservable.applySchedulers().subscribe(onNext, onError)
    )

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}