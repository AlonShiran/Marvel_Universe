package com.alons.marvel_universe.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String="",
    val firstName: String="",
    val lastName: String=""
)