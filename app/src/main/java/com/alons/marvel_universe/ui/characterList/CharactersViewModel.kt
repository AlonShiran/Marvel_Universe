package com.alons.marvel_universe.ui.characterList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alons.marvel_universe.domain.use_cases.CharactersUseCase
import com.alons.marvel_universe.domain.use_cases.SearchCharacterCase
import com.alons.marvel_universe.util.States
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersUseCase: CharactersUseCase,
    private val searchCharacterCase: SearchCharacterCase
) : ViewModel() {
    private val marvelValue = MutableStateFlow(MarvelListState())
    var _marvelValue: StateFlow<MarvelListState> = marvelValue

    //function to get all searched characters from API
    fun getSearchedCharacters(search: String) = viewModelScope.launch(Dispatchers.IO) {
        searchCharacterCase.invoke(search = search).collect {
            when (it) {
                is States.Success -> {
                    marvelValue.value = MarvelListState(characterList = it.data ?: emptyList())
                    Log.d("toCharacter", _marvelValue.value.toString())
                }
                is States.Loading -> {
                    marvelValue.value = MarvelListState(isLoading = true)
                    Log.d("loading", it.data.toString())
                }
                is States.Error -> {
                    marvelValue.value = MarvelListState(error = it.message ?: "An Unexpected Error")
                    Log.d("Error", it.data.toString())
                }
            }
        }
    }

    //function to get all the characters from API
    fun getAllCharactersData(offset: Int) = viewModelScope.launch(Dispatchers.IO) {
        charactersUseCase(offset).collect {
            when (it) {
                is States.Success -> {
                    marvelValue.value = MarvelListState(characterList = it.data ?: emptyList())
                    Log.d("toCharacter", _marvelValue.value.toString())
                }
                is States.Loading -> {
                    marvelValue.value = MarvelListState(isLoading = true)
                    Log.d("loading", it.data.toString())
                }
                is States.Error -> {
                    marvelValue.value = MarvelListState(error = it.message ?: "An Unexpected Error")
                    Log.d("Error", it.data.toString())
                }
            }
        }
    }
}