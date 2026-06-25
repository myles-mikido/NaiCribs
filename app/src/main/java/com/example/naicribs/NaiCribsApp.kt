package com.example.naicribs

import android.app.Application
import com.google.firebase.FirebaseApp

class NaiCribsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase auto-initializes from google-services.json in most cases,
        // but calling this explicitly here guarantees it's ready before
        // any Activity, ViewModel, or Repository tries to use
        // FirebaseAuth, FirebaseFirestore, or FirebaseStorage.
        FirebaseApp.initializeApp(this)
    }
}