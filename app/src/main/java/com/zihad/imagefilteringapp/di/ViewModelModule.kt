package com.zihad.imagefilteringapp.di

import com.zihad.imagefilteringapp.viewmodel.EditImageViewModel
import com.zihad.imagefilteringapp.viewmodel.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}