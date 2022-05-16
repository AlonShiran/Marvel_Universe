package com.alons.marvel_universe.domain.use_cases

import com.alons.marvel_universe.domain.model.CharacterModel
import com.alons.marvel_universe.domain.repository.MarvelRepository
import com.alons.marvel_universe.util.States
import com.alons.marvel_universe.util.States.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CharactersUseCase @Inject constructor(
    private val repository : MarvelRepository
) {
    operator fun invoke(offset: Int): Flow<States<List<CharacterModel>>> = flow {
        try {
            emit(States.Loading())
            val list =repository.getAllCharacters(offset = offset).data.results.map {
                it.toCharacter()
            }
            emit(Success(list))
        }
        catch (e:HttpException){
            emit(States.Error(e.printStackTrace().toString()))
        }
        catch (e:IOException){
            emit(States.Error(e.printStackTrace().toString()))
        }
    }
}