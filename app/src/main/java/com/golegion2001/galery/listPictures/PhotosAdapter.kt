package com.golegion2001.galery.listPictures

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.golegion2001.galery.R
import com.golegion2001.galery.extensions.load
import com.golegion2001.galery.model.Photo
import kotlinx.android.synthetic.main.photo_item.view.*

private const val VISIBLE_THRESHOLD = 2

class PhotosAdapter(private val viewModel: AllPhotosViewModel, lifecycleOwner: LifecycleOwner, private val activity: AllPhotosActivity)
    : RecyclerView.Adapter<PhotosAdapter.PhotoHolder>() {
    private var listPhotos: MutableList<Photo> = mutableListOf()
    private var isLoading = false


    init {
        viewModel.photos.observe(lifecycleOwner, Observer<List<Photo>> { photos ->
            photos?.let { addPhotos(photos) }
        })
        viewModel.isSuchCall.observe(lifecycleOwner, Observer { isLoading = false })
    }

    override fun onCreateViewHolder(parent: ViewGroup, typeHolder: Int): PhotoHolder {
        return PhotoHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false), this)
    }

    override fun getItemCount(): Int = listPhotos.size

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        listPhotos[position].let { photo -> holder.photoView.load(photo.previewUrl) }
        if ((itemCount - position) < VISIBLE_THRESHOLD) startLoading()
    }


    private fun addPhotos(photosForAdd: List<Photo>) {
        listPhotos.addAll(photosForAdd)
        notifyDataSetChanged()
    }

    private fun startLoading() {
        isLoading = true
        viewModel.loadPhotos()
    }

    private fun showPhotoDetails(photoPosition: Int) {
        activity.showPhotoDetails(listPhotos[photoPosition])
    }


    class PhotoHolder(itemView: View, adapter: PhotosAdapter) : RecyclerView.ViewHolder(itemView) {
        val photoView: ImageView = itemView.photo

        init {
            itemView.setOnClickListener({ _ ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    adapter.showPhotoDetails(adapterPosition)
                }
            })
        }
    }
}