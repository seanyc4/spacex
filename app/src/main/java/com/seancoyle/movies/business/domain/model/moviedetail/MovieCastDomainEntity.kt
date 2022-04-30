package com.seancoyle.movies.business.domain.model.moviedetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieCastDomainEntity(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieCastDomainEntity

        if (cast != other.cast) return false
        if (crew != other.crew) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cast.hashCode()
        result = 31 * result + crew.hashCode()
        result = 31 * result + id
        return result
    }
}

@Parcelize
data class Cast(
    val adult: Boolean,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val order: Int,
    val original_name: String,
    val popularity: Double,
    val profile_path: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cast

        if (adult != other.adult) return false
        if (cast_id != other.cast_id) return false
        if (character != other.character) return false
        if (credit_id != other.credit_id) return false
        if (gender != other.gender) return false
        if (id != other.id) return false
        if (known_for_department != other.known_for_department) return false
        if (name != other.name) return false
        if (order != other.order) return false
        if (original_name != other.original_name) return false
        if (popularity != other.popularity) return false
        if (profile_path != other.profile_path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = adult.hashCode()
        result = 31 * result + cast_id
        result = 31 * result + character.hashCode()
        result = 31 * result + credit_id.hashCode()
        result = 31 * result + gender
        result = 31 * result + id
        result = 31 * result + known_for_department.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + order
        result = 31 * result + original_name.hashCode()
        result = 31 * result + popularity.hashCode()
        result = 31 * result + profile_path.hashCode()
        return result
    }
}

@Parcelize
data class Crew(
    val adult: Boolean,
    val credit_id: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Crew

        if (adult != other.adult) return false
        if (credit_id != other.credit_id) return false
        if (department != other.department) return false
        if (gender != other.gender) return false
        if (id != other.id) return false
        if (job != other.job) return false
        if (known_for_department != other.known_for_department) return false
        if (name != other.name) return false
        if (original_name != other.original_name) return false
        if (popularity != other.popularity) return false
        if (profile_path != other.profile_path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = adult.hashCode()
        result = 31 * result + credit_id.hashCode()
        result = 31 * result + department.hashCode()
        result = 31 * result + gender
        result = 31 * result + id
        result = 31 * result + job.hashCode()
        result = 31 * result + known_for_department.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + original_name.hashCode()
        result = 31 * result + popularity.hashCode()
        result = 31 * result + profile_path.hashCode()
        return result
    }
}