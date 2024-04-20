package com.banquemisr.challenge05.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

data class MovieResponse(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)
@Entity
 data class Result(
    @ColumnInfo var adult: Boolean? = false,
    @ColumnInfo var backdrop_path: String? =null,
    var genre_ids: List<Int>? = null,
    @PrimaryKey
    var id: Int? = 0,
    @ColumnInfo  var original_language: String? = null,
    @ColumnInfo  var original_title: String?=null,
    @ColumnInfo  var overview: String?= null,
    @ColumnInfo  var popularity: Double?= null,
    @ColumnInfo  var poster_path: String? =null,
    @ColumnInfo  var release_date: String? = null,
    @ColumnInfo var title: String? = null,
    @ColumnInfo var video: Boolean? = null,
    @ColumnInfo var vote_average: Double? = null,
    @ColumnInfo var vote_count: Int? = null,
    @ColumnInfo var belongs_to_collection: BelongsToCollection? =null,
    @ColumnInfo var budget: Int? = null,
    @ColumnInfo var homepage: String? = null,
    @ColumnInfo var imdb_id: String? = null,
    var production_companies: List<ProductionCompany>? =  null,
    var production_countries: List<ProductionCountry>?=null,
    @ColumnInfo var revenue: Int? =null,
    @ColumnInfo   var runtime: Int? =null,
    var spoken_languages: List<SpokenLanguage>? = null,
    @ColumnInfo var status: String? = null,
    @ColumnInfo var tagline: String? = null) {
     constructor() : this(null,null)
}
@Entity
data class Genre (
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo  var name: String?= "All"
){
     constructor():this(0,"")
 }
@Entity
data class BelongsToCollection(
    val backdrop_path: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val poster_path: String
)

@Entity
data class ProductionCompany (
    @PrimaryKey
    var id: Int? = 0,
    @ColumnInfo  var logo_path: String? = "",
    @ColumnInfo  var name: String? ="",
    @ColumnInfo  var origin_country: String? = ""
) {
    constructor():this(0,"","","")
}
@Entity
data class ProductionCountry (
    @PrimaryKey
    var iso_3166_1: String = "0",
    @ColumnInfo  var name: String?= ""
 ){
   constructor():this("")
 }
@Entity
data class SpokenLanguage(
    @ColumnInfo  var english_name: String? = "",
    @PrimaryKey
    var iso_639_1: String ="",
    @ColumnInfo var name: String? ="",
 ){
   constructor():this(null)
}