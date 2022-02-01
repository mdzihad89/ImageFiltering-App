package com.zihad.imagefilteringapp.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.zihad.imagefilteringapp.data.ImageFilter
import com.zihad.imagefilteringapp.databinding.ActivityEditImageBinding
import com.zihad.imagefilteringapp.listeners.ImageFilterListener
import com.zihad.imagefilteringapp.ui.adapters.ImageFiltersAdapter
import com.zihad.imagefilteringapp.utilities.displayToast
import com.zihad.imagefilteringapp.utilities.hide
import com.zihad.imagefilteringapp.utilities.show
import com.zihad.imagefilteringapp.viewmodel.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    companion object {
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private lateinit var binding: ActivityEditImageBinding
    private val viewModel: EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()
    }

    private fun setupObservers() {
        viewModel.imagePreviewUiState.observe(this, {
            val dataState = it ?: return@observe
            binding.progressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                originalBitmap = bitmap
                filteredBitmap.value = bitmap
                with(originalBitmap) {
                    gpuImage.setImage(this)
                    binding.imgPreview.show()
                    viewModel.loadImageFilters(this)
                }

            } ?: kotlin.run {
                dataState.error?.let { error -> displayToast(error) }
            }
        })

        viewModel.imageFilterUiState.observe(this, {
            val imageFilterDataState = it ?: return@observe
            binding.imgFilterProgressBar.visibility =
                if (imageFilterDataState.isLoading) View.VISIBLE else View.GONE
            imageFilterDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.recyclerViewFilter.adapter = adapter
                }
            } ?: kotlin.run {
                imageFilterDataState.error?.let { error -> displayToast(error) }
            }
        })
        filteredBitmap.observe(this, { bitmap ->
            binding.imgPreview.setImageBitmap(bitmap)
        })

        viewModel.saveFilteredUiState.observe(this, {
            val saveFilteredImageDataState = it ?: return@observe
            if (saveFilteredImageDataState.isLoading) {
                binding.imgSave.hide()
                binding.savingProgressBar.show()
            } else {
                binding.savingProgressBar.hide()
                binding.imgSave.show()
            }
            saveFilteredImageDataState.uri?.let { savedImageUri ->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also { filteredImageIntent ->
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
                    startActivity(filteredImageIntent)

                }
            } ?: kotlin.run {
                saveFilteredImageDataState.error?.let { error -> displayToast(error) }
            }
        })
    }

    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }

    private fun setListeners() {
        binding.imgBack.setOnClickListener { onBackPressed() }

        binding.imgPreview.setOnLongClickListener {
            binding.imgPreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }

        binding.imgPreview.setOnClickListener {
            binding.imgPreview.setImageBitmap(filteredBitmap.value)
        }

        binding.imgSave.setOnClickListener {
            filteredBitmap.value?.let { bitmap ->
                viewModel.saveFilteredImage(bitmap)
            }
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}