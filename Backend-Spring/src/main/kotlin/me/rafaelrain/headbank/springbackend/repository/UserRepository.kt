package me.rafaelrain.headbank.springbackend.repository

import me.rafaelrain.headbank.springbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>