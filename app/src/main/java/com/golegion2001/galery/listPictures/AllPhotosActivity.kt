package com.golegion2001.galery.listPictures

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.Window
import android.widget.Toast
import com.golegion2001.galery.R
import com.golegion2001.galery.detailPicture.DetailImageActivity
import com.golegion2001.galery.model.Photo
import kotlinx.android.synthetic.main.activity_all_photos.*
import org.koin.android.architecture.ext.viewModel

private const val COUNT_SPAN_PHOTOS = 2

class AllPhotosActivity : AppCompatActivity(), LifecycleOwner, AllPhotosView {
    private val viewModel: AllPhotosViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()
        setContentView(R.layout.activity_all_photos)

        initDisplayPhotos()

        initIndicatorFirstLoad()
    }

    override fun showPhotoDetails(photo: Photo) {
        setCurrentPhoto(photo)
        startActivity(Intent(this, DetailImageActivity::class.java))
    }


    private fun initIndicatorFirstLoad() {
        if (isFirstLoad()) {
            initOnStartFirstLoad()
            initOnFinishFirstLoad()
        }
    }

    private fun initOnFinishFirstLoad() {
        viewModel.onStartCall.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                onStartFirstLoad()
                viewModel.onStartCall.removeObserver(this)
            }
        })
    }

    private fun initOnStartFirstLoad() {
        viewModel.callIsSuch.observe(this, object : Observer<Int> {
            override fun onChanged(countLoadedPhotos: Int?) {
                onEndFirstLoad()
                if (countLoadedPhotos == FAIL_LOADING_KEY ) showMessageOnErrorLoad()
                viewModel.callIsSuch.removeObserver(this)
            }
        })
    }

    private fun isFirstLoad() = viewModel.allPhotos.isEmpty()

    private fun setCurrentPhoto(photo: Photo) {
        viewModel.currentPhotoContainer.currentPhoto = photo
    }

    override fun showMessageOnErrorLoad() {
        Toast.makeText(this, R.string.failRequestPhotos, Toast.LENGTH_LONG).show()
    }

    fun onStartFirstLoad() {
        displayLoad.visibility = View.VISIBLE
    }

    fun onEndFirstLoad() {
        displayLoad.visibility = View.GONE
    }

    private fun initFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    private fun initDisplayPhotos() {
        with(allPhotos) {
            layoutManager = GridLayoutManager(this@AllPhotosActivity, COUNT_SPAN_PHOTOS)
            adapter = createAdapter()
        }
    }

    private fun createAdapter(): PhotosAdapter = PhotosAdapter(viewModel, this, this)
}