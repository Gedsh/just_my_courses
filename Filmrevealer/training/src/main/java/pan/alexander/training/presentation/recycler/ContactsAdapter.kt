package pan.alexander.training.presentation.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.training.databinding.RecyclerItemContactBinding
import pan.alexander.training.domain.entities.Contact

class ContactsAdapter(private val context: Context) : RecyclerView.Adapter<ContactsViewHolder>() {
    private val contacts = mutableListOf<Contact>()

    fun updateContacts(contacts: List<Contact>) {
        this.contacts.clear()
        this.contacts.addAll(contacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding =
            RecyclerItemContactBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContactsViewHolder(context, binding, contacts)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}
