package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import io.realm.Realm
import io.realm.kotlin.delete

class NoteOpen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_open)

        supportActionBar?.title = "Note Expanded."
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_list_alt_24)

        findViewById<TextView>(R.id.tvTitleOpen).text = intent.getStringExtra("title")
        findViewById<TextView>(R.id.tvDescOpen).text = intent.getStringExtra("description")
        findViewById<TextView>(R.id.tvTimeOpen).text = "Added on : ${ intent.getStringExtra("time") }"


    }
}