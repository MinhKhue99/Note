package com.minhkhue.note.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.minhkhue.note.model.Note

@Dao
interface NoteDao {
	
	@Insert
	suspend fun insertNote(note: Note)
	
	@Update
	suspend fun updateNote(note: Note)
	
	@Delete
	suspend fun deleteNote(note: Note)
	
	@Query("SELECT * FROM Note ORDER BY id DESC")
	fun getAllNotes(): LiveData<List<Note>>
	
	@Query("SELECT * FROM Note WHERE noteTitle LIKE :query OR noteBody LIKE :query")
	fun searchNote(query: String): LiveData<List<Note>>
	
}