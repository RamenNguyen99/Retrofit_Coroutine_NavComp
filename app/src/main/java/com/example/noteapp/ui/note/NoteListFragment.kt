package com.example.noteapp.ui.note

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.data.entity.Note
import com.example.noteapp.ui.adapter.NoteAdapter
import com.example.noteapp.ui.viewmodel.NoteViewModel
import com.example.noteapp.ultis.Status
import kotlinx.android.synthetic.main.fragment_note_list.*
import kotlinx.coroutines.launch

/**
 * Created by tuong.nguyen2 on 09/09/2022.
 */
class NoteListFragment : Fragment(R.layout.fragment_note_list) {
    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this,
            NoteViewModel.NoteViewModelFactory(requireActivity().application)
        )[NoteViewModel::class.java]
    }

    private val noteAdapter: NoteAdapter by lazy {
        NoteAdapter(requireContext(), onItemClick, onItemDelete)
    }

    private val controller: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setHasOptionsMenu(true)

        rv_note.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = noteAdapter
        }

        refreshData()

        swipe_layout.setOnRefreshListener {
            refreshData()
        }

        btn_open_add_activity.setOnClickListener {
            controller.navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.main_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.mnu_sync) {
//            Toast.makeText(context, "Syncing", Toast.LENGTH_SHORT).show()
//            noteViewModel.run {
//                getNotesFromApi().observe(viewLifecycleOwner) {
//                    it?.let { resource ->
//                        when (resource.status) {
//                            Status.SUCCESS -> {
//                                swipe_layout.isRefreshing = false
//
//                                noteViewModel.deleteAllNoteFromDatabase()
//                                resource.data?.let { notes ->
//                                    noteViewModel.insertAllNoteToDatabase(notes)
//                                }
//                            }
//                            Status.ERROR -> {
//                                swipe_layout.isRefreshing = false
//                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//                            }
//                            Status.LOADING -> {
//                                swipe_layout.isRefreshing = true
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun getDataFromDatabase() {
        noteViewModel.getAllNote().observe(viewLifecycleOwner) {
            Log.i("TAG", "getDataFromDatabase: $it")
            noteAdapter.setNotes(it)
        }
    }

    private fun refreshData() {
        noteViewModel.run {
            getNotesFromApi().observe(viewLifecycleOwner) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            swipe_layout.isRefreshing = false
                            resource.data?.let { notes ->
                                // check cho ni cho khoi loi trung id thoi
                                notes.forEach { note ->
                                    val id = notes.indexOf(note)
                                    note.myId = id
                                }
                                Log.i(
                                    "TAG",
                                    "refreshData: $notes"
                                )
                                // end check
                                noteAdapter.setNotes(notes)
                                // adding data to room database without sync in appbar
                                lifecycleScope.launch {
                                    deleteAllNoteFromDatabase()
                                    insertAllNoteToDatabase(notes)
                                }
                            }
                        }
                        Status.ERROR -> {
                            swipe_layout.isRefreshing = false
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            getDataFromDatabase()
                        }
                        Status.LOADING -> {
                            swipe_layout.isRefreshing = true
                        }
                    }
                }
            }
        }
    }

    private val onItemClick: (Note) -> Unit = { note ->
        /* toi man hinh update, tuy nhien server thiet ke bi trung id
        khi xoa mot phan tu va add phan tu moi nen dan toi CRASH APP */
        val action = NoteListFragmentDirections.actionNoteListFragmentToUpdateNoteFragment(note)
        controller.navigate(action)
    }

    private val onItemDelete: (Note) -> Unit = { note ->
        noteViewModel.deleteNoteFromServer(note.id).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(context, resource.status.toString(), Toast.LENGTH_SHORT)
                            .show()
                        refreshData()
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }
}
