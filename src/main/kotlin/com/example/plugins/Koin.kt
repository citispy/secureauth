package com.example.plugins

import io.ktor.server.application.*
import io.ktor.util.*
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin


fun Application.configureKoin() {
    install(CustomKoinPlugin) {
        modules(com.example.di.koinModule)
        GlobalContext.startKoin(this)
    }
}

internal class CustomKoinPlugin(internal val koinApplication: KoinApplication) {
    companion object Plugin: BaseApplicationPlugin<ApplicationCallPipeline, KoinApplication, CustomKoinPlugin> {
        override val key: AttributeKey<CustomKoinPlugin>
            get() = AttributeKey<CustomKoinPlugin>("CustomKoinPlugin")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: KoinApplication.() -> Unit
        ): CustomKoinPlugin = CustomKoinPlugin(startKoin(configure))
    }
}