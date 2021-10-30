package com.minhkhue.note.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.minhkhue.note.R
import com.minhkhue.note.databinding.FragmentUpdateNoteBinding
import com.minhkhue.note.model.Note
import com.minhkhue.note.ui.MainActivity
import com.minhkhue.note.viewmodel.NoteViewModel

class UpdateNoteFragment : Fragment() {
	private var _binding: FragmentUpdateNoteBinding? = null
	private val binding get() = _binding!!
	private lateinit var noteViewModel: NoteViewModel
	private lateinit var currentNote: Note
	private val args: UpdateNoteFragmentArgs by navArgs()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		noteViewModel = (activity as MainActivity).noteViewModel
		
		currentNote = args.note!!
		binding.etNoteTitleUpdate.setText(currentNote.noteTitle)
		binding.etNoteBodyUpdate.setText(currentNote.noteBody)
		
		if (currentNote.noteTitle.isNotEmpty()) {
			binding.fabDone.setOnClickListener {
				val title = binding.etNoteTitleUpdate.text.toString().trim()
				val body = binding.etNoteBodyUpdate.text.toString().trim()
				val note = Note(currentNote.id, title, body)
				noteViewModel.updateNote(note)
				view.findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)
				Snackbar.make(view, R.string.update_success, Snackbar.LENGTH_SHORT).show()
			}
		} else {
			Snackbar.make(view, R.string.enter_title, Snackbar.LENGTH_SHORT).show()
		}
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		menu.clear()
		inflater.inflate(R.menu.menu_update_note, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.menu_delete -> deleteNote()
		}
		return super.onOptionsItemSelected(item)
	}
	
	private fun deleteNote() {
		AlertDialog.Builder(activity).apply {
			setTitle(R.string.delete_note)
			setMessage(R.string.message_delete_note)
			setPositiveButton(R.string.delete_button) { _, _ ->
				noteViewModel.deleteNote(currentNote)
				view?.findNavController()?.navigate(R.id.action_updateNoteFragment_to_homeFragment)
			}
			setNegativeButton(R.string.cancel_button, null)
		}.create().show()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}