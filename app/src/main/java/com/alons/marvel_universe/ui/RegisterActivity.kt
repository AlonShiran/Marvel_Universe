package com.alons.marvel_universe.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alons.marvel_universe.R
import com.alons.marvel_universe.data.model.LoggedInUser
import com.alons.marvel_universe.databinding.ActivityRegisterBinding
import com.alons.marvel_universe.extensions.Extensions.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var etEmail: EditText
    private lateinit var etConfPass: EditText
    private lateinit var etPassword: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var tvRedirectLogin: TextView
    private lateinit var btnSignUp: Button
    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // View Bindings
        etEmail = findViewById(R.id.etEmail)
        etLastName = findViewById(R.id.etLastName)
        etFirstName = findViewById(R.id.etFirstName)
        etConfPass = findViewById(R.id.etConfPassword)
        etPassword = findViewById(R.id.etPassword)
        btnSignUp = findViewById(R.id.btnSSigned)
        tvRedirectLogin = findViewById(R.id.tvRedirectLogin)
        // Initializing auth object
        auth = Firebase.auth
        database = Firebase.database("https://marveluniverseapp2912-default-rtdb.europe-west1.firebasedatabase.app/").reference
        btnSignUp.setOnClickListener {
            signUpUser()
        }
        //login Button
        tvRedirectLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
    //sign up method
    @SuppressLint("SuspiciousIndentation")
    private fun signUpUser() {
        val email = etEmail.text.toString()
        val pass = etPassword.text.toString()
        val confirmPassword = etConfPass.text.toString()
        val firstname = etFirstName.text.toString()
        val lastname = etLastName.text.toString()

        // check if user didn't fill all rows
        if (firstname.isBlank()||lastname.isBlank() || email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }
        //creates user if password and email are OK
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val currentUser = LoggedInUser(
                    uid = FirebaseAuth.getInstance().uid.toString(),
                    firstname,
                    lastname
                )
                val currentUSerDb = database.child("users").child(currentUser.uid)
                    currentUSerDb.setValue(currentUser)
                        toast("created account successfully !")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
            } else {
                Toast.makeText(this, "Sign In Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}