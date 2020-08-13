package com.newsfeeds.api.model

import com.google.gson.annotations.SerializedName

data class NewsFeedsList(val title: String, @SerializedName("rows") var newsFeeds: MutableList<NewsFeed>)