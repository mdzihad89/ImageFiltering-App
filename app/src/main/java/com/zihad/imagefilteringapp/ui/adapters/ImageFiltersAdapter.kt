package com.zihad.imagefilteringapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zihad.imagefilteringapp.R
import com.zihad.imagefilteringapp.data.ImageFilter
import com.zihad.imagefilteringapp.databinding.ItemContainerFilterBinding
import com.zihad.imagefilteringapp.listeners.ImageFilterListener

class ImageFiltersAdapter(
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterListener
) :
    RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewholder>() {

    private var selectedFilterPosition = 0
    private var prevSelectedFilterPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewholder {
        val binding = ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageFilterViewholder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewholder, position: Int) {
        with(holder) {
            with(imageFilters[position]) {
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.txtFilterName.text = name
                binding.root.setOnClickListener {
                    if (position != selectedFilterPosition) {
                        imageFilterListener.onFilterSelected(this)
                        prevSelectedFilterPosition = selectedFilterPosition
                        selectedFilterPosition = position
                        with(this@ImageFiltersAdapter) {
                            notifyItemChanged(prevSelectedFilterPosition, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }
                    }

                }
            }
            binding.txtFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.txtFilterName.context,
                    if (selectedFilterPosition == position)
                        R.color.primaryDark
                    else
                        R.color.primaryText
                )
            )
        }
    }

    override fun getItemCount() = imageFilters.size


    inner class ImageFilterViewholder(val binding: ItemContainerFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}