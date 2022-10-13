package com.alons.marvel_universe.ui.Character

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alons.marvel_universe.R
import com.alons.marvel_universe.databinding.ActivityCharacterBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharacterActivity : AppCompatActivity() {
    private val viewModelCharacter: CharacterViewModel by viewModels()
    var id: Int = 0
    private lateinit var binding: ActivityCharacterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding = ActivityCharacterBinding.inflate(layoutInflater)
        //title
        val supportAC = supportActionBar
        supportAC?.title = "Character Information"
        supportAC?.setDisplayHomeAsUpEnabled(true)
        //getting the info of the character that was selected by user
        if (intent != null) {
            id = intent.getIntExtra("id", 0)
            viewModelCharacter.getCharacterByIdValue(id.toString())
            CoroutineScope(Dispatchers.Main).launch {
                viewModelCharacter._characterValue.collect {
                    when {
                        it.isLoading -> {
                            binding.progressBar2.visibility = View.VISIBLE
                        }
                        it.error.isNotBlank() -> {
                            binding.progressBar2.visibility = View.GONE
                            Toast.makeText(
                                this@CharacterActivity,
                                "Unexpected Error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        //if id was found in API storage
                        it.characterDetail.isNotEmpty() -> {
                            binding.progressBar2.visibility = View.GONE
                            //initialize to view binding
                            it.characterDetail.map { character ->
                                val url =
                                    "${character.thumbnail}/landscape_medium.${character.thumbnailExt}"
                                Picasso.get().load(url).placeholder(R.drawable.image1)
                                    .into(binding.appCompatImageView)
                                binding.CharacterName.text = character.name
                                binding.Description.text = character.description.ifBlank { "No Description " }
                                Log.d("description", character.description)
                                binding.Comics.text = character.comics.toString().ifBlank { "No Comics " }
                            }
                        }
                    }
                }
            }
        }
    }
//return to Main page
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }

    }
}