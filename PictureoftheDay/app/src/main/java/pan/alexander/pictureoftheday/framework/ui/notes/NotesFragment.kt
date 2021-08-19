package pan.alexander.pictureoftheday.framework.ui.notes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.databinding.NotesFragmentBinding
import pan.alexander.pictureoftheday.domain.notes.entities.Note
import java.util.*

class NotesFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(NotesViewModel::class.java) }
    private var _binding: NotesFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter: NotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NotesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        observeNotesChanges()
    }

    private fun initRecycler() {
        adapter = NotesAdapter()

        adapter?.apply {
            onNoteAddClicked = {
                val note = Note("", Date(), Note.Priority.NORMAL)
                viewModel.appendNote(note)
            }
            onNoteDeleteClicked = {
                viewModel.deleteNote(it)
            }
            onNoteLongClicked = {
                viewModel.changeNotePriority(it)
            }
            onNoteTextFocusChanged = { id, text ->
                viewModel.saveText(id, text)
            }
            onNoteMoved = { fromId, toId ->
                viewModel.moveNote(fromId, toId)
            }

            val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(this)).apply {
                attachToRecyclerView(binding.recyclerViewNotes)
            }
            val startDragListener = object : OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }
            dragListener = startDragListener
        }

        binding.recyclerViewNotes.adapter = adapter

    }

    private fun observeNotesChanges() {
        viewModel.notesLiveData.observe(viewLifecycleOwner) {
            if (!binding.recyclerViewNotes.isComputingLayout) {
                adapter?.updateNotes(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        initNotesFilter()
    }

    private fun initNotesFilter() {
        binding.chipGroupNotesFilter.childCount.let {
            for (i in 0 until it) {
                (binding.chipGroupNotesFilter.getChildAt(i) as Chip).setOnCheckedChangeListener { _, _ ->
                    filterNotes(binding.chipGroupNotesFilter.checkedChipIds)
                }
            }
        }
    }

    private fun filterNotes(checkedChips: List<Int>) {
        val notesPriorityLow =
            viewModel.notesLiveData.value?.filter { it.priority == Note.Priority.LOW }
        val notesPriorityNormal =
            viewModel.notesLiveData.value?.filter { it.priority == Note.Priority.NORMAL }
        val notesPriorityHigh =
            viewModel.notesLiveData.value?.filter { it.priority == Note.Priority.HIGH }
        val filteredNotes = mutableListOf<Note>()
        checkedChips.forEach {
            when (it) {
                R.id.chipPriorityLow -> filteredNotes.addAll(notesPriorityLow ?: emptyList())
                R.id.chipPriorityNormal -> filteredNotes.addAll(notesPriorityNormal ?: emptyList())
                R.id.chipPriorityHigh -> filteredNotes.addAll(notesPriorityHigh ?: emptyList())
            }
        }
        adapter?.updateNotes(filteredNotes)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        adapter?.apply {
            onNoteAddClicked = null
            onNoteDeleteClicked = null
            onNoteLongClicked = null
            onNoteTextFocusChanged = null
        }

        _binding = null
    }
}
