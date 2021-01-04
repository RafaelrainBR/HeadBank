package com.rafaelrain.headbank.javalinbackend.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rafaelrain.headbank.javalinbackend.Application
import com.rafaelrain.headbank.javalinbackend.model.User
import com.rafaelrain.headbank.javalinbackend.util.inject

class UserRepository(application: Application) {

    private val hash = "BANK_USERS"

    private val jedis = application.jedis
    private val mapper: ObjectMapper by inject()

    fun get(name: String): User? {
        return jedis.hget(hash, name)?.let { mapper.readValue(it) }
    }

    fun getAll(): ArrayList<User> {
        return ArrayList(
            jedis.hvals(hash).map { mapper.readValue<User>(it) }
        )
    }

    fun add(user: User) {
        jedis.hset(hash, user.name, mapper.writeValueAsString(user))
    }

    fun delete(name: String): Boolean {
        return jedis.hdel(hash, name) == 1L
    }

}