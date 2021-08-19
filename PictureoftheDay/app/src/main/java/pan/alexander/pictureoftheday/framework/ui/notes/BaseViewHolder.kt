package pan.alexander.pictureoftheday.framework.ui.notes

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.pictureoftheday.domain.notes.entities.Note

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener, View.OnLongClickListener, View.OnFocusChangeListener {
    abstract fun bind(note: Note)
}
