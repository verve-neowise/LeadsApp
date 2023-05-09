package com.neowise.leads.ui.screen.leadProfile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.neowise.leads.R
import com.neowise.leads.databinding.FragmentLeadProfileBinding
import com.neowise.leads.domain.models.*
import com.neowise.leads.domain.models.inputs.UpdateLeadInput
import com.neowise.leads.ui.custom.selection.SimpleSelectionAdapter
import com.neowise.leads.ui.custom.statusbar.StatusAdapter
import com.neowise.leads.ui.shared.CountrySelectionAdapter
import com.neowise.leads.ui.shared.LanguageSelectionAdapter
import com.neowise.leads.util.*
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LeadProfileFragment : Fragment(R.layout.fragment_lead_profile) {

    private val args: LeadProfileFragmentArgs by navArgs()

    private val binding: FragmentLeadProfileBinding by viewBinding()
    private val viewModel: LeadProfileViewModel by activityViewModel()

    private val countrySelectionAdapter = CountrySelectionAdapter()
    private val citySelectionAdapter = SimpleSelectionAdapter<City>()
    private val languageSelectionAdapter = LanguageSelectionAdapter()
    private val adSourceSelectionAdapter = SimpleSelectionAdapter<AdSource>()
    private val webSourceSelectionAdapter = SimpleSelectionAdapter<WebSource>()
    private val channelSourceSelectionAdapter = SimpleSelectionAdapter<ChannelSource>()
    private val propertySourceSelectionAdapter = SimpleSelectionAdapter<PropertyType>()
    private val nationalitySelectionAdapter = SimpleSelectionAdapter<Nationality>()
    private val statusAdapter = StatusAdapter(listOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leadStatus.adapter = statusAdapter
        binding.country.adapter = countrySelectionAdapter
        binding.city.adapter = citySelectionAdapter
        binding.language.adapter = languageSelectionAdapter
        binding.adSource.adapter = adSourceSelectionAdapter
        binding.webSource.adapter = webSourceSelectionAdapter
        binding.channelSource.adapter = channelSourceSelectionAdapter
        binding.propertyType.adapter = propertySourceSelectionAdapter
        binding.nationality.adapter = nationalitySelectionAdapter

        binding.processIcon.setOnClickListener{
            binding.processIcon.performLongClick()
        }

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbar.setNavigationOnClickListener {
            back()
        }
    }

    override fun onStart() {
        super.onStart()

        subscribeForUpdates()
        watchChanges()
    }

    private fun watchChanges() {

        binding.leadStatus.setOnSelectionChangeListener {
            sendChanges("Status", UpdateLeadInput())
        }

        binding.propertyType.setOnSelectionChangeListener {
            sendChanges("Property Type", UpdateLeadInput())
        }

        binding.country.setOnSelectionChangeListener {
            val country: Country = it as Country
            sendChanges("Country", UpdateLeadInput(country = country))
            fetchCities(country)
        }

        binding.leadType.setOnSelectionChangeListener {
            sendChanges("Intention Type", UpdateLeadInput(intentionType = it))
        }

        binding.city.setOnSelectionChangeListener {
            sendChanges("City", UpdateLeadInput(city = it as City))
        }

        binding.language.setOnSelectionChangeListener {
            val languages = languageSelectionAdapter.selectedItems
            Log.d("TAG", "watchChanges: $languages")
            sendChanges("Languages", UpdateLeadInput(languages = languages))
        }

        binding.nationality.setOnSelectionChangeListener {
            sendChanges("Nationality", UpdateLeadInput(nationality = it as Nationality))
        }

        binding.birthdayEdit.addTextCompleteListener { value ->
            val date = value.parseDate()

            if (date != null) {
                sendChanges("Birthday", UpdateLeadInput(birthDate = date))
            }
        }

        binding.budget.addTextCompleteListener { value ->
            val budget: Double? = value.toDoubleOrNull()

            if (budget != null) {
                sendChanges("Budget", UpdateLeadInput(budget = budget))
            }
        }
    }

    private fun sendChanges(data: String, update: UpdateLeadInput) {
        val status = binding.leadStatus.selectedItem ?: return
        val propertyType = binding.propertyType.selectedItem as? PropertyType

        if (propertyType == null) {
            binding.propertyType.error = "Property must be selected"
            viewModel.errorProcessStatus("Property must be selected")
            return
        }

        binding.propertyType.error = null
        viewModel.updateLead(
            data, update.copy(
                status = status,
                propertyType = propertyType
            )
        )
    }

    private fun subscribeForUpdates() {

        viewModel.detailsOptions.launchAndCollectIn(viewLifecycleOwner) {
            when (it) {
                is State.Success -> {
                    updateSelections(it.data)
                    viewModel.fetchLead(args.id)
                }
                is State.Error -> showError(it.message)
                is State.Loading -> {}
            }

            binding.gridLayout.isEnabled = it is State.Success
        }

        viewModel.leadState.launchAndCollectIn(viewLifecycleOwner) {
            when (it) {
                is State.Success -> {
                    render(it.data)
                    showContent()
                }
                is State.Error -> {
                    showError(it.message)
                }
                is State.Loading -> {
                    showLoading()
                }
            }
        }

        viewModel.cities.launchAndCollectIn(viewLifecycleOwner) {
            when (it) {
                is State.Success -> updateCitiesList(it.data)
                is State.Error -> showError(it.message)
                is State.Loading -> { }
            }
        }

        viewModel.processStatus.launchAndCollectIn(viewLifecycleOwner) {
            val icon = when(it) {
                is State.Success -> R.drawable.ic_success
                is State.Error -> R.drawable.ic_alert
                is State.Loading -> R.drawable.ic_loader
            }
            binding.processIcon.setImageResource(icon)

            val message = if (it is State.Error) it.message else null

            TooltipCompat.setTooltipText(binding.processIcon, message)
        }
    }

    private fun showContent() {
        binding.content.visibility = VISIBLE
        binding.loading.visibility = GONE
    }

    private fun showLoading() {
        binding.content.visibility = INVISIBLE
        binding.loading.visibility = VISIBLE
    }

    private fun fetchCities(country: Country, defaultCity: City? = null) {
        viewModel.fetchCities(country.id, defaultCity)
    }

    private fun updateCitiesList(cities: List<City>) {
        citySelectionAdapter.setItems(cities.sortedBy { it.title.first() })
        binding.city.select(viewModel.defaultCity)
    }

    private fun updateSelections(detailsOptions: DetailsOptions) {
        statusAdapter.setItems(detailsOptions.leadStatus)
        countrySelectionAdapter.setItems(detailsOptions.countries)
        languageSelectionAdapter.setItems(detailsOptions.languages)
        adSourceSelectionAdapter.setItems(detailsOptions.adSources)
        webSourceSelectionAdapter.setItems(detailsOptions.webSources)
        channelSourceSelectionAdapter.setItems(detailsOptions.channelSources)
        propertySourceSelectionAdapter.setItems(detailsOptions.propertyTypes)
        nationalitySelectionAdapter.setItems(detailsOptions.nationalities)
        binding.leadType.items = detailsOptions.leadIntentionTypes
    }

    private fun render(lead: Lead?) {

        if (lead == null) {
            return
        }

        binding.apply {
            ID.text = getString(R.string.id_pattern, lead.id)
            displayName.text = lead.displayName
            avatar.load(lead.avatar)
            leadType.select(lead.intention)
            adSource.select(lead.adSource)
            webSource.select(lead.webSource)
            channelSource.select(lead.channelSource)
            country.select(lead.country)
            languageSelectionAdapter.selectedItems = lead.languages
            propertyType.select(lead.propertyType)
            nationality.select(lead.nationality)
            budget.setText(lead.budget?.toString())
            birthdayEdit.setText(lead.birthday)
            leadStatus.setStatus(lead.status)

            if (lead.country != null) {
                fetchCities(lead.country, lead.city)
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}