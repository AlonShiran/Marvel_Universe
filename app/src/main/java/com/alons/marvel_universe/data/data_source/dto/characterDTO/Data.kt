package com.alons.marvel_universe.data.data_source.dto.characterDTO

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
)