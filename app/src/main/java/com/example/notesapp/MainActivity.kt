package com.example.notesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmResults

class MainActivity : AppCompatActivity() {

    private lateinit var addNotes : FloatingActionButton
    private lateinit var notesRv : RecyclerView
    private lateinit var notesList : ArrayList<Notes>
    private lateinit var realm : Realm

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.btnLogout -> {

                val alert = AlertDialog.Builder(this)
                alert.setTitle("Logout requested..")
                    .setMessage("You sure you want to logout?")
                    .setPositiveButton("Yes, Logout!!"){_,_->
                        FirebaseAuth.getInstance().signOut()
//                        findViewById<CheckBox>(R.id.checkbox).isChecked = false
                        startActivity(Intent(this, LoginSignup::class.java))
                    }
                    .create()
                    .show()
                return true
            }
            R.id.btnDark -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                return true
            }
            R.id.btnLight -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                return true
            }
            R.id.btnProfile -> {
                startActivity(Intent(this, Profile::class.java))
                return true
            }
            R.id.btnCloudList -> {
                startActivity(Intent(this, CloudList::class.java))
                return true
            }
            R.id.btnLocalList -> {
//                startActivity(Intent(this, Profile::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()
        addNotes = findViewById(R.id.FloatButton)
        notesRv = findViewById(R.id.notesRv)

        addNotes.setOnClickListener {
            startActivity(Intent(this, AddnotesActivity :: class.java))
            finish()
        }

        notesRv.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        getAllNotes()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllNotes() {

        notesList = ArrayList()
        val results : RealmResults<Notes> = realm.where(Notes::class.java).findAll()
        notesRv.adapter = NotesAdapter(this, results)
        notesRv.adapter!!.notifyDataSetChanged()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}