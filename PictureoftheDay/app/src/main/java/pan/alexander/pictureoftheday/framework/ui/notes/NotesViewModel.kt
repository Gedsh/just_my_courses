package pan.alexander.pictureoftheday.framework.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pan.alexander.pictureoftheday.domain.notes.entities.Note
import java.util.*
import kotlin.collections.ArrayList

class NotesViewModel : ViewModel() {
    val notes = ArrayList<Note>()

    private val notesMutableLiveData = MutableLiveData<List<Note>>(notes)
    val notesLiveData: LiveData<List<Note>> get() = notesMutableLiveData

    fun appendNote(note: Note) {
        notes.add(note)
        notesMutableLiveData.value = notes
    }

    fun deleteNote(id: Int) {
        notes.remove(notes.first { it.id == id })
        notesMutableLiveData.value = notes
    }

    fun changeNotePriority(id: Int) {
        for (i in notes.indices) {
            val note = notes[i]
            if (note.id == id) {
                notes[i] = when (note.priority) {
                    Note.Priority.LOW -> Note(note.id, note.text, note.date, Note.Priority.NORMAL)
                    Note.Priority.NORMAL -> Note(note.id, note.text, note.date, Note.Priority.HIGH)
                    Note.Priority.HIGH -> Note(note.id, note.text, note.date, Note.Priority.LOW)
                }
                notesMutableLiveData.value = notes
                break
            }
        }
    }

    fun saveText(id: Int, text: String) {
        for (i in notes.indices) {
            val note = notes[i]
            if (note.id == id) {
                if (text != note.text) {
                    notes[i] = Note(note.id, text, Date(), note.priority)
                }
                notesMutableLiveData.value = notes
                break
            }
        }
    }

    fun moveNote(fromId: Int, toId: Int) {
        var fromPosition = -1
        var toPosition = -1
        for (i in notes.indices) {
            val note = notes[i]
            if (note.id == fromId) {
                fromPosition = i
            } else if (note.id == toId) {
                toPosition = i
            }

            if (fromPosition >= 0 && toPosition >= 0) {
                break
            }
        }

        notes.removeAt(fromPosition).apply {
            notes.add(
                if (toPosition > fromPosition) {
                    toPosition - 1
                } else {
                    toPosition
                }, this
            )
        }
        notesMutableLiveData.value = notes
    }
}
