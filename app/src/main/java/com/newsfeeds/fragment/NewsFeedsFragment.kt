package com.newsfeeds.fragment

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
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

    /**
     * The ViewModel instance is provided by Koin using the viewModelModule from AppModule
     * also the SAME INSTANCE of ViewModel (with data retained) is provided by the Koin after
     * the orientation gets changed.
     */
    private val vm: NewsFeedsViewModel by viewModel()

    private val newsFeedsAdapter by lazy { NewsFeedsAdapter() }

    lateinit var viewDataBinding: FragmentNewsFeedsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * The Fragment isn't retained but the ViewModel is retained after the orientation changes,
         * this could be checked by printing the instance memory address in the below logs.
         */
        Log.d(javaClass.simpleName, "Current Fragment Instance: $this")
        Log.d(javaClass.simpleName, "Current ViewModel Instance: $vm")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = DataBindingUtil.inflate<FragmentNewsFeedsBinding>(inflater, R.layout.fragment_news_feeds, container, false)
        .also {
            viewDataBinding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.apply {

            vm.ldOnNewsFeedsListLoaded.observe(viewLifecycleOwner, Observer {
                newsFeedsAdapter.update(it)
                frmProgress.visibility = View.GONE
            })

            vm.ldOnError.observe(viewLifecycleOwner, Observer { error ->
                error?.let {
                    swpRfrshNewsFeeds.isEnabled = true
                    frmProgress.visibility = View.GONE
                    Snackbar.make(root, it, Snackbar.LENGTH_LONG).show()

                    /**
                     * resetting 'mtldOnError' back to null after once this value is notified,
                     * otherwise the value would be stored in the LiveData and every-time user
                     * changes the orientation then LiveData will instantly listen this value
                     * as soon as we attach the Observer to it and SnackBar would be shown
                     * to the user representing the earlier error.
                     */
                    vm.clearError()
                }
            })

            rclr.apply {
                adapter = newsFeedsAdapter
                addItemDecoration(ItemDecoration(resources.getDimensionPixelSize(R.dimen.rclr_decoration_ht_fragment_news_feeds)))
            }

            swpRfrshNewsFeeds.setOnRefreshListener {
                swpRfrshNewsFeeds.isRefreshing = false
                frmProgress.visibility = View.VISIBLE
                newsFeedsAdapter.clear()
                vm.loadNewsFeeds()
            }

            (requireActivity() as AppCompatActivity).setSupportActionBar(tlbr)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /**
         * if (vm.ldOnNewsFeedsListLoaded.value == null) then load News Feeds
         */
        vm.ldOnNewsFeedsListLoaded.value ?: vm.loadNewsFeeds()
    }
}

private class ItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) top = margin
            left = margin
            right = margin
            bottom = margin
        }
    }
}