package me.kzv.refactoring.chapter01

data class Invoice(
    val customer: String,
    val performances: List<Performance>
)

data class Performance(
    val playID: String,
    val audience: Int
)

data class Play(
    val name: String,
    val type: String
)

fun main() {

}
