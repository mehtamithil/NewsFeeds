package com.newsfeeds.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.newsfeeds.R
import com.newsfeeds.databinding.FragmentNewsFeedsBinding

class NewsFeedsFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentNewsFeedsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ) =
        DataBindingUtil.inflate<FragmentNewsFeedsBinding>(
            inflater, R.layout.fragment_news_feeds, container, false
        ).also { viewDataBinding = it }.root

}