package com.afflyas.fwcwallpapers.core

import android.app.Activity
import android.app.Application
import com.afflyas.fwcwallpapers.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    companion object {
        const val DEV_TAG = "myDev"
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        //init Dagger injections
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector
}