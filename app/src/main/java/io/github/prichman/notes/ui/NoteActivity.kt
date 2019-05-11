package io.github.prichman.notes.ui

import io.github.prichman.notes.R

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import android.app.Activity
import android.content.Intent

class NoteActivity : AppCompatActivity() {

    private val NEW_ITEM = -1

    private lateinit var titleView: EditText
    private lateinit var bodyView:  EditText
    private var position = NEW_ITEM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        titleView = findViewById(R.id.title)
        bodyView  = findViewById(R.id.body)

        // Enable up navigation arrow
        val toolbar = findViewById<Toolbar>(R.id.actionBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Set selection cursor at the end of line
        val titleView = findViewById<EditText>(R.id.title)
        titleView.setSelection(titleView.text.length)

        titleView.text.clear()
        bodyView.text.clear()

        val extras = intent.extras
        if (extras == null) {
            titleView.text.append("q")
            bodyView.text.append("ww")
        }
        else {
            titleView.text.append(extras.getString(getString(R.string.title)))
            bodyView.text.append(extras.getString(getString(R.string.body)))
            position = extras.getInt(getString(R.string.position_key))
        }
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra(getString(R.string.title), titleView.text.toString())
        resultIntent.putExtra(getString(R.string.body), bodyView.text.toString())
        resultIntent.putExtra(getString(R.string.position_key), position)
        setResult(Activity.RESULT_OK, resultIntent)

        finish()
    }
}
