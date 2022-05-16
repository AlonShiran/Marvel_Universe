package com.alons.marvel_universe.ui.Character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alons.marvel_universe.domain.use_cases.CharacterUseCase
import com.alons.marvel_universe.util.States
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {

    private val characterValue = MutableStateFlow(CharacterState())
    val _characterValue: StateFlow<CharacterState> = characterValue

    fun getCharacterByIdValue(id: String) = viewModelScope.launch {
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