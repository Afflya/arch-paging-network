package com.afflyas.fwcwallpapers.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.afflyas.fwcwallpapers.ui.listimages.ListImagesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Module to bind ViewModel's
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ListImagesViewModel::class)
    abstract fun bindListImagesViewModel(listMoviesViewModel: ListImagesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}