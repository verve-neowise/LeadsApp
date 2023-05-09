package com.neowise.leads.ui.screen.leadsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.neowise.leads.R
import com.neowise.leads.databinding.ItemLeadBinding
import com.neowise.leads.domain.models.LeadPartial
import com.neowise.leads.util.load
import com.neowise.leads.util.showIfExists
import com.neowise.leads.util.toFormat

class LeadsAdapter(
    private val leads: List<LeadPartial>,
    private val onSelect: (LeadPartial) -> Unit
) : Adapter<LeadViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadViewHolder {
        return LeadViewHolder(
            view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_lead, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        val model = leads[position]
        holder.bind(model)
        holder.itemView.setOnClickListener {
            onSelect(model)
        }
    }

    override fun getItemCount(): Int {
        return leads.size
    }
}

class LeadViewHolder(view: View) : ViewHolder(view) {
    private val binding = ItemLeadBinding.bind(view)

    fun bind(model: LeadPartial) {

        val context = itemView.context

        binding.displayName.text = model.displayName
            ?: context.getString(R.string.unknown)

        binding.countryFlag.text = model.countryEmoji

        binding.createdDate.text =
            context.getString(R.string.created_at_pattern, model.createdAt?.toFormat())
        binding.updatedDate.text =
            context.getString(R.string.updated_at_pattern, model.updatedAt?.toFormat())

        binding.intention.showIfExists(model.intention)
        binding.adSource.showIfExists(model.adSource)
        binding.channelSource.showIfExists(model.channelSource)
        binding.status.showIfExists(model.status)
        binding.avatar.load(model.avatar)
    }
}