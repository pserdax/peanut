package com.lyvetech.peanut.repositories

import com.lyvetech.peanut.db.Note
import com.lyvetech.peanut.db.NoteDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    fun getAllNotes() = noteDao.getAllNotes()
}