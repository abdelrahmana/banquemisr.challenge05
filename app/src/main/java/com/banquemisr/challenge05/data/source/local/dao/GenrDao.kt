package com.banquemisr.androidtask.data.source.database.dao

import androidx.room.*
import com.banquemisr.challenge05.data.model.Genre
import com.banquemisr.challenge05.data.model.Result

@Dao
interface GenrDao {

    @Query("SELECT * FROM Genre")
    suspend fun getGenrFromLocal(): List<Genre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGen(genr: List<Genre>)
    @Delete
    fun delete(user: Result)
}