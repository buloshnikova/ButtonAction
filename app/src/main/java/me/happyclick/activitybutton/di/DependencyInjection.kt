package me.happyclick.activitybutton.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.ww.roxie.BaseViewModel
import me.happyclick.activitybutton.model.ButtonActionRepository
import me.happyclick.activitybutton.model.api.ActionService
import me.happyclick.activitybutton.ui.ButtonAction
import me.happyclick.activitybutton.ui.ButtonActionState
import me.happyclick.activitybutton.ui.ButtonActionUseCaseImpl
import me.happyclick.activitybutton.ui.ButtonActionViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

interface DependencyInjection {

    val buttonActionViewModel: BaseViewModel<ButtonAction, ButtonActionState>
}

class DependencyInjectionImpl(val context: Context): DependencyInjection {

    override lateinit var buttonActionViewModel: BaseViewModel<ButtonAction, ButtonActionState>

    init {
        val apiBaseUrl = "https://www.boredapi.com"

        val moshi = Moshi.Builder().build()

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()

        val actionService = retrofit.create(ActionService::class.java)

        val buttonActionRepository = ButtonActionRepository(actionService)

        val getButtonActionUseCase = ButtonActionUseCaseImpl(buttonActionRepository)

        buttonActionViewModel = ButtonActionViewModel(ButtonActionState(activity = false), getButtonActionUseCase, context)
    }
}