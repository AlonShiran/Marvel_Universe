package com.alons.marvel_universe.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.alons.marvel_universe.R
import com.alons.marvel_universe.data.model.LoggedInUser
import com.alons.marvel_universe.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    // create Firebase objects
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var database: FirebaseDatabase = Firebase.database("https://marveluniverseapp2912-default-rtdb.europe-west1.firebasedatabase.app/")

    //create View Binding
    private lateinit var signOut: Button
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        //initializing firebase objects
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = database.reference.child("users")
        //load user profile info from firebase
        loadProfile()
        //bottom nav bar
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.favorite -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                }
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
            }
            false
        }
        //sign out button
        signOut = binding.btnSignOut
        signOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
            finish()
        }
    }
//loading profile from firebase method
    @SuppressLint("SuspiciousIndentation")
    private fun loadProfile() {

        val userReference = databaseReference.child(uid)
        userReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(LoggedInUser::class.java)
                binding.tvUserEmail.text = " Email : " + auth.currentUser!!.email
                binding.tvUserFirstName.text = " First Name : " + user?.firstname
                binding.tvUserLastName.text = " Last Name : " + user?.lastname


            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())

            }
        })
    }
}
