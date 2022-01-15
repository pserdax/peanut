package com.lyvetech.peanut.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    var title: String = "",
    var desc: String = "",
    var isPinned: Boolean = false,
    var timestamp: Long = 0L
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null;
}