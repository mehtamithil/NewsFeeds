package com.newsfeeds.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsfeeds.api.model.NewsFeedsList
import com.newsfeeds.repository.remote.NewsFeedsRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver

class NewsFeedsViewModel(private val repository: NewsFeedsRepository, private val mainScheduler: Scheduler,
    private val ioScheduler: Scheduler) : ViewModel() {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private val mtldOnNewsFeedsListLoaded by lazy { MutableLiveData<NewsFeedsList>() }
    val ldOnNewsFeedsListLoaded: LiveData<NewsFeedsList>
        get() = mtldOnNewsFeedsListLoaded

    private val mtldOnError by lazy { MutableLiveData<String>() }
    val ldOnError: LiveData<String>
        get() = mtldOnError

    fun loadNewsFeeds() {
        compositeDisposable.add(repository.getNewsFeeds()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
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
                        /**
                         * resetting 'mtldOnError' back to null after once this value is notified,
                         * otherwise the value would be stored in the LiveData and every-time user
                         * changes the orientation then LiveData will instantly listen this value
                         * as soon as we attach the Observer to it and SnackBar would be shown
                         * to the user representing the earlier error.
                         */
                        mtldOnError.value = null
                    }
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}