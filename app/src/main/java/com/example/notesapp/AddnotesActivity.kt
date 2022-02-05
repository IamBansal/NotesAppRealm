package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddnotesActivity : AppCompatActivity() {

    private lateinit var titleED : EditText
    private lateinit var descriptionEd : EditText
    private lateinit var save : Button
    private lateinit var saveC : Button
    private lateinit var realm : Realm
    private var firebaseAuth:FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnotes)

        supportActionBar?.title = "Add Note"
        supportActionBar?.subtitle = "Add any Note you want here."

        titleED = findViewById(R.id.etText)
        descriptionEd = findViewById(R.id.etDesc)
        save = findViewById(R.id.btnSave)
        saveC = findViewById(R.id.btnSaveC)
        realm = Realm.getDefaultInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        save.setOnClickListener {
            if(TextUtils.isEmpty(titleED.text) && TextUtils.isEmpty(descriptionEd.text)) {
             val alert = AlertDialog.Builder(this)
                 alert.setTitle("Unexpected :(")
                 .setMessage("Can't add empty note.")
                 .setPositiveButton("Okay Sorry!!"){_,_-> }
                 .show()
            } else {
                addNotesToDB()
            }
        }

        saveC.setOnClickListener {
            if(TextUtils.isEmpty(titleED.text) && TextUtils.isEmpty(descriptionEd.text)) {
             val alert = AlertDialog.Builder(this)
                 alert.setTitle("Unexpected :(")
                 .setMessage("Can't add empty note.")
                 .setPositiveButton("Okay Sorry!!"){_,_-> }
                 .show()
            } else {
                addNotesToCloud()
            }
        }

    }

    private fun addNotesToCloud() {

        val date = Calendar.getInstance().time
        val formattedDate = SimpleDateFormat.getDateTimeInstance().format(date)

        val ref = FirebaseDatabase.getInstance().reference.child("Notes")
        val map = HashMap<String, Any>()
        map["Title"] = titleED.text.toString().trim()
        map["Description"] = descriptionEd.text.toString().trim()
        map["Time"] = formattedDate.toString()

        ref.push().updateChildren(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                addNotesToDB()
            } else {
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
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