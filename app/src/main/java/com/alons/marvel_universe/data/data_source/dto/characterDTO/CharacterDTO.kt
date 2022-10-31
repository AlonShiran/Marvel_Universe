package com.alons.marvel_universe.data.data_source.dto.characterDTO

data class CharacterDTO(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val `data`: Data,
    val etag: String,
    val status: String
)