package com.banquemisr.challenge05.data.model

class SourceModel {
    companion object {
        const val GENRE = "genre/movie/list"
        const val MOVIEDETAILS = "movie/{id}"
        const val MOVIEPOPULARLIST = "movie/popular"
        const val FILTERMOVIES = "discover/movie"
        const val SEARCHKEYWORD = "search/movie"
        const val baseUrlImage = "http://image.tmdb.org/t/p/w500/"
    }
}