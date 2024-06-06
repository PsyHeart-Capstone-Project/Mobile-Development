package com.capstone.psyheart

import android.app.Application
import android.content.Context

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        context = this
    }

    companion object {
        private lateinit var context: App

        fun getAppContext(): Context {
            return context.applicationContext
        }

    }
}