package com.afflyas.fwcwallpapers.di

import com.afflyas.fwcwallpapers.ui.image.ImageFragment
import com.afflyas.fwcwallpapers.ui.listimages.ListImagesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeListImagesFragment() : ListImagesFragment

    @ContributesAndroidInjector
    abstract fun contributeImageFragment() : ImageFragment

}