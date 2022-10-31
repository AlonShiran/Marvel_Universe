package com.alons.marvel_universe.ui.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alons.marvel_universe.domain.use_cases.CharacterUseCase
import com.alons.marvel_universe.util.States
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {
    //saving in variable the state
    private val characterValue = MutableStateFlow(CharacterState())
    val _characterValue: StateFlow<CharacterState> = characterValue

    //method to get Character info by his id
    fun getCharacterByIdValue(id: String?) = viewModelScope.launch {
        if (id != null) {
            characterUseCase(id).collect {
                when (it) {
                    is States.Success -> {
                        characterValue.value = CharacterState(
                            characterDetail = it.data ?: emptyList()
                        )
                    }
                    is States.Loading -> {
                        characterValue.value = CharacterState(isLoading = true)
                    }
                    is States.Error -> {
                        characterValue.value =
                            CharacterState(error = it.message ?: "An Unexpected Error")
                    }
                }
            }
        }
    }
}