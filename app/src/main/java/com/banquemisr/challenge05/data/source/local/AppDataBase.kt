package com.banquemisr.androidtask.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.banquemisr.androidtask.data.source.database.dao.GenrDao
import com.banquemisr.androidtask.data.source.database.dao.MovieDao
import com.banquemisr.challenge05.data.model.Genre
import com.banquemisr.challenge05.data.model.ProductionCompany
import com.banquemisr.challenge05.data.model.ProductionCountry
import com.banquemisr.challenge05.data.model.Result
import com.banquemisr.challenge05.data.model.RoomConverter
import com.banquemisr.challenge05.data.model.SpokenLanguage

@Database(entities = [Result::class, ProductionCompany::class
    , ProductionCountry::class, SpokenLanguage::class, Genre::class], version = 1)
@TypeConverters(RoomConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genrDao() : GenrDao
}