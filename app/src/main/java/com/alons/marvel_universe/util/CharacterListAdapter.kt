package com.alons.marvel_universe.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.alons.marvel_universe.R
import com.alons.marvel_universe.R.drawable.image1
import com.alons.marvel_universe.domain.model.CharacterModel
import com.alons.marvel_universe.ui.Character.CharacterActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CharacterListAdapter(
    private val context: Context,
    private var itemList: ArrayList<CharacterModel>
) : RecyclerView.Adapter<
        CharacterListAdapter.CharacterListViewHolder>() {
    inner class CharacterListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val characterName: TextView = view.findViewById(R.id.txtCharacterName)
        val thumbnail: ImageView = view.findViewById(R.id.imgCharacterImage)
        val cardCharacter: LinearLayout = view.findViewById(R.id.characterLinearLayout)
        val favBtn: ToggleButton = view.findViewById(R.id.FavoriteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_character, parent, false)
        return CharacterListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        val list = itemList[position]
        holder.characterName.text = list.name
        val imageUrl = "${list.thumbnail}/portrait_xlarge.${list.thumbnailExt}"
        val listOfImages = listOf(
            image1, R.drawable.image2, R.drawable.image3, R.drawable.image4,
            R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image5
        )
        Glide.with(context).load(imageUrl).placeholder(listOfImages[(0..7).random()])
            .into(holder.thumbnail)
        holder.favBtn.setOnCheckedChangeListener { _, isChecked ->
            val database = Firebase.database
            val auth = FirebaseAuth.getInstance()
            val myRef =
                database.reference.child("users").child(auth.uid.toString())
            if (isChecked) {
                myRef.setValue(list.copy())
                Toast.makeText(
                    this.context,
                    "added to favorites",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                myRef.child(list.id.toString()).removeValue()
                Toast.makeText(
                    this.context,
                    "removed from favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        holder.cardCharacter.setOnClickListener {
            val intent = Intent(context, CharacterActivity::class.java)
            intent.putExtra("id", list.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(characterList: ArrayList<CharacterModel>) {
        this.itemList.addAll(characterList)
        notifyDataSetChanged()
    }
}