package com.golegion2001.galery.tests.viewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.di.galeryTestModules
import com.golegion2001.galery.extensions.any
import com.golegion2001.galery.extensions.randomString
import com.golegion2001.galery.listPictures.AllPhotosViewModel
import com.golegion2001.galery.model.MEDIA_TYPE_IMAGE
import com.golegion2001.galery.model.Photo
import io.reactivex.Single
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito.`when`


@RunWith(JUnit4::class)
class AllPhotosViewModelTest : KoinTest {
    @JvmField
    @Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val photoRepository: PhotosRepository by inject()
    private val viewModel: AllPhotosViewModel by inject()

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
    fun testLoadPhotos() {
        viewModel.loadPhotos()
        Assert.assertTrue(viewModel.allPhotos.isNotEmpty())
        Assert.assertTrue(viewModel.callIsSuch.value == SIZE_PORTION_MOCKED_PHOTOS)
        Assert.assertTrue(viewModel.onStartCall.value == true)
    }

    @Test
    fun testFiltrationPhotos() {
        Assert.assertTrue(viewModel.photoIsValid(Photo(randomString(), "", randomString(),
                randomString(), randomString(), randomString(), MEDIA_TYPE_IMAGE)))
        Assert.assertFalse(viewModel.photoIsValid(Photo(randomString(), "", randomString(),
                randomString(), randomString(), "", MEDIA_TYPE_IMAGE)))
        Assert.assertFalse(viewModel.photoIsValid(Photo(randomString(), "", randomString(),
                randomString(), randomString(), randomString(), "")))
    }


    private fun createRandomPhotos(): List<Photo> =
            (0 until SIZE_PORTION_MOCKED_PHOTOS).mapTo(mutableListOf()) {
                Photo(randomString(), "", randomString(),
                        randomString(), randomString(), randomString(), MEDIA_TYPE_IMAGE)
            }

    private fun prepareRepository() {
        `when`(photoRepository.loadPortionPhotosUrls()).thenReturn(Single.create<List<Photo>> {
            it.onSuccess(createRandomPhotos())
        })

        `when`(photoRepository.setPreviewUrl(any())).thenAnswer {
            (it.arguments[0] as Photo).previewUrl = MOCK_PREVIEW_URL
            null
        }
    }
}

private const val SIZE_PORTION_MOCKED_PHOTOS = 20
private const val MOCK_PREVIEW_URL = "MOCK_PREVIEW_URL"