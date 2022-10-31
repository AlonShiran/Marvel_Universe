package com.alons.marvel_universe.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.alons.marvel_universe.R
import com.alons.marvel_universe.domain.model.CharacterModel
import com.alons.marvel_universe.ui.character.CharacterActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FavoritesListAdapter(
    private val context: Context,
    private var favList: ArrayList<CharacterModel>
) : RecyclerView.Adapter<
        FavoritesListAdapter.FavoritesListViewHolder>() {
    inner class FavoritesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // View Binding
        val characterName: TextView = view.findViewById(R.id.txtFavCharacterName)
        val thumbnail: ImageView = view.findViewById(R.id.imgFavCharacterImage)
        val cardCharacter: LinearLayout = view.findViewById(R.id.FavCharactersLinearLayout)
        val unFavorite: ToggleButton = view.findViewById(R.id.unFavoriteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardrview_favorites, parent, false)
        return FavoritesListViewHolder(view)
    }

    //function for all actions in the specific favorite character
    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: FavoritesListViewHolder, position: Int) {
        val sharedPreferences: SharedPreferences? =
            context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val list = favList[position]
        holder.characterName.text = list.name
        //favorite character image
        val imageUrl = "${list.thumbnail}/portrait_xlarge.${list.thumbnailExt}"
        val listOfImages = listOf(
            R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4,
            R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image5
        )
        //using Glide to show the image of the character
        Glide.with(context).load(imageUrl).placeholder(listOfImages[(0..7).random()])
            .into(holder.thumbnail)
        //Redirect to Character info page
        holder.cardCharacter.setOnClickListener {
            val intent = Intent(context, CharacterActivity::class.java)
            intent.putExtra("id", list.id)
            context.startActivity(intent)
        }
        //remove from favorites button
        holder.unFavorite.isChecked = true
        holder.unFavorite.setOnClickListener {
            val database =
                Firebase.database("https://marveluniverseapp2912-default-rtdb.europe-west1.firebasedatabase.app")
            val auth = FirebaseAuth.getInstance()
            val myRef = database.reference.child("users").child(auth.currentUser?.uid!!)
                .child("favorites")
            myRef.child(list.id.toString()).removeValue()
            editor?.putBoolean(list.name, false)
            editor?.apply()
            Toast.makeText(
                this.context,
                "removed from favorites",
                Toast.LENGTH_SHORT
            ).show()
            favList.removeAt(position)
            notifyItemRemoved(position)


        }
    }

    //count how many favorites in the list
    override fun getItemCount(): Int {
        return favList.size
    }

}