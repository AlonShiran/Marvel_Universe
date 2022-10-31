package com.alons.marvel_universe.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alons.marvel_universe.R
import com.alons.marvel_universe.data.model.LoggedInUser
import com.alons.marvel_universe.databinding.FragmentSettingsBinding
import com.alons.marvel_universe.ui.activities.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    // create Firebase objects
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var database: FirebaseDatabase =
        Firebase.database("https://marveluniverseapp2912-default-rtdb.europe-west1.firebasedatabase.app/")

    //create View Binding
    private lateinit var signOut: Button
    private var _binding: FragmentSettingsBinding? = null
    private val binding by lazy { _binding!! }
    private lateinit var uid: String
    override fun onAttach(context: Context) {
        super.onAttach(context)
        //initializing firebase objects
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = database.reference.child("users")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        signOut = binding.btnSignOut
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //load user profile info from firebase
        loadProfile()
        signOut.setOnClickListener {
            signOut()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //sign out method
    private fun signOut() {
        Toast.makeText(
            requireContext(),
            "Good Bye ${auth.currentUser!!.email}",
            Toast.LENGTH_SHORT
        ).show()
        auth.signOut()
        activity?.finish()
        startActivity(Intent(requireContext(), RegisterActivity::class.java))

    }

    //loading profile from firebase method
    @SuppressLint("SuspiciousIndentation")
    private fun loadProfile() {

        val userReference = databaseReference.child(uid)
        userReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(LoggedInUser::class.java)
                binding.tvUserEmail.text = " Email : " + auth.currentUser!!.email.toString()
                binding.tvUserFirstName.text = " First Name : " + user?.firstname
                binding.tvUserLastName.text = " Last Name : " + user?.lastname
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())

            }
        })
    }
}
