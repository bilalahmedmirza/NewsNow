package com.bilalmirza.newsnow.db

import androidx.room.TypeConverter
import com.bilalmirza.newsnow.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toString(name: String): Source {
        return Source(name, name)
    }
}