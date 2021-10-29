package com.minhkhue.note.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.minhkhue.note.R
import com.minhkhue.note.adapter.NoteAdapter
import com.minhkhue.note.databinding.FragmentHomeBinding
import com.minhkhue.note.model.Note
import com.minhkhue.note.ui.MainActivity
import com.minhkhue.note.viewmodel.NoteViewModel

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {
	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!
	private lateinit var noteViewModel: NoteViewModel
	private lateinit var noteAdapter: NoteAdapter
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentHomeBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		noteViewModel = (activity as MainActivity).noteViewModel
		setupRecyclerView()
		binding.fabAddNote.setOnClickListener {
			it.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
		}
	}
	
	private fun setupRecyclerView() {
		noteAdapter = NoteAdapter()
		binding.recyclerView.apply {
			adapter = noteAdapter
			layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
			setHasFixedSize(true)
		}
		activity?.let {
			noteViewModel.getAllNote().observe(viewLifecycleOwner,{notes ->
				noteAdapter.differ.submitList(notes)
				updateUI(notes)
			})
		}
	}
	
	private fun updateUI(notes: List<Note>) {
		if (notes.isEmpty()){
			binding.cardView.visibility = View.VISIBLE
			binding.recyclerView.visibility = View.GONE
		}
		else{
			binding.cardView.visibility = View.GONE
			binding.recyclerView.visibility = View.VISIBLE
		}
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		inflater.inflate(R.menu.home_menu, menu)
		val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
		mMenuSearch.isSubmitButtonEnabled = false
		mMenuSearch.setOnQueryTextListener(this)
	}
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	override fun onQueryTextSubmit(query: String?): Boolean {
		/*if (query != null) {
            searchNote(query)
        }*/
		return false
	}
	
	override fun onQueryTextChange(newText: String?): Boolean {
		if (newText != null) {
			searchNote(newText)
		}
		return true
	}
	private fun searchNote(query: String?) {
		val searchQuery = "%$query%"
		noteViewModel.searchNote(searchQuery).observe(
			this, { list ->
				noteAdapter.differ.submitList(list)
			}
		)
	}
	
}