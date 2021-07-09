package pan.alexander.training.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pan.alexander.training.R
import pan.alexander.training.databinding.FragmentContactsBinding
import pan.alexander.training.presentation.recycler.ContactsAdapter
import pan.alexander.training.presentation.viewmodels.ContactsViewModel
import pan.alexander.training.utils.PermissionUtils

class ContactsFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(ContactsViewModel::class.java) }
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private var contactsAdapter: ContactsAdapter? = null

    private val requestReadContactsPermissionLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                observeContacts()
            } else {
                showReadContactsPermissionMissingDialog()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initContactsRecycler()

        showProgressBar()

        return root
    }

    private fun initContactsRecycler() {
        context?.let {
            contactsAdapter = ContactsAdapter(it)
        }
        binding.contactsRecyclerView.adapter = contactsAdapter
    }

    private fun showProgressBar() = with(binding) {
        progressBarContacts.isIndeterminate = true
        progressBarContacts.show()
    }

    private fun hideProgressBar() = with(binding) {
        progressBarContacts.hide()
        progressBarContacts.isIndeterminate = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            PermissionUtils.checkReadContactsPermission(
                it,
                requestReadContactsPermissionLauncher
            ) { granted ->
                if (granted) {
                    observeContacts()
                } else {
                    showReadContactsPermissionMissingDialog()
                }
            }
        }
    }

    private fun observeContacts() {
        viewModel.getContactsLiveData().observe(viewLifecycleOwner) {
            contactsAdapter?.updateContacts(it)
            hideProgressBar()
        }
    }

    private fun showReadContactsPermissionMissingDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.access_to_contacts_dialog_title)
                .setMessage(R.string.access_to_contacts_missing_dialog_message)
                .setNegativeButton(R.string.close) { _, _ ->
                }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
