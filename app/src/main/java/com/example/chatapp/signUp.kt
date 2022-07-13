package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signUp : AppCompatActivity() {

    private lateinit var edtName : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnBack : Button
    private lateinit var mDbRef : DatabaseReference

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        edtName = findViewById(R.id.name)
        edtEmail = findViewById(R.id.emailIdSignUp)
        edtPassword = findViewById(R.id.passwordSignUp)
        btnSignUp = findViewById(R.id.SignUpButtonSignUpPage)
        btnBack = findViewById(R.id.back)
        mAuth = FirebaseAuth.getInstance()

        btnBack.setOnClickListener{
            val intent = Intent(this,logIn::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener{
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()
            val name = edtName.text.toString()
            signup(email, password, name)
        }

    }

    private fun signup(email: String, password: String, name:String){
        //logic for creating a user
        if((email.isEmpty())or(password.isEmpty())or(name.isEmpty())){
            Toast.makeText(this@signUp, "Empty field", Toast.LENGTH_SHORT).show()
        }else{
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                        val intent = Intent(this@signUp, MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this@signUp, "Try again.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun addUserToDatabase(name:String, email: String, uid:String){
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val user_no = (0..10).random()
        mDbRef.child("User").child(uid).setValue(User(name, email, uid, user_no))
    }

}