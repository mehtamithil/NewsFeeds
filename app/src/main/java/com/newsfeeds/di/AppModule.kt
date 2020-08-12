package com.newsfeeds.di

import com.newsfeeds.viewmodel.SplashViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * This file is responsible for creating and providing all the dependencies required by the app.
 */

val repositoryModule = module {}

val viewModelModule = module {

    val mainScheduler by lazy { AndroidSchedulers.mainThread() }
    val ioScheduler by lazy { Schedulers.io() }

    viewModel { SplashViewModel(mainScheduler, ioScheduler) }

}