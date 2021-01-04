package me.rafaelrain.headbank.springbackend.controller

import me.rafaelrain.headbank.springbackend.model.User
import me.rafaelrain.headbank.springbackend.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val repository: UserRepository) {

    @GetMapping
    fun findAll(): List<User> {
        return repository.findAll()
    }

    @GetMapping(path = ["/{name}"])
    fun findByName(@PathVariable name: String): ResponseEntity<User> {
        return repository.findById(name)
            .map { ResponseEntity.ok(it) }
            .orElseGet { ResponseEntity.notFound().build() }
    }

    @PostMapping
    fun createUser(@RequestBody user: User): User {
        return repository.save(user)
    }

    @PutMapping("/{name}")
    fun updateUser(
        @PathVariable name: String,
        @RequestBody user: User
    ): ResponseEntity<User> {
        return repository.findById(name)
            .map {
                it.money = user.money

                val updated = repository.save(it)
                ResponseEntity.ok(updated)
            }.orElseGet { ResponseEntity.notFound().build() }
    }

    @DeleteMapping("/{name}")
    fun deleteUser(@PathVariable name: String): ResponseEntity<User> {
        val user = repository.findByIdOrNull(name) ?: return ResponseEntity.notFound().build()

        repository.delete(user)
        return ResponseEntity.ok().build()
    }
}