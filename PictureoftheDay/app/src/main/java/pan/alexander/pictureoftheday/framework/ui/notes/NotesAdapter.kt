package pan.alexander.pictureoftheday.framework.ui.notes

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.databinding.NoteRecycleItemBinding
import pan.alexander.pictureoftheday.databinding.NotesFooterRecycleItemBinding
import pan.alexander.pictureoftheday.domain.notes.entities.Note
import pan.alexander.pictureoftheday.framework.App.Companion.LOG_TAG
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val CARD_DRAG_ACTION_TRANSPARENCY = 215

class NotesAdapter : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    var onNoteAddClicked: (() -> Unit)? = null
    var onNoteDeleteClicked: ((id: Int) -> Unit)? = null
    var onNoteLongClicked: ((id: Int) -> Unit)? = null
    var onNoteTextFocusChanged: ((id: Int, text: String) -> Unit)? = null
    var onNoteMoved: ((fromId: Int, toId: Int) -> Unit)? = null
    var dragListener: OnStartDragListener? = null

    private val simpleDateFormat by lazy { SimpleDateFormat("dd.MM.yy", Locale.getDefault()) }
    private val notes: MutableList<Note> = ArrayList()

    fun updateNotes(notes: List<Note>) {

        val notesWithButton = ArrayList<Note>(notes).apply {
            add(Note(0, "", Date(0), Note.Priority.NORMAL))
        }

        val result = DiffUtil.calculateDiff(DiffUtilCallback(this.notes, notesWithButton))

        this.notes.apply {
            clear()
            addAll(notesWithButton)
        }

        result.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isItemFooter(position)) {
            TYPE_FOOTER
        } else {
            TYPE_NOTE
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == TYPE_NOTE) {
            val binding = NoteRecycleItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

            val holder = NotesViewHolder(binding)

            binding.imageButtonDeleteNote.setOnClickListener(holder)
            binding.noteCard.setOnLongClickListener(holder)
            binding.editTextNote.onFocusChangeListener = holder
            binding.imageButtonMoveNote.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    dragListener?.onStartDrag(holder)
                }
                false
            }

            holder
        } else {
            val binding = NotesFooterRecycleItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

            val holder = FooterViewHolder(binding)

            binding.imageButtonAddNote.setOnClickListener(holder)

            holder
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    fun isItemFooter(position: Int?): Boolean {
        return if (position == null || position == RecyclerView.NO_POSITION) {
            false
        } else {
            return notes[position].id == 0
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        onNoteMoved?.invoke(notes[fromPosition].id, notes[toPosition].id)
    }

    override fun onItemDismiss(position: Int) {
        onNoteDeleteClicked?.invoke(notes[position].id)
    }

    inner class NotesViewHolder(
        private val binding: NoteRecycleItemBinding
    ) : BaseViewHolder(binding.root), ItemTouchHelperViewHolder {

        private var cardDefaultColor: Int = 0

        override fun bind(note: Note) {
            setText(note)
            setDate(note)
            setColorByPriority(note)
            requestFocusIfEmpty(note)
        }

        private fun setText(note: Note) {
            binding.editTextNote.setText(note.text, TextView.BufferType.EDITABLE)
        }

        private fun setDate(note: Note) {
            binding.textViewDate.text = simpleDateFormat.format(note.date)
        }

        private fun setColorByPriority(note: Note) {
            binding.noteCard.setCardBackgroundColor(note.priority.getColor(binding.root.context))
        }

        private fun requestFocusIfEmpty(note: Note) {
            if (note.text.isBlank()) {
                binding.editTextNote.requestFocus()
            }
        }

        override fun onClick(v: View?) {
            adapterPosition.takeIf { adapterPosition != RecyclerView.NO_POSITION }?.let {
                when (v?.id) {
                    R.id.imageButtonDeleteNote -> {
                        onNoteDeleteClicked?.invoke(notes[it].id)
                    }
                    else -> Log.e(LOG_TAG, "Unknown view Id ${v.toString()}")
                }
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onNoteTextFocusChanged?.invoke(
                    notes[position].id,
                    binding.editTextNote.text.toString()
                )
                onNoteLongClicked?.invoke(notes[position].id)
                return true
            }
            return false
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            when (v?.id) {
                R.id.editTextNote -> {
                    val position = adapterPosition
                    if (!hasFocus && position != RecyclerView.NO_POSITION) {
                        onNoteTextFocusChanged?.invoke(
                            notes[position].id,
                            binding.editTextNote.text.toString()
                        )
                    }
                }
            }
        }

        override fun onItemSelected() {
            cardDefaultColor = binding.noteCard.cardBackgroundColor.defaultColor
            binding.noteCard.setCardBackgroundColor(
                ColorUtils.setAlphaComponent(cardDefaultColor, CARD_DRAG_ACTION_TRANSPARENCY)
            )
        }

        override fun onItemClear() {
            binding.noteCard.setCardBackgroundColor(cardDefaultColor)
        }

    }

    inner class FooterViewHolder(
        binding: NotesFooterRecycleItemBinding
    ) : BaseViewHolder(binding.root) {
        override fun bind(note: Note) {}

        override fun onClick(v: View?) {
            adapterPosition.takeIf { adapterPosition != RecyclerView.NO_POSITION }?.let {
                when (v?.id) {
                    R.id.imageButtonAddNote -> onNoteAddClicked?.invoke()
                    else -> Log.e(LOG_TAG, "Unknown view Id ${v.toString()}")
                }
            }
        }

        override fun onLongClick(v: View?): Boolean = false
        override fun onFocusChange(v: View?, hasFocus: Boolean) {}
    }

    private companion object {
        private const val TYPE_NOTE = 1
        private const val TYPE_FOOTER = 2
    }

}
