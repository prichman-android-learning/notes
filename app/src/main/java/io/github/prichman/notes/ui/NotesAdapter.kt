package io.github.prichman.notes.ui


import androidx.recyclerview.widget.RecyclerView

import android.content.Context
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView

import io.github.prichman.notes.domain.model.Note
import io.github.prichman.notes.domain.model.SelectableNote
import io.github.prichman.notes.R



typealias ItemPickerHandler = (Int) -> Unit
typealias ItemSelectionHandler = () -> Boolean



class NotesAdapter(val context: Context) : RecyclerView.Adapter<NotesAdapter.ViewHolder>()  {

    private val notes = ArrayList<SelectableNote>()

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var isSelectionMode: Boolean = false
        private set

    private lateinit var itemPickerHandler: ItemPickerHandler
    private lateinit var itemSelectionHandler: ItemSelectionHandler

    fun setNotes(notes: List<Note>) {
        this.notes.clear()
        loop@ for (note: Note in notes.reversed()) {
            for (selectableNote in this.notes) {
                if (selectableNote.note == note) {
                    break@loop
                }
            }
            this.notes.add(SelectableNote(note))
        }
        notifyDataSetChanged()
    }

    fun registerItemSelectionHandler(f: ItemPickerHandler) {
        itemPickerHandler = f
    }

    fun registerLongClickListener(f: ItemSelectionHandler) {
        itemSelectionHandler = f
    }

    fun removeSelectedItems(): List<Note> {
        val selectedSelItems = notes.filter { it.isSelected }
        val selectedNotes = ArrayList<Note>()
        for (selectableNote in selectedSelItems) {
            selectedNotes.add(selectableNote.note)
        }
        cancelSelection()
        return selectedNotes
    }

    fun cancelSelection() {
        isSelectionMode = false
        for (note in notes) {
            note.isSelected = false
        }
    }

    private fun setSelectionMode() {
        isSelectionMode = true
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.setNote(notes[position].note, isSelectionMode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.note_item_layout, parent, false)
        val onCheckClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                view.callOnClick()
            }
        }
        val vh = ViewHolder(view, onCheckClickListener)
        view.setOnClickListener {
            if (isSelectionMode) {
                itemSelectionHandler()
                notes[vh.adapterPosition].isSelected = !notes[vh.adapterPosition].isSelected
                vh.setSelected(notes[vh.adapterPosition].isSelected)
            } else {
                itemPickerHandler(vh.adapterPosition)
            }
        }
        view.setOnLongClickListener {
            if (!isSelectionMode) {
                itemSelectionHandler()
                setSelectionMode()
                // vh.setSelectionMode(true)
                vh.setSelected(notes[vh.adapterPosition].isSelected)
            }

            // Do nothing in Selection mode
            false
        }
        return vh
    }

    class ViewHolder(v: View, checkedClickListener: View.OnClickListener) : RecyclerView.ViewHolder(v) {

        private val titleView  = v.findViewById<TextView>(R.id.noteItemTitle)
        private val bodyView   = v.findViewById<TextView>(R.id.noteItemBody)
        private val dateView   = v.findViewById<TextView>(R.id.noteItemDate)
        private val selectBox  = v.findViewById<CheckBox>(R.id.noteSelectBox)

        init {
            selectBox.setOnClickListener(checkedClickListener)
            selectBox.isChecked = false
            selectBox.visibility = View.INVISIBLE
        }

        fun setNote(note: Note, visible: Boolean) {
            titleView.text = note.title
            bodyView.text  = note.body
            dateView.text  = note.date.toString()
            if (visible) {
                selectBox.visibility = View.VISIBLE
            } else {
                selectBox.visibility = View.INVISIBLE
            }
        }

        fun setSelectionMode(isSelection: Boolean) {
            selectBox.isChecked = false
            if (isSelection) {
                selectBox.visibility = View.VISIBLE
            } else {
                selectBox.visibility = View.INVISIBLE
            }
        }

        fun setSelected(isSelected: Boolean) {
            selectBox.isChecked = isSelected
        }
    }
}
