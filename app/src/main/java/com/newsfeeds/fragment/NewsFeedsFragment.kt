package com.newsfeeds.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.newsfeeds.R
import com.newsfeeds.adapter.NewsFeedsAdapter
import com.newsfeeds.databinding.FragmentNewsFeedsBinding
import com.newsfeeds.viewmodel.NewsFeedsViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentNewsFeedsBinding>(
        inflater, R.layout.fragment_news_feeds, container, false
    ).also { viewDataBinding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.apply {

            fun updateNewsFeeds() {
                swpRfrshNewsFeeds.isEnabled = false
                frmProgress.visibility = View.GONE
                rclr.apply {
                    adapter = newsFeedsAdapter
                    addItemDecoration(
                        ItemDecoration(resources.getDimensionPixelSize(R.dimen.rclr_decoration_ht_fragment_news_feeds))
                    )
                }
            }

            if (::newsFeedsAdapter.isInitialized) updateNewsFeeds() else {

                vm.ldOnNewsFeedsListLoaded.observe(viewLifecycleOwner, Observer {
                    newsFeedsAdapter = NewsFeedsAdapter(it)
                    updateNewsFeeds()
                })

                vm.ldOnError.observe(viewLifecycleOwner, Observer {
                    swpRfrshNewsFeeds.isEnabled = true
                    frmProgress.visibility = View.GONE
                    Snackbar.make(root, it, Snackbar.LENGTH_LONG).show()
                })
            }

            swpRfrshNewsFeeds.setOnRefreshListener {
                swpRfrshNewsFeeds.isRefreshing = false
                frmProgress.visibility = View.VISIBLE
                vm.loadNewsFeeds()
            }

            (requireActivity() as AppCompatActivity).setSupportActionBar(tlbr)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.loadNewsFeeds()
    }
}

private class ItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) top = margin
            left = margin
            right = margin
            bottom = margin
        }
    }
}