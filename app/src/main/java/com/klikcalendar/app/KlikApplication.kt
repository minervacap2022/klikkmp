package com.klikcalendar.app

import android.app.Application
import com.klikcalendar.app.audio.AndroidAudioRecorder

class KlikApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize AndroidAudioRecorder
        AndroidAudioRecorder.initialize(this)
    }
}
