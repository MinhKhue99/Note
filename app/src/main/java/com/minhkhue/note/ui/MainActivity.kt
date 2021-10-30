package com.minhkhue.note.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.minhkhue.note.R
import com.minhkhue.note.database.NoteDatabase
import com.minhkhue.note.databinding.ActivityMainBinding
import com.minhkhue.note.repository.NoteRepository
import com.minhkhue.note.viewmodel.NoteViewModel
import com.minhkhue.note.viewmodel.NoteViewModelProviderFactory

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	lateinit var noteViewModel: NoteViewModel
	private lateinit var navController: NavController
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		navController =
			(supportFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment).navController
		setSupportActionBar(binding.toolbar)
		setupViewModel()
	}
	
	private fun setupViewModel() {
		val noteRepository = NoteRepository(NoteDatabase(this))
		val noteViewModelProviderFactory = NoteViewModelProviderFactory(application, noteRepository)
		noteViewModel =
			ViewModelProvider(this, noteViewModelProviderFactory).get(NoteViewModel::class.java)
	}
	
	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp() || super.onSupportNavigateUp()
	}
}