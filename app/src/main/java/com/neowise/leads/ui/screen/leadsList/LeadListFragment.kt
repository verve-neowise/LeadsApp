package com.neowise.leads.ui.screen.leadsList

import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.neowise.leads.R
import com.neowise.leads.databinding.FragmentLeadListBinding
import com.neowise.leads.domain.models.LeadPartial
import com.neowise.leads.util.activityNavController
import com.neowise.leads.util.launchAndCollectIn
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class LeadListFragment : Fragment(R.layout.fragment_lead_list) {

    private val binding: FragmentLeadListBinding by viewBinding()

    private val viewModel: LeadListViewModel by activityViewModel()

    override fun onStart() {
        super.onStart()

        initializeRecycler()
        initializeUIState()
        runProcess()

        binding.createLeadBtn.setOnClickListener {
            // navigate to create lead screen inside outer graph on EntryActivity
           activityNavController(R.id.nav_host_fragment)
                .navigate(R.id.createLead)
        }
    }

    private fun runProcess() {
        viewModel.fetchLeads()
    }

    private fun initializeUIState() {
        viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { it ->
            when(it) {
                is LeadListState.Error -> {
                    showError(it.message)
                }
                LeadListState.Loading -> {
                    showProgress()
                }
                is LeadListState.Success -> {
                    showLeads(it.leads)
                }
            }
        }
    }

    private fun showError(message: String) {
        binding.leadsRecycler.visibility = View.VISIBLE
        binding.progressIndicator.visibility = View.GONE

        Toast.makeText(
            requireContext(),
            "Error while get list: $message",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLeads(leads: List<LeadPartial>) {
        binding.leadsRecycler.visibility = View.VISIBLE
        binding.progressIndicator.visibility = View.GONE

        binding.leadsRecycler.adapter = LeadsAdapter(leads) {
            activityNavController(R.id.nav_host_fragment).navigate(
                R.id.leadProfile,
                bundleOf("id" to it.id)
            )
        }
    }

    private fun showProgress() {
        binding.leadsRecycler.visibility = View.INVISIBLE
        binding.progressIndicator.visibility = View.VISIBLE
    }

    private fun initializeRecycler() {
        // Initialize recyclerview: add divider
        binding.leadsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.leadsRecycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }
}