package com.alons.marvel_universe.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alons.marvel_universe.databinding.ActivityRegisterBinding
import com.alons.marvel_universe.databinding.SettingsActivityBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding
    private lateinit var user: FirebaseAuth
    private lateinit var userName: String
      lateinit var arb : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        arb = ActivityRegisterBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        userName = arb.etName.toString()
        setContentView(binding.root)
        if (user.currentUser != null) {
            user.currentUser?.let { binding.userName.text = it.displayName }
        }
        binding.btnSignOut.setOnClickListener {
            user.signOut()
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
            finish()
        }
    }

}
