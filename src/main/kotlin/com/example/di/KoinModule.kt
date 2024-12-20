package com.example.di

import com.example.common.Constants
import com.example.data.UserDataSourceImpl
import com.example.domain.repository.UserDataSource
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val koinModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase(Constants.DATABASE_NAME)
    }

    single<UserDataSource> {
        UserDataSourceImpl(get())
    }
}