package com.newsfeeds.repository.remote

import com.newsfeeds.api.ApiService
import com.newsfeeds.api.model.NewsFeedsList
import io.reactivex.Single

interface NewsFeedsRepository {
    fun getNewsFeeds(): Single<NewsFeedsList>
}

class NewsFeedsRepositoryImpl(private val apiService: ApiService) : NewsFeedsRepository {

    override fun getNewsFeeds() = apiService.getNewsFeeds()
}