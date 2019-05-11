package io.github.prichman.notes.common

data class Date(
    val day: Int,
    val month: Int,
    val year: Int
) {
    override fun toString(): String {
        return "$day/$month/$year"
    }
}
