package io.github.prichman.notes

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.android.material.floatingactionbutton.FloatingActionButton

import io.github.prichman.notes.domain.model.NotesKeeper
import io.github.prichman.notes.ui.NotesAdapter
import io.github.prichman.notes.ui.NoteActivity
import io.github.prichman.notes.ui.NotesViewModel



class MainActivity : AppCompatActivity() {

    private val NOTE_INFO_CODE =  1
    private val NEW_ITEM       = -1

    private var BOTTOM_MENU_CANCEL_ID = 0
    private var BOTTOM_MENU_DELETE_ID = 0

    private lateinit var bottomBar: View
    private lateinit var toolbar: Toolbar
    private lateinit var bottomView: BottomNavigationView
    private lateinit var addButton: FloatingActionButton

    private lateinit var notesView: RecyclerView
    private lateinit var notesViewModel: NotesViewModel

    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup contants
        BOTTOM_MENU_CANCEL_ID = R.id.action_cancel_selection
        BOTTOM_MENU_DELETE_ID = R.id.action_delete_selected

        // Setup RecyclerView
        notesAdapter = NotesAdapter(this)
        notesAdapter.registerItemSelectionHandler(::addNote)
        notesAdapter.registerLongClickListener(::setCheckBoxesVisible)

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

        addButton = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            addNote()
        }
        bottomView = findViewById(R.id.bottomMenuView)
        bottomView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                BOTTOM_MENU_CANCEL_ID -> onCancelSelectionButtonClicked()
                BOTTOM_MENU_DELETE_ID -> onDeleteButtonClicked()
            }
            true
        }
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        bottomBar = findViewById(R.id.bottomSelectedLayout)
        bottomBar.visibility = View.INVISIBLE
    }

    private fun addNote(position: Int = NEW_ITEM) {
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

    private fun setCheckBoxesVisible(): Boolean {
        if (notesAdapter.isSelectionMode)
            return false

        for (i: Int in 0 until notesAdapter.itemCount) {
            val vh = notesView.findViewHolderForAdapterPosition(i) as? NotesAdapter.ViewHolder
            vh?.setSelectionMode(true)
        }
        addButton.hide()
        bottomBar.visibility = View.VISIBLE
        return true
    }

    private fun setCheckBoxesInvisible() {
        for (i: Int in 0 until notesAdapter.itemCount) {
            val vh = notesView.findViewHolderForAdapterPosition(i) as? NotesAdapter.ViewHolder
            vh?.setSelectionMode(false)
        }
        bottomBar.visibility = View.INVISIBLE
        addButton.show()
    }


    private fun onCancelSelectionButtonClicked() {
        notesAdapter.cancelSelection()
        setCheckBoxesInvisible()
        addButton.show()
    }

    private fun onDeleteButtonClicked() {
        val removed = notesAdapter.removeSelectedItems()
        onCancelSelectionButtonClicked()

        notesViewModel.removeNotes(removed)
    }

    private fun handleNote(title: String, body: String, position: Int) {
        Log.d("qwe", "Title: $title, body: $body")
        if (position == NEW_ITEM) {
            notesViewModel.addNote(title, body)
        } else {
            notesViewModel.editNote(title, body, position)
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }
}
