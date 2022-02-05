package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginSignup : AppCompatActivity() {

    private var email : TextInputEditText? = null
    private var password : TextInputEditText? = null
    private var login : Button? = null
    private var signuptext : TextView? = null
    private var anonymous : TextView? = null
//    private var checkBox : CheckBox? = null
    private var firebaseAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        supportActionBar?.title = "Welcome Back"
        supportActionBar?.subtitle = "Login to continue..."

        email = findViewById(R.id.emailLog)
        password = findViewById(R.id.passLog)
        login = findViewById(R.id.btnLogin)
        signuptext = findViewById(R.id.tvSign)
        anonymous = findViewById(R.id.tvSignAnonymous)
        firebaseAuth = FirebaseAuth.getInstance()

        login?.setOnClickListener {
            loginUser()
        }

        signuptext?.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

        anonymous?.setOnClickListener {
            anonymousLogin()
        }

    }


    //For anonymous login.
    private fun anonymousLogin() {

        firebaseAuth?.signInAnonymously()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Guest logged in successfully.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    //For remembering the user.
    override fun onStart() {
        super.onStart()
//        checkBox = findViewById(R.id.checkbox)
//        if(checkBox!!.isChecked) {
            val user = FirebaseAuth.getInstance().currentUser
//            checkBox!!.isChecked = true
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
//        } else {
//            Toast.makeText(this, "unchecked", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    //To sign in the user.(Without verification for now.)
    private fun loginUser() {

        val emailText = email?.text.toString().trim()
        val passwordText = password?.text.toString().trim()

        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Login failed!!")
                .setMessage("fill all credentials first.")
                .setPositiveButton("Okay"){_,_-> }
                .create()
                .show()
        } else {
            firebaseAuth?.signInWithEmailAndPassword(emailText, passwordText)?.addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "Logged in successfully.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}