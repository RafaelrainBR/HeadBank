package com.rafaelrain.headbank.javalinbackend.security

import io.javalin.core.security.AccessManager
import io.javalin.core.security.Role
import io.javalin.http.Context
import io.javalin.http.Handler

class AccessManagerImpl : AccessManager {

    override fun manage(handler: Handler, ctx: Context, permittedRoles: MutableSet<Role>) {
        permittedRoles.forEach { role ->
            role as AccessRole

            if (role == AccessRole.ANYONE || role.couldComplete(ctx)) {
                handler.handle(ctx)
                return
            }
        }

        ctx.status(401).result("No permission for this route.")
    }

}