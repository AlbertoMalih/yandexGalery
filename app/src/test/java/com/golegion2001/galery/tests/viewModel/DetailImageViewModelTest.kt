package com.golegion2001.galery.tests.viewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.detailPicture.DetailImageViewModel
import com.golegion2001.galery.di.galeryTestModules
import com.golegion2001.galery.model.Photo
import io.reactivex.Single
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class DetailImageViewModelTest : KoinTest {
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()
    private val photoRepository: PhotosRepository by inject()
    private val viewModel: DetailImageViewModel by inject()

    @Before
    fun beforeTests() {
        startKoin(galeryTestModules)
        prepareRepository()
    }

    @After
    fun afterTest() {
        closeKoin()
    }

    @Test
    fun testLoadingImageUrl() {
        viewModel.loadImage()
        Assert.assertNotSame(viewModel.urlLoadedImage.value, "")
    }


    private fun prepareRepository() {
        `when`(photoRepository.loadImageUrl(Photo())).thenReturn(Single.create<String> { emitter ->
            emitter.onSuccess(SOME_MOCK_IMAGE_URL)
        })
    }
}

private const val SOME_MOCK_IMAGE_URL = "SOME_MOCK_IMAGE_URL"