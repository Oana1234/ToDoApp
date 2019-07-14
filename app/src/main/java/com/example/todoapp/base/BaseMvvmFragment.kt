package com.example.todoapp.base

import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable

abstract class BaseMvvmFragment  < VM : BaseViewModel> : BaseFragment(){

    protected val compositeDisposable = CompositeDisposable()

    protected abstract fun getViewModelClass(): Class<VM>

    protected val viewModel: VM by lazy { ViewModelProviders.of(this).get(getViewModelClass()) }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}