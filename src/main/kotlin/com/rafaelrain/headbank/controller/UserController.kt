package com.rafaelrain.headbank.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rafaelrain.headbank.exception.UserNotFoundException
import com.rafaelrain.headbank.model.User
import com.rafaelrain.headbank.repository.UserRepository
import com.rafaelrain.headbank.util.inject
import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context

class UserController : CrudHandler {

    private val repository: UserRepository by inject()

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
            return
        } ?: throw UserNotFoundException()
    }

    override fun update(ctx: Context, resourceId: String) {
        val user = repository.get(resourceId) ?: throw UserNotFoundException()

        val money = jacksonObjectMapper().readTree(ctx.body()).get("money").intValue()
        user.money = money

        ctx.status(200)
    }
}
