package com.rafaelrain.headbank.javalinbackend.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.rafaelrain.headbank.javalinbackend.exception.UserNotFoundException
import com.rafaelrain.headbank.javalinbackend.model.User
import com.rafaelrain.headbank.javalinbackend.repository.UserRepository
import com.rafaelrain.headbank.javalinbackend.util.inject
import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context

class UserController : CrudHandler {

    private val repository: UserRepository by inject()
    private val mapper: ObjectMapper by inject()

    override fun create(ctx: Context) {
        val user = ctx.bodyAsClass(User::class.java)
        repository.add(user)

        ctx.status(200)
    }

    override fun delete(ctx: Context, resourceId: String) {
        val status = if (repository.delete(resourceId)) 200 else 500

        ctx.status(status)
    }

    override fun getAll(ctx: Context) {
        val allUsers = repository.getAll()

        ctx.json(allUsers)
    }

    override fun getOne(ctx: Context, resourceId: String) {
        repository.get(resourceId)?.let { user ->
            ctx.json(user)
        } ?: throw UserNotFoundException()
    }

    override fun update(ctx: Context, resourceId: String) {
        val user = repository.get(resourceId) ?: throw UserNotFoundException()

        val money = mapper.readTree(ctx.body()).get("money").intValue()
        repository.add(user.apply {
            this.money = money
        })

        ctx.status(200)
    }
}
