package com.lyvetech.peanut.di

import android.content.Context
import androidx.room.Room
import com.lyvetech.peanut.db.NoteDatabase
import com.lyvetech.peanut.utils.Constants.Companion.NOTE_DB_NAME
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

object AppModule {

    @Singleton
    @Provides
    fun providesNoteDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        NoteDatabase::class.java,
        NOTE_DB_NAME
    ).build()

    @Singleton
    @Provides
    fun providesNoteDao(db: NoteDatabase) = db.getNoteDao()
}