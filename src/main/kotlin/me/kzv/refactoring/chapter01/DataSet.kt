package me.kzv.refactoring.chapter01

import kotlinx.serialization.Serializable

@Serializable
data class Invoice(
    val customer: String,
    val performances: List<Performance>
)

@Serializable
data class Performance(
    val playID: String,
    val audience: Int
)

@Serializable
data class Play(
    val name: String,
    val type: String
)
