package com.lyvetech.peanut.db

import androidx.room.Database
import androidx.room.TypeConverters

@Database(
    entities = [Note::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class NoteDatabase {
    abstract fun getNoteDao(): NoteDao
}