package com.rafaelrain.headbank

import com.rafaelrain.headbank.controller.UserController
import com.rafaelrain.headbank.exception.UserNotFoundException
import com.rafaelrain.headbank.repository.UserRepository
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import org.koin.core.context.startKoin
import org.koin.dsl.module
import redis.clients.jedis.Jedis
import kotlin.system.exitProcess

class Application {

    val jedis: Jedis

    private val app: Javalin

    init {
        try {
            jedis = Jedis("localhost")
            jedis.connect()

            app = Javalin.create().apply {
                routes {
                    crud("users/:name", UserController())
                }
                exception(UserNotFoundException::class.java) { _, ctx ->
                    ctx.status(404).result("User not found.")
                }
            }.start(3333)

        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to initialize the application.")
            exitProcess(1)
        }
    }

    fun stop() {
        app.stop()
        jedis.disconnect()
    }

    companion object {
        fun start() {
            startKoin {
                modules(
                    module(true) {
                        single { Application() }
                        single { UserRepository(get()) }
                    }
                )
            }
        }
    }
}

fun main() {
    Application.start()
}