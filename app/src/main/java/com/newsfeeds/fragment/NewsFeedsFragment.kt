package com.newsfeeds.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.newsfeeds.R
import com.newsfeeds.adapter.NewsFeedsAdapter
import com.newsfeeds.databinding.FragmentNewsFeedsBinding
import com.newsfeeds.viewmodel.NewsFeedsViewModel
import kotlinx.android.synthetic.main.fragment_news_feeds.*
import org.koin.android.viewmodel.ext.android.viewModel

class NewsFeedsFragment : Fragment() {

    private val vm: NewsFeedsViewModel by viewModel() // The ViewModel instance is provide by Koin
    // using the viewModelModule from AppModule

    private lateinit var newsFeedsAdapter: NewsFeedsAdapter

    private lateinit var viewDataBinding: FragmentNewsFeedsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true // will maintain the Fragment instance when orientation changes
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<FragmentNewsFeedsBinding>(
            inflater, R.layout.fragment_news_feeds, container, false
        ).also { viewDataBinding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun updateUI() {
            viewDataBinding.frmProgress.visibility = View.GONE
            viewDataBinding.rclr.apply {
                adapter = newsFeedsAdapter
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            }
        }

        if (::newsFeedsAdapter.isInitialized) updateUI()
        else {
            vm.ldOnNewsFeedsListLoaded.observe(viewLifecycleOwner, Observer {
                newsFeedsAdapter = NewsFeedsAdapter(it)

                updateUI()
            })

            vm.ldOnError.observe(viewLifecycleOwner, Observer {
                viewDataBinding.frmProgress.visibility = View.GONE
                Snackbar.make(viewDataBinding.root, it, Snackbar.LENGTH_SHORT).show()
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.loadNewsFeeds()
    }
}