package pan.alexander.pictureoftheday.framework.ui.notes

import androidx.recyclerview.widget.DiffUtil
import pan.alexander.pictureoftheday.domain.notes.entities.Note

class DiffUtilCallback(
    private val oldItems: List<Note>,
    private val newItems: List<Note>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].text == newItems[newItemPosition].text
                && oldItems[oldItemPosition].priority == newItems[newItemPosition].priority
    }
}
