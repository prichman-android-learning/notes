package io.github.prichman.notes.domain.model

import io.github.prichman.notes.common.Date

class SelectableNote(note: Note) {

    var isSelected: Boolean = false
    val note: Note = note
}