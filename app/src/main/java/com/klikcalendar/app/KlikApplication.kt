package com.klikcalendar.app

import android.app.Application
import com.klikcalendar.shared.audio.AndroidAudioRecorder

class KlikApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize AndroidAudioRecorder
        AndroidAudioRecorder.initialize(this)
    }
}
