package com.zihad.imagefilteringapp.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zihad.imagefilteringapp.data.ImageFilter
import com.zihad.imagefilteringapp.data.ImageFilterDataState
import com.zihad.imagefilteringapp.data.ImagePreviewDataState
import com.zihad.imagefilteringapp.data.SaveFilteredImageDataState
import com.zihad.imagefilteringapp.repository.EditImageRepository
import com.zihad.imagefilteringapp.utilities.CoroutinesUtil

class EditImageViewModel(private val editImageRepository: EditImageRepository) : ViewModel() {

    //region :: Prepare Image preview
    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri) {
        CoroutinesUtil.io {
            kotlin.runCatching {
                emitImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null) {
                    emitImagePreviewUiState(bitmap = bitmap)
                } else {
                    emitImagePreviewUiState(error = "Unable to prepare image preview")
                }
            }.onFailure {
                emitImagePreviewUiState(error = it.message.toString())
            }
        }
    }

    private fun emitImagePreviewUiState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ) {
        val dataState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }

    // endregion

    //region:: Load image filters
    private val imageFilterDataState = MutableLiveData<ImageFilterDataState>()
    val imageFilterUiState: LiveData<ImageFilterDataState> get() = imageFilterDataState

    fun loadImageFilters(originalImage: Bitmap) {
        CoroutinesUtil.io {
            kotlin.runCatching {
                emitImageFilterUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess { imageFilters ->
                emitImageFilterUiState(imageFilters = imageFilters)
            }.onFailure {
                emitImageFilterUiState(error = it.message.toString())
            }
        }
    }

    private fun getPreviewImage(originalImage: Bitmap): Bitmap {
        return kotlin.runCatching {
            val previewWidth = 150
            val previewHeight = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage, previewWidth, previewHeight, false)
        }.getOrDefault(originalImage)
    }

    private fun emitImageFilterUiState(
        isLoading: Boolean = false,
        imageFilters: List<ImageFilter>? = null,
        error: String? = null
    ) {
        val dataState = ImageFilterDataState(isLoading, imageFilters, error)
        imageFilterDataState.postValue(dataState)
    }

    //endregion

    //region:: Save filtered image
    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredUiState: LiveData<SaveFilteredImageDataState> get() = saveFilteredImageDataState

    fun saveFilteredImage(filteredBitmap: Bitmap) {
        CoroutinesUtil.io {
            kotlin.runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editImageRepository.saveFilteredImage(filteredBitmap)
            }.onSuccess { saveImageUri ->
                emitSaveFilteredImageUiState(uri = saveImageUri)
            }.onFailure {
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveFilteredImageUiState(
        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ) {
        val dataState = SaveFilteredImageDataState(isLoading, uri, error)
        saveFilteredImageDataState.postValue(dataState)
    }
    //endregion
}