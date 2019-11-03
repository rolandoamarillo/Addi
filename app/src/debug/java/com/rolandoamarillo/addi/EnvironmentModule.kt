package com.rolandoamarillo.addi

import com.google.gson.GsonBuilder
import com.rolandoamarillo.addi.network.MockMode
import com.rolandoamarillo.addi.network.MockResponseInterceptor
import okhttp3.Interceptor
import org.koin.dsl.module

val environmentModule = module {

    //Gson
    single {
        GsonBuilder().create()
    }

    single<Interceptor> {
        MockResponseInterceptor(get(), get(), MockMode.FIXED)
    }
}