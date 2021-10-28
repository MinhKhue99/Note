package com.minhkhue.note.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.minhkhue.note.databinding.NoteItemBinding
import com.minhkhue.note.model.Note
import com.minhkhue.note.ui.fragment.HomeFragmentDirections
import java.util.*

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
	inner class NoteViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root)
	
	private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
		override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
			return oldItem.id == newItem.id &&
					oldItem.noteTitle == newItem.noteTitle &&
					oldItem.noteBody == newItem.noteBody
		}
		
		override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
			return oldItem == newItem
		}
	}
	
	var differ = AsyncListDiffer(this, diffCallback)
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
		return NoteViewHolder(
			NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}
	
	override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
		val note = differ.currentList[position]
		holder.binding.tvNoteTitle.text = note.noteTitle
		holder.binding.tvNoteBody.text = note.noteBody
		val random = Random()
		val color = Color.argb(
			255, random.nextInt(256),
			random.nextInt(256), random.nextInt(256)
		)
		holder.binding.ibColor.setBackgroundColor(color)
		holder.itemView.setOnClickListener {
			val direction = HomeFragmentDirections
				.actionHomeFragmentToUpdateNoteFragment(note)
			it.findNavController().navigate(direction)
		}
	}
	
	override fun getItemCount(): Int {
		return differ.currentList.size
	}
}
