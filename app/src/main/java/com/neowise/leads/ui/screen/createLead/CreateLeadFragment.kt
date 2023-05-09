package com.neowise.leads.ui.screen.createLead

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.neowise.leads.R
import com.neowise.leads.databinding.FragmentCreateLeadBinding
import com.neowise.leads.domain.models.*
import com.neowise.leads.domain.models.inputs.CreateLeadInput
import com.neowise.leads.ui.custom.selection.SimpleSelectionAdapter
import com.neowise.leads.ui.shared.CountrySelectionAdapter
import com.neowise.leads.ui.shared.LanguageSelectionAdapter
import com.neowise.leads.util.*
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CreateLeadFragment : Fragment(R.layout.fragment_create_lead) {

    private val binding: FragmentCreateLeadBinding by viewBinding()
    private val viewModel: CreateLeadViewModel by activityViewModel()

    private val countrySelectionAdapter = CountrySelectionAdapter()
    private val citySelectionAdapter = SimpleSelectionAdapter<City>()
    private val languageSelectionAdapter = LanguageSelectionAdapter()
    private val sourceSelectionAdapter = SimpleSelectionAdapter<AdSource>()

    override fun onStart() {
        super.onStart()

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbar.setNavigationOnClickListener {
            back()
        }

        binding.country.adapter = countrySelectionAdapter
        binding.city.adapter = citySelectionAdapter
        binding.language.adapter = languageSelectionAdapter
        binding.adSource.adapter = sourceSelectionAdapter

        binding.country.setOnSelectionChangeListener {
            val country: Country = it as Country
            updateCountryCode(country)
            updateCities(country)
        }

        binding.saveBtn.setOnClickListener {
            val inputs = validateInputs()
            if (inputs != null) {
                saveChanges(inputs)
            }
        }

        binding.cancelBtn.setOnClickListener {
            back()
        }

        subscribeUpdates()
    }

    private fun saveChanges(inputs: CreateLeadInput) {
        viewModel.createLead(inputs)
    }

    private fun validateInputs(): CreateLeadInput? {
        val firstName: String = binding.firstEdit.string()
        val lastName: String = binding.lastEdit.string()
        val leadIntentionType: LeadIntentionType? = binding.leadType.selectedItem
        val country: Country? = binding.country.selectedItem as Country?
        val city: City? = binding.city.selectedItem as City?
        val languages: List<Language> = languageSelectionAdapter.selectedItems
        val number: String = binding.numberEdit.string()
        val email: String = binding.emailEdit.string()

        val isValid = validate(binding.firstEdit) { firstName.isNotEmpty() }
            .validate(binding.leadType) { leadIntentionType.isNotNull() }
            .validate(binding.language) { languages.isNotEmpty() }
            .validate(binding.numberEdit) { number.isNotEmpty() }

        val phone = if (country.isNotNull()) "${country?.phoneCode}${number}" else ""

        return if (!isValid) {
            null
        }
        else {
            CreateLeadInput(
                firstName = firstName,
                lastName = lastName,
                intentionType = leadIntentionType!!,
                phone = phone,
                email = email,
                country = country,
                city = city,
                languages = languages
            )
        }
    }

    private fun updateCountryCode(country: Country) {
        binding.city.setText("")
        binding.numberEditLayout.prefixText = country.phoneCode
    }

    private fun updateCities(country: Country) {
        viewModel.fetchCities(country.id)
    }

    private fun subscribeUpdates() {

        viewModel.createState.launchAndCollectIn(viewLifecycleOwner) { state ->

            binding.saveBtn.isEnabled = true

            when (state) {
                is CreateState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is CreateState.Loading -> {
                    binding.saveBtn.isEnabled = false
                }
                is CreateState.Success -> {
                    findNavController().navigate(R.id.main)
                }
                is CreateState.Nothing -> {
                }
            }
        }

        viewModel.detailsOptions.launchAndCollectIn(viewLifecycleOwner) { options ->
            Log.d("LEAD TYPES", "initDialog: ${options.leadIntentionTypes}")

            countrySelectionAdapter.setItems(options.countries)
            languageSelectionAdapter.setItems(options.languages)
            sourceSelectionAdapter.setItems(options.adSources)
            binding.leadType.items = options.leadIntentionTypes
        }

        viewModel.cities.launchAndCollectIn(viewLifecycleOwner) { cities ->
            citySelectionAdapter.setItems(cities)
        }
    }
}