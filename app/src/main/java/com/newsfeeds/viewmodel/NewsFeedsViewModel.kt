package com.newsfeeds.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsfeeds.api.model.NewsFeedsList
import com.newsfeeds.repository.remote.NewsFeedsRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver

class NewsFeedsViewModel(private val repository: NewsFeedsRepository,
                         private val observeOnScheduler: Scheduler,
                         private val subscribeOnScheduler: Scheduler,
                         private val compositeDisposable: CompositeDisposable) : ViewModel() {

    private val mtldOnNewsFeedsListLoaded by lazy {
        MutableLiveData<NewsFeedsList>()
    }

    val ldOnNewsFeedsListLoaded: LiveData<NewsFeedsList>
        get() = mtldOnNewsFeedsListLoaded

    private val mtldOnError by lazy {
        MutableLiveData<String>()
    }

    val ldOnError: LiveData<String>
        get() = mtldOnError

    fun loadNewsFeeds() {
        compositeDisposable.add(repository.getNewsFeeds()
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribeWith(object : DisposableSingleObserver<NewsFeedsList>() {

                    override fun onSuccess(nfl: NewsFeedsList) {
                        /**
                         * using Sequence, so that the filter operation doesn't start until the
                         * terminal function (toMutableList()) is called.
                         */
                        nfl.newsFeeds = nfl.newsFeeds.asSequence().filter {
                            /**
                             * filter removing the NewsFeed if everything is null in it
                             */
                            it.title != null || it.description != null && it.imageHref != null

                        }.toMutableList()

                        mtldOnNewsFeedsListLoaded.value = nfl
                    }

                    override fun onError(e: Throwable) {
                        mtldOnError.value = e.message
                    }
                }))
    }

    fun clearError() {
        mtldOnError.value = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}