package com.alons.marvel_universe.data.repository

import com.alons.marvel_universe.data.data_source.dto.CharacterDTO.CharacterDTO
import com.alons.marvel_universe.data.data_source.dto.CharactersDTO.CharactersDTO
import com.alons.marvel_universe.data.data_source.MarvelApi
import com.alons.marvel_universe.domain.repository.MarvelRepository
import javax.inject.Inject

class MarvelRepositoryImpl @Inject constructor(
    private val api : MarvelApi
) :MarvelRepository{
    override suspend fun getAllCharacters(offset:Int): CharactersDTO {
        return api.getAllCharacters(offset=offset.toString())
    }

    override suspend fun getAllSearchedCharacters(search: String): CharactersDTO {
        return api.getAllSearchedCharacters(search=search)
    }

    override suspend fun getCharacterById(id: String): CharacterDTO {
        return api.getCharacterById(id)
    }
}