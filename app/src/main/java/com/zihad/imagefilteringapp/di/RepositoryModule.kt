package com.zihad.imagefilteringapp.di

import com.zihad.imagefilteringapp.repository.EditImageRepository
import com.zihad.imagefilteringapp.repository.EditImageRepositoryImpl
import com.zihad.imagefilteringapp.repository.SavedImagesRepository
import com.zihad.imagefilteringapp.repository.SavedImagesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
    factory<SavedImagesRepository> { SavedImagesRepositoryImpl(androidContext()) }
}