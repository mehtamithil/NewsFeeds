package com.newsfeeds

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.newsfeeds.api.ApiService
import com.newsfeeds.api.model.NewsFeed
import com.newsfeeds.api.model.NewsFeedsList
import com.newsfeeds.repository.remote.NewsFeedsRepository
import com.newsfeeds.viewmodel.NewsFeedsViewModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsFeedsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var successObserver: Observer<NewsFeedsList>

    @Mock
    lateinit var failureObserver: Observer<String>

    @Mock
    lateinit var newsFeedsRepository: NewsFeedsRepository

    @Mock
    lateinit var apiService: ApiService

    lateinit var viewModel: NewsFeedsViewModel

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)

        successObserver = Mockito.mock(Observer::class.java) //Observer { Log.d(javaClass.simpleName, "NewsFeedsList: $it") }
        failureObserver = Mockito.mock(Observer::class.java) //Observer { Log.d(javaClass.simpleName, "Error: $it") }

        viewModel = NewsFeedsViewModel(
            newsFeedsRepository, Schedulers.trampoline(), Schedulers.trampoline()
        )
        viewModel.ldOnNewsFeedsListLoaded.observeForever(successObserver)
        viewModel.ldOnError.observeForever(failureObserver)
    }

    @Test
    fun testApiSuccess() {
        // Mock API response
        val newsFeedsList = NewsFeedsList(
            "About Canada", mutableListOf(
                NewsFeed(
                    "Beavers",
                    "Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony",
                    "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
                ),
                NewsFeed(
                    "Flag",
                    null,
                    "http://images.findicons.com/files/icons/662/world_flag/128/flag_of_canada.png"
                ),
                NewsFeed(
                    null,
                    "It is a well known fact that polar bears are the main mode of transportation in Canada. They consume far less gas and have the added benefit of being difficult to steal.",
                    "http://1.bp.blogspot.com/_VZVOmYVm68Q/SMkzZzkGXKI/AAAAAAAAADQ/U89miaCkcyo/s400/the_golden_compass_still.jpg"
                ),
                NewsFeed(
                    "Eh",
                    "A chiefly Canadian interrogative utterance, usually expressing surprise or doubt or seeking confirmation.",
                    null
                ),
                NewsFeed(null, null, null)
            )
        )

        `when`(apiService.getNewsFeeds()).thenReturn(Single.just(newsFeedsList))
        viewModel.loadNewsFeeds()

        verify() { successObserver.onChanged(capture(slots)) }

        //Assert.assertEquals(newsFeedsList, viewModel.ldOnNewsFeedsListLoaded.value)
        //Assert.assertEquals(null, viewModel.ldOnError.value)
    }

    @Test
    fun testApiError() {
        val throwable = Throwable("Api Error")

        `when`(apiService.getNewsFeeds()).thenReturn(Single.error(throwable))
        viewModel.loadNewsFeeds()

        Assert.assertEquals(null, viewModel.ldOnNewsFeedsListLoaded.value)
        Assert.assertEquals(throwable, viewModel.ldOnError.value)
    }
}