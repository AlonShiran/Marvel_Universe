package com.alons.marvel_universe.data.data_source.dto.CharacterDTO

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXX>,
    val returned: Int
)