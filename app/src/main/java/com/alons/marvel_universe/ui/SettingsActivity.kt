package com.alons.marvel_universe.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alons.marvel_universe.R
import com.alons.marvel_universe.data.model.LoggedInUser
import com.alons.marvel_universe.databinding.ActivitySettingsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var signOut: Button
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        signOut = binding.btnSignOut
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = database.reference.child("profile")
        loadProfile()
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
        signOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadProfile() {

        val userReference = databaseReference.child(uid)
        userReference.addValueEventListener(object : ValueEventListener {
            var user : LoggedInUser = LoggedInUser(userId = uid)
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(LoggedInUser::class.java)!!
                binding.tvUserEmail.text = auth.currentUser?.email
                binding.tvUserFirstName.text = user.firstName
                binding.tvUserLastName.text = user.lastName


            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())

            }
        })
    }
}
