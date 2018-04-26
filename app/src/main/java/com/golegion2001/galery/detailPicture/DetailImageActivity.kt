package com.golegion2001.galery.detailPicture

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.widget.Toast
import com.golegion2001.galery.R
import com.golegion2001.galery.model.Photo
import io.reactivex.Completable
import io.reactivex.Maybe
import kotlinx.android.synthetic.main.activity_detal_picture.*
import org.koin.android.architecture.ext.viewModel

class DetailImageActivity : AppCompatActivity(), DetailImageView {
    private val viewModel: DetailImageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()
        setContentView(R.layout.activity_detal_picture)

        viewModel.loadImage(image)
    }

    override fun onErrorLoadImage() {
        Toast.makeText(this, resources.getString(R.string.invalidLoadImage), Toast.LENGTH_LONG).show()
    }


    private fun initFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }
}
