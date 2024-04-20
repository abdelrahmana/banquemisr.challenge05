package com.banquemisr.androidtask.data.source.remote.repository

import com.banquemisr.androidtask.data.source.database.AppDataBase
import com.banquemisr.challenge05.data.model.Genre
import com.banquemisr.challenge05.data.model.MovieResponse
import com.banquemisr.challenge05.data.model.Result
import com.banquemisr.challenge05.data.model.genertype.GenRes

interface MovieRepository {
   /* suspend fun insertUserInfoToDataBase(userData: UserData,completion: (String?, String?) -> Unit)
    suspend fun getUsersInfo(completion: (List<UserData?>?, String?) -> Unit)*/
   suspend fun getMovieFromLocal(genreId : Int,completion: (List<Result>?, String?) -> Unit)
    suspend fun getMovieByKeyWordLocale(keyWord : String,completion: (List<Result>?, String?) -> Unit)
    suspend fun getGenreFromLocal(completion: (List<Genre>?, String?) -> Unit)
    suspend fun getMovieByKeyWord(hashMap: HashMap<String,Any>,completion: (MovieResponse?, String?) -> Unit)
    suspend fun getMovieDetailsFromLocal(movieId : Int,completion: (Result?, String?) -> Unit)

    suspend fun getMovieList(queryMap: HashMap<String,Any>,completion: (MovieResponse?, String?) -> Unit)
    suspend fun getGenreTypes(completion: (List<Genre>?, String?) -> Unit)

    suspend fun getMovieDetails(movieId : Int,completion: (Result?, String?) -> Unit)
    suspend fun getFilteredMovies(queryMap: HashMap<String, Any>,completion: (MovieResponse?, String?) -> Unit)

}