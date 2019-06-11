package io.github.prichman.notes.domain.model

/**
 * Basic data srtucture for storing the notes.
 */
class NotesKeeper {

    private var notes = ArrayList<Note>()

    fun add(note: Note) = notes.add(note)

    fun remove(notes: List<Note>) {
        this.notes.removeAll(notes)
        var a = 5
    }

    fun note(index: Int): Note {
        if (index < 0 || index >= notes.size)
            throw ArrayIndexOutOfBoundsException("Notes size: ${notes.size}, index: $index")
        return notes[index]
    }

    fun getAll(): List<Note> = notes

    fun find(text: String): Note? {
        // TODO_2: add smart search
        return null
    }
}