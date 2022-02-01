package com.zihad.imagefilteringapp.ui.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zihad.imagefilteringapp.databinding.ItemContainerSavedImagesBinding
import com.zihad.imagefilteringapp.listeners.SavedImageListener
import java.io.File

class SavedImagesAdapter(
    private val savedImages: List<Pair<File, Bitmap>>,
    private val savedImageListener: SavedImageListener
) :
    RecyclerView.Adapter<SavedImagesAdapter.SavedImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImagesViewHolder {
        val binding = ItemContainerSavedImagesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImagesViewHolder, position: Int) {
        with(holder) {
            with(savedImages[position]) {
                binding.imageSaved.setImageBitmap(second)
                binding.root.setOnClickListener { savedImageListener.onImageClicked(first) }
            }
        }
    }

    override fun getItemCount() = savedImages.size


    inner class SavedImagesViewHolder(val binding: ItemContainerSavedImagesBinding) :
        RecyclerView.ViewHolder(binding.root)
}