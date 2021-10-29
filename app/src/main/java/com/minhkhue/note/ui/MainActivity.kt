package com.minhkhue.note.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.minhkhue.note.database.NoteDatabase
import com.minhkhue.note.databinding.ActivityMainBinding
import com.minhkhue.note.repository.NoteRepository
import com.minhkhue.note.viewmodel.NoteViewModel
import com.minhkhue.note.viewmodel.NoteViewModelProviderFactory

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	lateinit var noteViewModel: NoteViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		setSupportActionBar(binding.toolbar)
		setupViewModel()
	}
	
	private fun setupViewModel() {
		val noteRepository = NoteRepository(NoteDatabase(this))
		val noteViewModelProviderFactory = NoteViewModelProviderFactory(application, noteRepository)
		noteViewModel =
			ViewModelProvider(this, noteViewModelProviderFactory).get(NoteViewModel::class.java)
	}
}