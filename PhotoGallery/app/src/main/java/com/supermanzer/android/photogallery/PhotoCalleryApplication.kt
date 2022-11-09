package com.supermanzer.android.photogallery

import android.app.Application

class PhotoCalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesRepository.initialize(this)
    }
}