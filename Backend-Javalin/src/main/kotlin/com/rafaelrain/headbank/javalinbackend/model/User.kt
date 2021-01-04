package com.rafaelrain.headbank.javalinbackend.model

data class User(
    val name: String,
    val gender: Gender,
    var money: Int
)

enum class Gender { MALE, FEMALE }