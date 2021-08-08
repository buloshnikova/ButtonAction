package me.happyclick.activitybutton

import android.app.Application
import android.content.Context
import com.ww.roxie.Roxie
import me.happyclick.activitybutton.di.DependencyInjection
import me.happyclick.activitybutton.di.DependencyInjectionImpl

open class Application : Application() {

    open val di: DependencyInjection by lazy {
        DependencyInjectionImpl(this.applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        Roxie.enableLogging()
    }
}

val Context.di: DependencyInjection
    get() = (this.applicationContext as me.happyclick.activitybutton.Application).di