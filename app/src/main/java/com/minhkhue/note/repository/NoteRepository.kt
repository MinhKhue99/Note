package com.minhkhue.note.repository

import com.minhkhue.note.database.NoteDatabase
import com.minhkhue.note.model.Note

class NoteRepository(private var db: NoteDatabase) {
	fun insertNote(note: Note) = db.getNoteDao().insertNote(note)
	fun updateNote(note: Note) = db.getNoteDao().updateNote(note)
	fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)
	fun getAllNotes() = db.getNoteDao().getAllNotes()
	fun searchNote(query: String) = db.getNoteDao().searchNote(query)
}