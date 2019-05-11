package io.github.prichman.notes

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.material.floatingactionbutton.FloatingActionButton

import io.github.prichman.notes.domain.model.NotesKeeper
import io.github.prichman.notes.ui.NotesAdapter
import io.github.prichman.notes.ui.NoteActivity
import io.github.prichman.notes.ui.NotesViewModel

class MainActivity : AppCompatActivity() {

    private val NOTE_INFO_CODE =  1
    private val NEW_ITEM       = -1

    private lateinit var notesView: RecyclerView
    private lateinit var notesViewModel: NotesViewModel

    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerView
        notesAdapter = NotesAdapter(this)
        notesAdapter.registerItemSelectionHandler(::addNote)

        notesView = findViewById(R.id.notesView)
        notesView.adapter = notesAdapter
        notesView.layoutManager = LinearLayoutManager(this)

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)

        val notesChangesObserver = Observer<NotesKeeper> {
            it?.let {
                notesAdapter.setNotes(it.getAll())
            }
        }
        notesViewModel.notes.observe(this, notesChangesObserver)

        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener {
            addNote()
        }
    }

    fun addNote(position: Int = NEW_ITEM) {
        val noteIntent = Intent(this, NoteActivity::class.java)
        if (position != NEW_ITEM) {
            val titleExtraName    = getString(R.string.title)
            val bodyExtraName     = getString(R.string.body)
            val positionExtraName = getString(R.string.position_key)

            val note = notesViewModel.notesKeeper.note(position)
            noteIntent.putExtra(titleExtraName, note.title)
            noteIntent.putExtra(bodyExtraName, note.body)
            noteIntent.putExtra(positionExtraName, position)
        }
        startActivityForResult(noteIntent, NOTE_INFO_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NOTE_INFO_CODE) {
            if (resultCode == RESULT_OK) {
                data?.let {
                    val titleExtraName    = getString(R.string.title)
                    val bodyExtraName     = getString(R.string.body)
                    val positionExtraName = getString(R.string.position_key)

                    val title    = it.getStringExtra(titleExtraName)
                    val body     = it.getStringExtra(bodyExtraName)
                    val position = it.getIntExtra(positionExtraName, NEW_ITEM)

                    handleNote(title, body, position)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun handleNote(title: String, body: String, position: Int) {
        Log.d("qwe", "Title: $title, body: $body")
        if (position == NEW_ITEM) {
            notesViewModel.addNote(title, body)
        } else {
            notesViewModel.editNote(title, body, position)
        }
    }
}
