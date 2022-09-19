package com.alons.marvel_universe.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alons.marvel_universe.R
import com.alons.marvel_universe.databinding.ActivityFavoritesBinding
import com.alons.marvel_universe.domain.model.CharacterModel
import com.alons.marvel_universe.util.FavoritesListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database
    private lateinit var myRef: DatabaseReference
    private lateinit var adapter: FavoritesListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var header : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        header = binding.favoriteTitle
        myRef = database.reference.child("users").child(auth.currentUser?.uid!!).child("favorites")
        auth = FirebaseAuth.getInstance()
        bottomNav = binding.bottomNavigation
        layoutManager = GridLayoutManager(this, 2)
        recyclerView = binding.favoriteRV
        adapter = FavoritesListAdapter(this@FavoritesActivity, ArrayList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        getFavorites()
        bottomNav.setOnItemSelectedListener { item ->
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
        setContentView(binding.root)
    }

    private fun getFavorites() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<CharacterModel>()
                for (data in snapshot.children) {
                    val favoriteCharacterModel: CharacterModel =
                        data.getValue(CharacterModel::class.java)!!
                    list.add(favoriteCharacterModel)
                    adapter = FavoritesListAdapter(this@FavoritesActivity, list)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = layoutManager
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }
        })
    }
}








