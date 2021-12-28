package com.minhkhue.note.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.minhkhue.note.model.Note
import com.minhkhue.note.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application, private val noteRepository: NoteRepository) :
	AndroidViewModel(application) {
	
	fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
		noteRepository.insertNote(note)
	}
	
	fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
		noteRepository.updateNote(note)
	}
	
	fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
		noteRepository.deleteNote(note)
	}
	
	fun getAllNote() = noteRepository.getAllNotes()
	
	fun searchNote(query: String) = noteRepository.searchNote(query)
}