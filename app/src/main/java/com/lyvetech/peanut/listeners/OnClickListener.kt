package com.lyvetech.peanut.listeners

import com.lyvetech.peanut.db.Note

interface OnClickListener {
    fun onNoteClicked(note: Note)
    fun onNoteLongClicked(note: Note)
}