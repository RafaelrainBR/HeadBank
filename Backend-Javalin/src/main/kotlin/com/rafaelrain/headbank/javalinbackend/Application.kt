package com.rafaelrain.headbank.javalinbackend

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.rafaelrain.headbank.javalinbackend.controller.UserController
import com.rafaelrain.headbank.javalinbackend.exception.UserNotFoundException
import com.rafaelrain.headbank.javalinbackend.repository.UserRepository
import com.rafaelrain.headbank.javalinbackend.security.AccessManagerImpl
import com.rafaelrain.headbank.javalinbackend.security.AccessRole
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import io.javalin.core.security.SecurityUtil.roles
import org.koin.core.context.startKoin
import org.koin.dsl.module
import redis.clients.jedis.Jedis
import java.io.File
import kotlin.system.exitProcess

class Application {

    val jedis: Jedis

    private val app: Javalin


    val adminKey: String by lazy {
        val file = File("adminKey.txt")
        if (!file.exists()) {
            file.createNewFile()
            file.printWriter().use { it.println("defaultKey") }
        }
        file.readLines()[0]
    }


    init {
        adminKey

        try {
            jedis = Jedis("localhost").also {
                it.connect()
            }

            app = Javalin.create().apply {
                routes {
                    crud("users/:name", UserController(), roles(AccessRole.ADMIN))
                }
                exception(UserNotFoundException::class.java) { _, ctx ->
                    ctx.status(404).result("User not found.")
                }
                config.accessManager(AccessManagerImpl())
            }.start(3333)

        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to initialize the application.")
            exitProcess(1)
        }

        Runtime.getRuntime().addShutdownHook(Thread {
            stop()
        })
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
                        single { ObjectMapper().registerKotlinModule() }
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