package com.neowise.leads

import android.app.Application
import androidx.core.provider.FontRequest
import androidx.emoji2.text.EmojiCompat
import androidx.emoji2.text.FontRequestEmojiCompatConfig
import com.neowise.leads.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class LeadsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup for emoji support
        val fontRequest = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "Noto Color Emoji Compat",
            R.array.com_google_android_gms_fonts_certs
        )
        val config: EmojiCompat.Config = FontRequestEmojiCompatConfig(this, fontRequest)
        EmojiCompat.init(config)
        // Initialize Koin
        startKoin {
            androidLogger()
            androidContext(this@LeadsApp)
            modules(appModule)
        }
    }
}