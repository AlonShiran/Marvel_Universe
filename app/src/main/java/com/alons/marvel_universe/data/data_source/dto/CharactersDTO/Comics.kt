package com.alons.marvel_universe.data.data_source.dto.CharactersDTO

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)