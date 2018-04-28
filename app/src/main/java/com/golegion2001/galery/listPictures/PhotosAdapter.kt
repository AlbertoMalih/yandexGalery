package com.golegion2001.galery.listPictures

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.golegion2001.galery.R
import com.golegion2001.galery.extensions.load
import com.golegion2001.galery.model.Photo
import kotlinx.android.synthetic.main.all_photos_item.view.*


private const val TYPE_ITEM = 0
private const val TYPE_FOOTER = 1
private const val COUNT_NOT_ITEM_ELEMENTS = 1
private const val MAX_COUNT_COLUMNS = 2
private const val ITEM_COUNT_COLUMNS = 1

class PhotosAdapter(private val viewModel: AllPhotosViewModel, lifecycleOwner: LifecycleOwner, private val activity: AllPhotosView)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listPhotos: MutableList<Photo> = viewModel.allPhotos
    var isLoadedAllPhotos = false


    init {
        viewModel.callIsSuch.observe(lifecycleOwner, Observer { countLoadedPhotos ->
            when (countLoadedPhotos) {
                FAIL_LOADING_KEY -> activity.showMessageOnErrorLoad()
                LOADED_ALL_PHOTOS -> onLoadedAllPhotos()
                else -> onSuchLoadPhotos()
            }
        })
        if (listPhotos.isEmpty()) startLoading()
    }

    private fun onLoadedAllPhotos() {
        isLoadedAllPhotos = true
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                            when (getItemViewType(position)) {
                                TYPE_FOOTER -> MAX_COUNT_COLUMNS
                                TYPE_ITEM -> ITEM_COUNT_COLUMNS
                                else -> throw Exception("Not valid ViewHolder type.")
                            }
                }
    }

    override fun onCreateViewHolder(parent: ViewGroup, typeHolder: Int): RecyclerView.ViewHolder =
            when (typeHolder) {
                TYPE_FOOTER -> inflateFooter(parent)
                TYPE_ITEM -> inflateItem(parent)
                else -> throw Exception("Not valid ViewHolder type.")
            }

    override fun getItemCount(): Int = when {
        listPhotos.isEmpty() -> 0
        isLoadedAllPhotos -> listPhotos.size
        else -> listPhotos.size + COUNT_NOT_ITEM_ELEMENTS
    }

    override fun getItemViewType(position: Int): Int =
            if ((position == listPhotos.size) && (listPhotos.isNotEmpty()))
                TYPE_FOOTER
            else
                TYPE_ITEM

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoHolder -> listPhotos[position].let { photo -> holder.photoView.load(photo.previewUrl) }
            is FooterHolder -> startLoading()
        }
    }


    private fun inflateItem(parent: ViewGroup) =
            PhotoHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.all_photos_item, parent, false), this)

    private fun inflateFooter(parent: ViewGroup) =
            FooterHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.all_photos_footer, parent, false))

    private fun onSuchLoadPhotos() {
        notifyDataSetChanged()
    }

    private fun startLoading() {
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

    private inner class FooterHolder(view: View) : RecyclerView.ViewHolder(view)
}