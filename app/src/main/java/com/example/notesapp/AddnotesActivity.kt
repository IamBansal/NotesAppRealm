package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import java.text.SimpleDateFormat
import java.util.*

class AddnotesActivity : AppCompatActivity() {

    private lateinit var titleED : EditText
    private lateinit var descriptionEd : EditText
    private lateinit var save : Button
    private lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnotes)

        supportActionBar?.title = "Add Note"
        supportActionBar?.subtitle = "Add any Note you want here."

        titleED = findViewById(R.id.etText)
        descriptionEd = findViewById(R.id.etDesc)
        save = findViewById(R.id.btnSave)
        realm = Realm.getDefaultInstance()

        save.setOnClickListener {
            if(TextUtils.isEmpty(titleED.text) && TextUtils.isEmpty(descriptionEd.text)) {
             val alert = AlertDialog.Builder(this)
                 .setTitle("Unexpected :(")
                 .setMessage("Can't add empty note.")
                 .setPositiveButton("Okay Sorry!!"){_,_-> }
                 .show()
            } else {
                addNotesToDB()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun addNotesToDB() {

        try {

            realm.beginTransaction()
            val currentID : Number? = realm.where(Notes::class.java).max("id")

            val nextId : Int = if (currentID == null) {
                1
            } else {
                currentID.toInt() + 1
            }

            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val formattedDate = formatter.format(date)

            val notes = Notes(nextId, titleED.text.toString(), descriptionEd.text.toString(), formattedDate)

            realm.copyToRealmOrUpdate(notes)
            realm.commitTransaction()

            Toast.makeText(this, "Notes Added Successfully.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } catch (e : Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }
}