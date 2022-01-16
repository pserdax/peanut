package com.lyvetech.peanut.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lyvetech.peanut.R
import com.lyvetech.peanut.adapters.NoteAdapter
import com.lyvetech.peanut.databinding.FragmentMainBinding
import com.lyvetech.peanut.db.Note
import com.lyvetech.peanut.listeners.OnClickListener
import com.lyvetech.peanut.ui.viewmodel.MainViewModel
import com.lyvetech.peanut.utils.OnboardingUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@AndroidEntryPoint
class MainFragment : Fragment(), OnClickListener {

    private var _binding: FragmentMainBinding? = null

    private lateinit var bottomSheetDialog: BottomSheetDialog

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bottom sheet layout components
        bottomSheetDialog = context?.let { BottomSheetDialog(it) }!!
        bottomSheetDialog.setContentView(R.layout.bottom_sheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCreateNote = bottomSheetDialog.findViewById<Button>(R.id.btn_create_note)!!

        setUpRecyclerView()

        viewModel.allNotes.observe(viewLifecycleOwner) {
            noteAdapter.submitList(it)
        }

        binding.fabAdd.setOnClickListener {
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_SETTLING
            bottomSheetDialog.show()
        }

        btnCreateNote.setOnClickListener {
            (activity as OnboardingUtils).showProgressBar()
            Handler(Looper.getMainLooper()).postDelayed({ manageNoteCreation() }, 200)
            (activity as OnboardingUtils).hideProgressBar()
        }

        manageBottomSheetBehaviour()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun manageNoteCreation() {
        bottomSheetDialog.let {
            val title = it.findViewById<EditText>(R.id.et_note_title)!!.text
            val desc = it.findViewById<EditText>(R.id.et_note_desc)!!.text
            val swIsPinned = it.findViewById<SwitchMaterial>(R.id.switch_pin)!!

            if (title.trim().isEmpty()) {
                return
            }
            if (desc.trim().isEmpty()) {
                return
            }

            val note = Note()
            note.title = title?.trim().toString()
            note.desc = desc?.trim().toString()
            note.isPinned = swIsPinned.isChecked
            note.timestamp = Calendar.getInstance().timeInMillis

            viewModel.insertNote(note)
        }

        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setUpRecyclerView() = binding.rvNote.apply {
        noteAdapter = NoteAdapter(this@MainFragment)
        adapter = noteAdapter
        layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun manageBottomSheetBehaviour() {
        bottomSheetDialog.behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                //Here listen all of action bottom sheet
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.let {
                        it.et_note_title.setText("")
                        it.et_note_desc.setText("")
                    }
                }
            }
        })
    }

    override fun onNoteClicked(note: Note) {
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_SETTLING
        bottomSheetDialog.show()

        bottomSheetDialog.let {
            it.et_note_title.setText(note.title)
            it.et_note_desc.setText(note.desc)
            it.switch_pin.isChecked = note.isPinned
        }
    }

    override fun onNoteLongClicked(note: Note) {
        TODO("Not yet implemented")
    }
}