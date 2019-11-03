package com.rolandoamarillo.addi

import androidx.room.Room
import com.rolandoamarillo.addi.db.AppDatabase
import com.rolandoamarillo.addi.network.ProspectApi
import com.rolandoamarillo.addi.repository.ProspectDataSource
import com.rolandoamarillo.addi.repository.ProspectRepository
import com.rolandoamarillo.addi.viewmodel.AddProspectViewModel
import com.rolandoamarillo.addi.viewmodel.AgendaViewModel
import com.rolandoamarillo.addi.viewmodel.ProspectsViewModel
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

/**
 * Application module
 */
val addiModule = module {

    //Retrofit

    single {
        OkHttpClient.Builder()
            .addInterceptor(get())
            .dispatcher(get())
            .build()
    }

    single {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 3
        dispatcher
    }

    single {
        Retrofit.Builder()
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .baseUrl("https://www.example.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        get<Retrofit>().create(ProspectApi::class.java)
    }

    //Database

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "addi-database").build()
    }

    single {
        get<AppDatabase>().prospectDao()
    }

    //Repository

    single<ProspectDataSource> {
        ProspectRepository(get(), get())
    }

    //ViewModel

    viewModel { ProspectsViewModel(get()) }

    viewModel { AgendaViewModel(get()) }

    viewModel { AddProspectViewModel(get()) }

}