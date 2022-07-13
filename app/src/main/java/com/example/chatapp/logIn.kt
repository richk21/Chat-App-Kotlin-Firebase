package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class logIn : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        mAuth = FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.emailId)
        edtPassword = findViewById(R.id.password)
        btnLogin = findViewById(R.id.loginButton)
        btnSignUp = findViewById(R.id.SignUpButton)

        if(mAuth.currentUser !=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, signUp::class.java)
            startActivity(intent)

        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()
            login(email, password)
        }

    }

    private fun login(email: String, password: String){
        //logic for logging in a user

        if((email.isBlank())or(password.isBlank())){
            Toast.makeText(this@logIn, "Empty field", Toast.LENGTH_SHORT).show()
        }else {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val intent = Intent(this@logIn, MainActivity::class.java)
                        finish()
                        startActivity(intent)

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this@logIn, "User doesn't exist", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}