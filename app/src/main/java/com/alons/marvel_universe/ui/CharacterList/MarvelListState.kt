package com.alons.marvel_universe.ui.CharacterList

import com.alons.marvel_universe.domain.model.CharacterModel

data class MarvelListState(

    val isLoading : Boolean = false,
    val characterList: List<CharacterModel> = emptyList(),
    val error : String = ""

)