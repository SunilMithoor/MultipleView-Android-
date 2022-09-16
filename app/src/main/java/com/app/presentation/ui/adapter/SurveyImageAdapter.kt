package com.app.presentation.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.presentation.util.ImageController
import com.app.presentation.util.SquareImageView
import com.squareup.picasso.Picasso


class SurveyImageAdapter(
    context: Context,
    imageController: ImageController,
    imagePaths: ArrayList<Uri>
) :
    RecyclerView.Adapter<SurveyImageAdapter.ViewHolder>() {
    private val context: Context
    private var imagePaths: ArrayList<Uri>
    private val imageController: ImageController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imagePath: Uri = imagePaths[position]
        Picasso.get()
            .load(imagePath)
            .fit()
            .centerCrop()
            .into(holder.imageView)

        holder.imageClose.setOnClickListener { v: View? ->
            removeAt(
                holder.adapterPosition
            )
        }
    }

    fun changePath(imagePaths: ArrayList<Uri>) {
        this.imagePaths = imagePaths
        imageController.setImgMain(imagePaths[0])
        notifyDataSetChanged()
    }

    private fun removeAt(position: Int) {
        imagePaths.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, imagePaths.size)
    }

    override fun getItemCount(): Int {
        return imagePaths.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageClose: AppCompatImageView
        var imageView: SquareImageView

        init {
            imageView = itemView.findViewById(R.id.img_item)
            imageClose = itemView.findViewById(R.id.img_close)
        }
    }

    init {
        this.context = context
        this.imageController = imageController
        this.imagePaths = imagePaths
    }
}