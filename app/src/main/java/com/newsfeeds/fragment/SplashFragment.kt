package com.newsfeeds.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.newsfeeds.R
import com.newsfeeds.databinding.FragmentSplashBinding
import com.newsfeeds.viewmodel.SplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {

    private val vm: SplashViewModel by viewModel() // The ViewModel instance is provide by Koin
    // using the viewModelModule from AppModule

    private lateinit var viewDataBinding: FragmentSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true // will maintain the Fragment instance when orientation changes
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            DataBindingUtil.inflate<FragmentSplashBinding>(inflater, R.layout.fragment_splash, container, false)
                    .also { viewDataBinding = it }.root


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.ldOnContinue.observe(viewLifecycleOwner, Observer { success ->
            if (success) findNavController().navigate(R.id.action_splashFragment_to_newsFeedsFragment)
            else Snackbar.make(viewDataBinding.root, R.string.splash_timer_error_msg, Snackbar.LENGTH_SHORT).show()
        })
        vm.startTimer()
    }
}