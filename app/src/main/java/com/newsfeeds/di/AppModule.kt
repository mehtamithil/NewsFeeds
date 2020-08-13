package com.newsfeeds.di

import com.newsfeeds.api.ApiClient
import com.newsfeeds.repository.remote.NewsFeedsRepository
import com.newsfeeds.repository.remote.NewsFeedsRepositoryImpl
import com.newsfeeds.viewmodel.NewsFeedsViewModel
import com.newsfeeds.viewmodel.SplashViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * This file is responsible for creating and providing all the dependencies required by the app.
 */

val repositoryModule = module {

    /**
     * This single ApiService instance would be shared to all API Repositories listed below
     */
    val apiService by lazy { ApiClient().apiService }

    single<NewsFeedsRepository> { NewsFeedsRepositoryImpl(apiService) }
}

val viewModelModule = module {

    /**
     * These same instances of the mainScheduler and ioScheduler would be shared to all ViewModels
     * listed below. Also, while creating the instance of 'NewsFeedsViewModel' note that we have
     * passed the instance of NewsFeedsRepository using 'get()' as its now Koin's responsibility to
     * get the NewsFeedsRepository's instance from repositoryModule and pass it here.
     */
    val mainScheduler by lazy { AndroidSchedulers.mainThread() }
    val ioScheduler by lazy { Schedulers.io() }

    viewModel { SplashViewModel(mainScheduler, ioScheduler) }
    viewModel { NewsFeedsViewModel(get(), mainScheduler, ioScheduler) }

}