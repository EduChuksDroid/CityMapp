package com.educhuks.citymapp.di

import androidx.room.Room
import com.educhuks.citymapp.data.CitiesAPI
import com.educhuks.citymapp.data.CitiesRepository
import com.educhuks.citymapp.data.database.CitiesDatabase
import com.educhuks.citymapp.ui.main.CitiesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Depending on how many components(Interfaces, Repositories, ViewModels, etc.)
// I'd create a separate module for each one

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(CitiesAPI::class.java) }
    single {
        Room.databaseBuilder(
            androidContext(),
            CitiesDatabase::class.java,
            "cities_database"
        ).build()
    }
    single { get<CitiesDatabase>().citiesDao() }
    single { CitiesRepository(get(), get()) }
    viewModel { CitiesViewModel(get()) }
}