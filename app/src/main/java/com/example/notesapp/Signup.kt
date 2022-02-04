package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {

    private var email : TextInputEditText? = null
    private var password : TextInputEditText? = null
    private var confirmPassword : TextInputEditText? = null
    private var signup : Button? = null
    private var firebaseAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.title = "New User?"
        supportActionBar?.subtitle = "Sign Up Now..."

        email = findViewById(R.id.emailSign)
        password = findViewById(R.id.passSign)
        confirmPassword = findViewById(R.id.passConfirmSign)
        signup = findViewById(R.id.btnSign)
        firebaseAuth = FirebaseAuth.getInstance()

        signup?.setOnClickListener {
            signupUser()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LoginSignup::class.java))
    }

    //To sign up the user (Without verification for now.)
    private fun signupUser() {

        val emailText = email?.text.toString().trim()
        val passwordText = password?.text.toString().trim()
        val confirmPasswordText = confirmPassword?.text.toString().trim()

        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(confirmPasswordText)) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Signup failed!!")
                .setMessage("fill all credentials first.")
                .setPositiveButton("Okay"){_,_-> }
                .create()
                .show()
        } else if (passwordText != confirmPasswordText) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Signup failed!!")
                .setMessage("password didn't matched.")
                .setPositiveButton("Okay"){_,_-> }
                .create()
                .show()
        } else {
            firebaseAuth?.createUserWithEmailAndPassword(emailText, passwordText)?.addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "Signed up successfully.\nLogin to continue.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginSignup::class.java))
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}