package com.alons.marvel_universe.domain.model

data class CharacterModel(
    val id : Int,
    val name : String,
    val description : String,
    val thumbnail : String,
    val thumbnailExt: String,
    val comics : List<String>,
)