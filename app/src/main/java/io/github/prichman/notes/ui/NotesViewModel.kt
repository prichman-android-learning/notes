package io.github.prichman.notes.ui

import java.util.Calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import io.github.prichman.notes.common.Date
import io.github.prichman.notes.domain.model.Note
import io.github.prichman.notes.domain.model.NotesKeeper
import io.github.prichman.notes.domain.repository.NotesRepo


class NotesViewModel : ViewModel() {

    private val notesRepo = NotesRepo()
    val notesKeeper = NotesKeeper()

    val notes = MutableLiveData<NotesKeeper>()

    fun addNote(title: String, body: String) {
        notesKeeper.add(Note(title, body, getCurrentDate()))
        update()
    }

    fun editNote(title: String, body: String, position: Int) {
        notesKeeper.note(position).apply {
            this.title = title
            this.body = body
        }
        update()
    }

    fun removeNotes(notes: List<Note>) {
        notesKeeper.remove(notes)
        update()
    }

    fun getCurrentDate(): Date {
        val day   = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1
        val year  = Calendar.getInstance().get(Calendar.YEAR)

        return Date(day, month, year)
    }

    private fun update() {
        notes.value = notesKeeper
    }
}