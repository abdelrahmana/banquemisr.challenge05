package com.banquemisr.challenge05.data.source.remote

import com.banquemisr.challenge05.data.model.MovieResponse
import com.banquemisr.challenge05.data.model.Result
import com.banquemisr.challenge05.data.model.SourceModel.Companion.FILTERMOVIES
import com.banquemisr.challenge05.data.model.SourceModel.Companion.GENRE
import com.banquemisr.challenge05.data.model.SourceModel.Companion.MOVIEDETAILS
import com.banquemisr.challenge05.data.model.SourceModel.Companion.MOVIEPOPULARLIST
import com.banquemisr.challenge05.data.model.SourceModel.Companion.SEARCHKEYWORD
import com.banquemisr.challenge05.data.model.genertype.GenRes
import com.skydoves.sandwich.ApiResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface EndPoints {
    @GET(MOVIEPOPULARLIST) // movie list
    suspend fun getMovies(
        @QueryMap hashMap: HashMap<String,Any>
    ): ApiResponse<MovieResponse>

    @GET(MOVIEDETAILS) // movie details
    suspend fun getMovieDetails(@Path ("id")movieDetailsId : Int
    ): ApiResponse<Result>

    @GET(GENRE) // movie details
    suspend fun getGenerTypes(
    ): ApiResponse<GenRes>

    @GET(FILTERMOVIES) // filter movie by genre
    suspend fun getFilteredMovies(@QueryMap hashMap: HashMap<String,Any>
    ): ApiResponse<MovieResponse>

    @GET(SEARCHKEYWORD) // search movie
    suspend fun getMovieBySearch(@QueryMap hashMap: HashMap<String,Any>
    ): ApiResponse<MovieResponse>


}