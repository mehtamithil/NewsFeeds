package com.newsfeeds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.newsfeeds.R
import com.newsfeeds.api.model.NewsFeedsList
import com.newsfeeds.databinding.RvRowNewsFeedBinding

class NewsFeedsAdapter : RecyclerView.Adapter<NewsFeedsAdapter.NewsFeedsVH>() {

    var nfl: NewsFeedsList? = null
        private set

    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int) = NewsFeedsVH(
        DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context),
            R.layout.rv_row_news_feed, viewGroup, false)
    )

    override fun onBindViewHolder(holder: NewsFeedsVH, pos: Int) {
        nfl?.let {
            it.newsFeeds[pos].let { nf ->
                holder.binding.apply {
                    descAbout = it.title
                    newsFeed = nf
                }
            }
        }
    }

    override fun getItemCount() = nfl?.newsFeeds?.size ?: 0

    override fun onViewRecycled(holder: NewsFeedsVH) {
        super.onViewRecycled(holder)
        holder.binding.imgNews.setImageResource(R.drawable.ic_placeholder)
    }

    fun clear() {
        nfl = null
        notifyDataSetChanged()
    }

    fun update(list: NewsFeedsList?) {
        nfl = list
        notifyDataSetChanged()
    }

    class NewsFeedsVH(val binding: RvRowNewsFeedBinding) : RecyclerView.ViewHolder(binding.root)

}