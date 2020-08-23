package com.newsfeeds

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.newsfeeds.api.model.NewsFeed
import com.newsfeeds.api.model.NewsFeedsList
import com.newsfeeds.repository.remote.NewsFeedsRepository
import com.newsfeeds.viewmodel.NewsFeedsViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.*
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
    lateinit var newsFeedsRepository: NewsFeedsRepository

    private var viewModel : NewsFeedsViewModel? = null

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        viewModel = NewsFeedsViewModel(newsFeedsRepository, Schedulers.trampoline(),
            Schedulers.trampoline(), CompositeDisposable())
    }

    @Test
    fun testApiSuccess() {
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

        Mockito.`when`(newsFeedsRepository.getNewsFeeds()).thenReturn(Single.just(newsFeedsList))

        viewModel?.let {
            it.loadNewsFeeds()
            Assert.assertEquals(newsFeedsList, it.ldOnNewsFeedsListLoaded.value)
            Assert.assertNull(it.ldOnError.value)
        }
    }

    @Test
    fun testApiError() {
        val throwable = Throwable("Api Error")

        Mockito.`when`(newsFeedsRepository.getNewsFeeds()).thenReturn(Single.create { it.onError(throwable) })

        viewModel?.let {
            it.loadNewsFeeds()
            Assert.assertNull(it.ldOnNewsFeedsListLoaded.value)
            Assert.assertEquals(throwable.message, it.ldOnError.value)
        }
    }

    @After
    fun afterTest() {
        viewModel = null
    }

}