package com.alons.marvel_universe.domain.use_cases


import com.alons.marvel_universe.domain.model.CharacterModel
import com.alons.marvel_universe.domain.repository.MarvelRepository
import com.alons.marvel_universe.util.States
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchCharacterCase @Inject constructor(
    private val repository : MarvelRepository
) {
    operator fun invoke(search:String): Flow<States<List<CharacterModel>>> = flow {
        try {
            emit(States.Loading())
            val list = repository.getAllSearchedCharacters(search).data.results.map {
                it.toCharacter()
            }
            emit(States.Success(list))
        }
        catch (e: HttpException){
            emit(States.Error(e.printStackTrace().toString()))
        }
        catch (e: IOException){
            emit(States.Error(e.printStackTrace().toString()))
        }
    }
}