package com.minhkhue.note.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.minhkhue.note.database.dao.NoteDao
import com.minhkhue.note.model.Note

@Database(
	entities = [Note::class],
	version = 1
)
abstract class NoteDatabase : RoomDatabase() {
	abstract fun getNoteDao(): NoteDao
	
	companion object {
		@Volatile
		private var instance: NoteDatabase? = null
		private var LOCK = Any()
		operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
			instance ?: createDatabase(context).also { instance = it }
		}
		
		private fun createDatabase(context: Context) = Room.databaseBuilder(
			context.applicationContext,
			NoteDatabase::class.java,
			"note.db"
		).build()
	}
}