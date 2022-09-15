package com.example.noteapp.ui.note

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp.R
import com.example.noteapp.ui.viewmodel.NoteViewModel
import com.example.noteapp.ultis.Status
import kotlinx.android.synthetic.main.fragment_add_note.edt_note_des
import kotlinx.android.synthetic.main.fragment_add_note.edt_note_title
import kotlinx.android.synthetic.main.fragment_update_note.*

/**
 * Created by tuong.nguyen2 on 09/09/2022.
 */
class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {
    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this,
            NoteViewModel.NoteViewModelFactory(requireActivity().application)
        )[NoteViewModel::class.java]
    }

    private val controller: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: UpdateNoteFragmentArgs by navArgs()
        val note = args.note

        edt_note_title.setText(note.title)
        edt_note_des.setText(note.description)

        btn_update.setOnClickListener {
            note.title = edt_note_title.text.toString()
            note.description = edt_note_des.text.toString()

            noteViewModel.updateNoteFromServer(note.id, note).observe(viewLifecycleOwner) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { note ->
                                Toast.makeText(
                                    requireContext(),
                                    note.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                                controller.popBackStack()
                            }
                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        Status.LOADING -> {

                        }
                    }
                }
            }
        }
    }
}
