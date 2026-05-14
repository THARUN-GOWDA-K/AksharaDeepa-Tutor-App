package com.aksharadeepa.tutor

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AksharaDeepaTutorApp : Application() {
	override fun onCreate() {
		super.onCreate()
		try {
			if (FirebaseApp.getApps(this).isEmpty()) {
				FirebaseApp.initializeApp(this)
			}
		} catch (_: Exception) {
			// Firebase is optional in local builds until google-services.json is added.
		}
	}
}
