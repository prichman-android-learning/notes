package io.github.prichman.notes.ui


import androidx.recyclerview.widget.RecyclerView

import android.content.Context
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import io.github.prichman.notes.domain.model.Note
import io.github.prichman.notes.R



typealias ItemSelectionHandler = (Int) -> Unit



class NotesAdapter(val context: Context) : RecyclerView.Adapter<NotesAdapter.ViewHolder>()  {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val titleView = v.findViewById<TextView>(R.id.noteItemTitle)
        private val bodyView  = v.findViewById<TextView>(R.id.noteItemBody)
        private val dateView  = v.findViewById<TextView>(R.id.noteItemDate)

        fun setNote(note: Note) {
            titleView.text = note.title
            bodyView.text  = note.body
            dateView.text  = note.date.toString()
        }
    }

    private val notes = ArrayList<Note>()

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private lateinit var itemSelectionHandler: ItemSelectionHandler

    fun setNotes(notes: List<Note>) {
        for (note: Note in notes.reversed()) {
            if (note !in this.notes) {
                this.notes.add(note)
            } else {
                // All other notes are already added
                break;
            }
        }
        notifyDataSetChanged()
    }

    fun registerItemSelectionHandler(f: ItemSelectionHandler) {
        itemSelectionHandler = f
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.setNote(notes[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.note_item_layout, parent, false)
        val vh = ViewHolder(view)
        view.setOnClickListener {
            itemSelectionHandler(vh.adapterPosition)
        }
        return vh
    }
}