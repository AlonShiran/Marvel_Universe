package com.alons.marvel_universe.data.data_source.dto.characterDTO

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemX>,
    val returned: Int
)