package com.zihad.imagefilteringapp.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zihad.imagefilteringapp.data.SavedImagesDataState
import com.zihad.imagefilteringapp.repository.SavedImagesRepository
import com.zihad.imagefilteringapp.utilities.CoroutinesUtil
import java.io.File

class SavedImagesViewModel(private val savedImagesRepository: SavedImagesRepository) : ViewModel() {

    private val savedImagesDataState = MutableLiveData<SavedImagesDataState>()

    val savedImagesUiState: LiveData<SavedImagesDataState> get() = savedImagesDataState

    fun loadSavedImages() {
        CoroutinesUtil.io {
            kotlin.runCatching {
                emitSavedImagesUiState(isLoading = true)
                savedImagesRepository.loadSavedImages()
            }.onSuccess { images ->
                if (images.isNullOrEmpty()) {
                    emitSavedImagesUiState(error = "No image found")
                } else {
                    emitSavedImagesUiState(savedImages = images)
                }
            }.onFailure {
                emitSavedImagesUiState(error = it.message.toString())
            }
        }
    }


    private fun emitSavedImagesUiState(
        isLoading: Boolean = false,
        savedImages: List<Pair<File, Bitmap>>? = null,
        error: String? = null
    ) {
        val dataState = SavedImagesDataState(isLoading, savedImages, error)
        savedImagesDataState.postValue(dataState)
    }

}