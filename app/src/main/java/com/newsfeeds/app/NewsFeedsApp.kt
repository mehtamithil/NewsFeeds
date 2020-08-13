package com.newsfeeds.app

import android.app.Application
import com.newsfeeds.BuildConfig
import com.newsfeeds.di.repositoryModule
import com.newsfeeds.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NewsFeedsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // starting Koin here in Application class
        startKoin {
            androidContext(this@NewsFeedsApp) // providing the app context to Koin
            if (BuildConfig.DEBUG) androidLogger()
            //now setting all the modules which are going to provide all the dependencies.
            modules(viewModelModule)
            modules(repositoryModule)
        }
    }
}