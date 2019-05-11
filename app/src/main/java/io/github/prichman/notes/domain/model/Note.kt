package io.github.prichman.notes.domain.model

import io.github.prichman.notes.common.Date

data class Note(
    var title: String? = null,
    var body: String? = null,
    var date: Date
) {
    val id: String
    init {
        val unixTime = System.currentTimeMillis().toString().hashCode()
        id = unixTime.toString()
    }
}