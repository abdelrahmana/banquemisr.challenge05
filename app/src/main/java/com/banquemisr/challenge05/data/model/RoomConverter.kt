package com.banquemisr.challenge05.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomConverter {
    @TypeConverter
    fun getGnerList(listOfString: String?): List<Int>? {
        return Gson().fromJson(
            listOfString,
            object : TypeToken<List<Int>?>() {}.type
        )
    }

    @TypeConverter
    fun saveGenrList(listOfString: List<Int>?): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun getProductionList(listOfString: String?): List<ProductionCompany>? {
        return Gson().fromJson(
            listOfString,
            object : TypeToken<List<ProductionCompany>?>() {}.type
        )
    }

    @TypeConverter
    fun saveProductionList(listOfString: List<ProductionCompany>?): String? {
        return Gson().toJson(listOfString)
    }
    @TypeConverter
    fun getProductionCountry(listOfString: String?): List<ProductionCountry>? {
        return Gson().fromJson(
            listOfString,
            object : TypeToken<List<ProductionCountry>?>() {}.type
        )
    }

    @TypeConverter
    fun saveBelongCollection(listOfString: BelongsToCollection?): String? {
        return Gson().toJson(listOfString)
    }
    @TypeConverter
    fun getBelongCollection(listOfString: String?): BelongsToCollection? {
        return Gson().fromJson(
            listOfString,
            object : TypeToken<BelongsToCollection?>() {}.type
        )
    }

    @TypeConverter
    fun saveProductionCountry(listOfString: List<ProductionCountry>?): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun getSpokenLanguage(listOfString: String?): List<SpokenLanguage>? {
        return Gson().fromJson(
            listOfString,
            object : TypeToken<List<SpokenLanguage>?>() {}.type
        )
    }

    @TypeConverter
    fun saveSpokenLangugae(listOfString: List<SpokenLanguage>?): String? {
        return Gson().toJson(listOfString)
    }



}