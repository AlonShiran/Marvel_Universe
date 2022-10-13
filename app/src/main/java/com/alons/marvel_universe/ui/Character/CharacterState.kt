package com.alons.marvel_universe.ui.Character

import com.alons.marvel_universe.domain.model.CharacterModel
//data class with all states of Character
data class CharacterState(
    val isLoading : Boolean = false,
    val characterDetail : List<CharacterModel> = emptyList(),
    val error : String = ""
)