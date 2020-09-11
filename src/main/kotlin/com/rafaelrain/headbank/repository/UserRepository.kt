package com.rafaelrain.headbank.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.rafaelrain.headbank.Application
import com.rafaelrain.headbank.model.User

class UserRepository(application: Application) {

    private val hash = "BANK_USERS"

    private val jedis = application.jedis
    private val mapper = ObjectMapper().registerKotlinModule()

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