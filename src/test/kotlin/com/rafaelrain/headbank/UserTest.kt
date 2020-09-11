package com.rafaelrain.headbank

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.responseObject
import com.rafaelrain.headbank.model.Gender
import com.rafaelrain.headbank.model.User
import com.rafaelrain.headbank.util.inject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("User Tests")
class UserTest {

    private lateinit var application: Application

    @BeforeAll
    fun setup() {
        Application.start()
        application = inject<Application>().value

        FuelManager.instance.basePath = "http://localhost:3333"
    }

    @AfterAll
    fun stop() {
        application.stop()
    }

    @Test
    fun `should not return no one error when creating the user`() {
        assertDoesNotThrow {
            "/users"
                .httpPost()
                .jsonBody(
                    """ 
                    {
                        "name": "cornao",
                        "gender" : "FEMALE",
                        "money": "500"
                    }
                """.trimIndent()
                ).response()
        }
    }

    @Test
    fun `should return the user without errors`() {
        val (_, _, result) = "/users/cornao".httpGet().responseObject<User>()

        assertEquals(
            User("cornao", Gender.FEMALE, 500),
            result.get()
        )
    }

    @Test
    fun `should get error for non-existing user`() {
        val (_, _, result) = "/users/kakakaka".httpGet().responseObject<User>()

        val (_, error) = result

        assertEquals(404, error!!.response.statusCode)
    }

    @Test
    fun `should get no error when deleting a user`() {
        assertDoesNotThrow {
            "/users/cornao".httpDelete().response()
        }
    }
}