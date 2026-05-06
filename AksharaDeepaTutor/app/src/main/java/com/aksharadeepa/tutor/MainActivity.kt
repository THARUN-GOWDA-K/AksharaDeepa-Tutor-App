package com.aksharadeepa.tutor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aksharadeepa.tutor.ui.navigation.AppNavigation
import com.aksharadeepa.tutor.ui.theme.AksharaDeepaTutorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AksharaDeepaTutorTheme {
                AppNavigation()
            }
        }
    }
}
