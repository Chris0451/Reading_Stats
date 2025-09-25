package com.project.reading_stats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.project.reading_stats.navigation.AppNavHost
import com.project.reading_stats.core.ui.theme.ReadingStatsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReadingStatsTheme {
                AppNavHost()
            }
        }
    }
}
