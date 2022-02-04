package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {

    private var name : TextView? = null
    private var phone : TextView? = null
    private var about : TextView? = null
    private var email : TextView? = null
    private var firebaseAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name = findViewById(R.id.nameP)
        phone = findViewById(R.id.phoneP)
        about = findViewById(R.id.aboutP)
        email = findViewById(R.id.emailP)
        firebaseAuth = FirebaseAuth.getInstance()

        showProfile()

    }

    //For displaying user's info
    private fun showProfile() {
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(firebaseAuth?.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    name?.text = snapshot.child("Name").value.toString()
                    email?.text = snapshot.child("Email").value.toString()
                    phone?.text = snapshot.child("Phone Number").value.toString()
                    about?.text = snapshot.child("About User").value.toString()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

}