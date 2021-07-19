package pan.alexander.training.presentation.recycler

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.training.R
import pan.alexander.training.databinding.RecyclerItemContactBinding
import pan.alexander.training.domain.entities.Contact

class ContactsViewHolder(
    private val context: Context,
    private val binding: RecyclerItemContactBinding,
    private val contacts: List<Contact>
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    fun bind(contact: Contact) = with(binding) {
        contactName.text = contact.name
        contactBadge.assignContactUri(contact.contactUri)
        when (contact.photo) {
            null -> contactBadge.setImageResource(R.drawable.ic_baseline_account_box_24)
            else -> contactBadge.setImageBitmap(contact.photo)
        }
        imageViewCall.setOnClickListener(this@ContactsViewHolder)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.imageViewCall) {
            adapterPosition.takeIf { it >= 0 }?.let { position ->
                contacts[position].phoneNumbers.takeIf { it.isNotEmpty() }
            }?.let { numbers ->
                val intent by lazy {
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.fromParts("tel", numbers.first(), null)
                    )
                }
                context.startActivity(intent)
            }
        }
    }
}
