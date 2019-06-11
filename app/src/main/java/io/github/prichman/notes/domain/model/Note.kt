package io.github.prichman.notes.domain.model

import io.github.prichman.notes.common.Date

open class Note(
    var title: String? = null,
    var body:  String? = null,
    var date:  Date
) {
    val id: String
    init {
        val unixTime = System.currentTimeMillis().toString().hashCode()
        id = unixTime.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false

        val otherNote = other as? Note ?: return false
        return id == otherNote.id
    }
}