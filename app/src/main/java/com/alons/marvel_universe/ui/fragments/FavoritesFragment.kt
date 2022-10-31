package com.alons.marvel_universe.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alons.marvel_universe.R
import com.alons.marvel_universe.databinding.FragmentFavoritesBinding
import com.alons.marvel_universe.domain.model.CharacterModel
import com.alons.marvel_universe.util.FavoritesListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    // Creating firebaseAuth object
    private lateinit var auth: FirebaseAuth
    private val database =
        Firebase.database("https://marveluniverseapp2912-default-rtdb.europe-west1.firebasedatabase.app")
    private lateinit var myRef: DatabaseReference

    //View Binding
    private lateinit var adapter: FavoritesListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var _binding: FragmentFavoritesBinding? = null
    private val binding by lazy { _binding!! }
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //initializing firebase objects
        auth = FirebaseAuth.getInstance()
        myRef = database.reference.child("users").child(auth.uid!!).child("favorites")
        //initializing view binding
        layoutManager = GridLayoutManager(requireContext(), 2)
        //Recycler View
        recyclerView = binding.favoritesRV
        adapter = FavoritesListAdapter(requireContext(), ArrayList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        getFavorites()
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //loading favorites from FireBase
    private fun getFavorites() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<CharacterModel>()
                for (data in snapshot.children) {
                    val favoriteCharacterModel: CharacterModel =
                        data.getValue(CharacterModel::class.java)!!
                    list.add(favoriteCharacterModel)
                    adapter = FavoritesListAdapter(requireContext(), list)
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








