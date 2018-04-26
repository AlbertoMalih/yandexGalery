package com.golegion2001.galery.detailPicture

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import com.golegion2001.galery.R
import kotlinx.android.synthetic.main.activity_detal_picture.*
import org.koin.android.architecture.ext.viewModel

class DetailImageActivity : AppCompatActivity() {
    private val viewModel: DetailImageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()
        setContentView(R.layout.activity_detal_picture)

        viewModel.loadImage(image)
    }


    private fun initFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }
}
