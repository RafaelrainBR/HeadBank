package com.rafaelrain.headbank.javalinbackend.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.rafaelrain.headbank.javalinbackend.Application
import com.rafaelrain.headbank.javalinbackend.util.inject
import io.javalin.core.security.Role
import io.javalin.http.Context

enum class AccessRole(val keyPath: () -> String) : Role {
    ADMIN({ application.adminKey }),
    ANYONE({ "" });

    fun couldComplete(ctx: Context): Boolean {
        val key = ctx.header("key") ?: return false

        return key == keyPath()
    }

    companion object {
        val application: Application by inject()
        val mapper: ObjectMapper by inject()
    }
}