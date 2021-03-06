package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Matcher
import java.util.regex.Pattern

class Signup : AppCompatActivity() {

    private var email : TextInputEditText? = null
    private var password : TextInputEditText? = null
    private var confirmPassword : TextInputEditText? = null
    private var name : TextInputEditText? = null
    private var phone : TextInputEditText? = null
    private var about : TextInputEditText? = null
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
        name = findViewById(R.id.name)
        phone = findViewById(R.id.phone)
        about = findViewById(R.id.About)
        firebaseAuth = FirebaseAuth.getInstance()

        signup?.setOnClickListener {
            signupUser()
        }

    }

    //for password regex.
    private fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"

        /*

        REGEX condition explanation....

        (?=.*[0-9])  This is for that it should have at least a digit.
        (?=.*[a-z])  This is for that it should have at least a lowercase alphabet.
        (?=.*[A-Z])  This is for that it should have at least a uppercase alphabet.
        (?=.*[$specialCharacters])  This is for that it should have at least a special character which are defined above..
        (?=\S+$).{8,20}  This is for that it should have at least 8 and at most 20 characters without any space..

         */

        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{8,20}$"
        pattern = Pattern.compile(passwordRegex)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
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
        val nameText = name?.text.toString().trim()
        val phoneText = phone?.text.toString().trim()
        val aboutText = about?.text.toString().trim()

        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(confirmPasswordText) ||
            TextUtils.isEmpty(nameText) || TextUtils.isEmpty(phoneText) || TextUtils.isEmpty(aboutText)) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Signup failed!!")
                .setMessage("Fill all credentials first.")
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

            if (isValidPassword(passwordText)) {
                firebaseAuth?.createUserWithEmailAndPassword(emailText, passwordText)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            //Storing user's info
                            val map = HashMap<String, Any>()
                            map["Email"] = emailText
                            map["Name"] = nameText
                            map["Phone Number"] = phoneText
                            map["About User"] = aboutText

                            //Updating user's info to realtime database
                            FirebaseDatabase.getInstance().reference.child("Users")
                                .child(firebaseAuth?.currentUser!!.uid).updateChildren(map)
                                .addOnCompleteListener { task1 ->
                                    if (task1.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "Signed up successfully.\nLogin to continue.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(Intent(this, LoginSignup::class.java))
                                    } else {
                                        Toast.makeText(
                                            this,
                                            task1.exception?.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Weak password...\nStrong password must contain lowercase, uppercase alphabet," +
                        " a digit, a special character with no spaces.", Toast.LENGTH_SHORT).show()
            }
        }

    }

}