package com.alons.marvel_universe.domain.repository
import com.alons.marvel_universe.data.data_source.dto.CharacterDTO.CharacterDTO
import com.alons.marvel_universe.data.data_source.dto.CharactersDTO.CharactersDTO

interface MarvelRepository {
//interface for all API calls methods
    suspend fun getAllCharacters(offset: Int): CharactersDTO
    suspend fun getAllSearchedCharacters(search:String):CharactersDTO
    suspend fun getCharacterById(id:String):CharacterDTO
}