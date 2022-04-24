package com.seancoyle.movies.framework.datasource.network.model.moviedetail

data class MovieCastNetworkEntity(
    val cast: List<CastNetworkEntity?>?,
    val crew: List<CrewNetworkEntity?>?,
    val id: Int?
)

data class CastNetworkEntity(
    val adult: Boolean?,
    val cast_id: Int?,
    val character: String?,
    val credit_id: String?,
    val gender: Int?,
    val id: Int?,
    val known_for_department: String?,
    val name: String?,
    val order: Int?,
    val original_name: String?,
    val popularity: Double?,
    val profile_path: String?
)

data class CrewNetworkEntity(
    val adult: Boolean?,
    val credit_id: String?,
    val department: String?,
    val gender: Int?,
    val id: Int?,
    val job: String?,
    val known_for_department: String?,
    val name: String?,
    val original_name: String?,
    val popularity: Double?,
    val profile_path: String?
)