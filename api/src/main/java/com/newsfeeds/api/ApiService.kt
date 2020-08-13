package com.newsfeeds.api

import com.newsfeeds.api.model.NewsFeedsList
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {

    @GET("2iodh4vg0eortkl/facts.json")
    fun getNewsFeeds(): Single<NewsFeedsList>

}