package com.banquemisr.androidtask.data.source.database.dao

import androidx.room.*
import com.banquemisr.challenge05.data.model.Genre
import com.banquemisr.challenge05.data.model.Result

@Dao
interface MovieDao {
    @Query("SELECT * FROM Result")
   suspend fun getAll(): List<Result>

    @Query("SELECT * FROM Result WHERE id = :movieId LIMIT 1")
    suspend fun getMovieDetails(movieId : Int): Result
    @Query("SELECT * FROM Result Where title LIKE '%' || :keyWord || '%'")
    suspend fun getFilterdMoviesByKeyWord(keyWord : String): List<Result>
@Insert(onConflict = OnConflictStrategy.REPLACE)
fun insertMovie(user: Result)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(user: List<Result>)
    @Delete
    fun delete(user: Result)
}