package com.alons.marvel_universe.ui.CharacterList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alons.marvel_universe.domain.use_cases.CharactersUseCase
import com.alons.marvel_universe.domain.use_cases.SearchCharacterCase

class CharactersViewModelFactory(private val charactersUseCase: CharactersUseCase,
                       private val searchCharacterCase: SearchCharacterCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CharactersViewModel::class.java).newInstance(charactersUseCase,searchCharacterCase)
    }
}