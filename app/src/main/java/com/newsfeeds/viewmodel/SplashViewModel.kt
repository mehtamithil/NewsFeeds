package com.newsfeeds.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import java.util.concurrent.TimeUnit

class SplashViewModel(private val mainScheduler: Scheduler, private val ioScheduler: Scheduler) : ViewModel() {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private val mtldOnContinue by lazy { MutableLiveData<Boolean>() }
    val ldOnContinue: LiveData<Boolean>
        get() = mtldOnContinue

    private val DELAY_SPLASH by lazy { 3 }

    fun startTimer() {
        compositeDisposable.add(Completable.timer(DELAY_SPLASH.toLong(), TimeUnit.SECONDS)
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribeWith(object : DisposableCompletableObserver() {

                override fun onComplete() {
                    mtldOnContinue.value = true
                }

                override fun onError(e: Throwable) {
                    mtldOnContinue.value = false
                }
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}