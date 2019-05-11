package io.github.prichman.notes.domain.repository

import io.github.prichman.notes.domain.model.Note

class NotesRepo {

    fun getNotes(): List<Note> {
        return ArrayList<Note>()
    }

    fun saveNotes(notes: List<Note>) {

    }
}
