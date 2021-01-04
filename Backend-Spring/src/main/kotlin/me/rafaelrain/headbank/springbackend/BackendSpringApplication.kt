package me.rafaelrain.headbank.springbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendSpringApplication

fun main(args: Array<String>) {
    runApplication<BackendSpringApplication>(*args)
}
