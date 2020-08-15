package com.newsfeeds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.newsfeeds.R
import com.newsfeeds.api.model.NewsFeedsList
import com.newsfeeds.databinding.RvRowNewsFeedBinding

class NewsFeedsAdapter(private val nfl: NewsFeedsList) :
    RecyclerView.Adapter<NewsFeedsAdapter.NewsFeedsVH>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int) = NewsFeedsVH(
        DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.rv_row_news_feed, viewGroup, false
        )
    )

    override fun onBindViewHolder(holder: NewsFeedsVH, pos: Int) {
        nfl.newsFeeds[pos].let { nf ->
            holder.binding.apply {
                descAbout = nfl.title
                newsFeed = nf
            }
        }
    }

    override fun getItemCount() = nfl.newsFeeds.size

    override fun onViewRecycled(holder: NewsFeedsVH) {
        super.onViewRecycled(holder)
        holder.binding.imgNews.setImageResource(R.drawable.ic_placeholder)
    }

    inner class NewsFeedsVH(val binding: RvRowNewsFeedBinding) :
        RecyclerView.ViewHolder(binding.root)

}