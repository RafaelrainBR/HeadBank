package me.rafaelrain.headbank.springbackend.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(
    @Id val name: String,
    var money: Int
)