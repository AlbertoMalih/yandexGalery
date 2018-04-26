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
import com.golegion2001.galery.detailPicture.URL_PHOTO_KEY
import com.golegion2001.galery.model.Photo
import kotlinx.android.synthetic.main.activity_all_photos.*
import org.koin.android.architecture.ext.viewModel

class AllPhotosActivity : AppCompatActivity(), AllPhotosView, LifecycleOwner {
    private val viewModel: AllPhotosViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()
        setContentView(R.layout.activity_all_photos)

        initDisplayPhotos()

        viewModel.startLoadingPhotosUrls()

        viewModel.isSuchCall.observe(this, Observer { isSuch ->
            isSuch?.let {
                hideIndicatorLoading()
                if (!isSuch)
                    showMessageOnErrorLoad()
            }
        })
        viewModel.onStartCall.observe(this, Observer { showIndicatorLoading() })
    }

    fun showPhotoDetails(photo: Photo){
        viewModel.currentPhotoContainer.currentPhoto = photo
        startActivity(Intent(this, DetailImageActivity::class.java).putExtra(URL_PHOTO_KEY, photo))
    }


    private fun showMessageOnErrorLoad() {
        Toast.makeText(this, R.string.failRequestPhotos, Toast.LENGTH_LONG).show()
    }

    private fun showIndicatorLoading() {
        displayLoad.visibility = View.VISIBLE
    }

    private fun hideIndicatorLoading() {
        displayLoad.visibility = View.GONE
    }

    private fun initFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    private fun initDisplayPhotos() {
        with(allPhotos) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@AllPhotosActivity, resources.getInteger(R.integer.count_columns_previews))
            adapter = createAdapter()
        }
    }

    private fun createAdapter(): PhotosAdapter = PhotosAdapter(viewModel, this, this)
}