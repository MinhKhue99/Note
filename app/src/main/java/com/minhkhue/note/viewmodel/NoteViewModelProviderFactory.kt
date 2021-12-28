package com.minhkhue.note.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minhkhue.note.repository.NoteRepository

class NoteViewModelProviderFactory(
	private val application: Application,
	private val noteRepository: NoteRepository
) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return NoteViewModel(application, noteRepository) as T
	}
}