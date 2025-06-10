package com.trade.tradewise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trade.tradewise.databinding.RecyclerviewItemBinding
import com.trade.tradewise.model.ProfileOrginizationModel




class ProfileAdapter(
    private val profiles: List<ProfileOrginizationModel>,
    private val onEditClick: (ProfileOrginizationModel) -> Unit
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        with(holder.binding) {
            tvAccount.text = profile.aCCOUNTNAME ?: "N/A"

//            tvStatus.text = if (profile.iSACTIVE == "Y") "Active" else "Inactive"
            tvAmount.text = profile.iNITIALDEPOSIT ?: "$0"
            tvMethod.text = profile.pROFITCALCULATIONMETHOD ?: "N/A"

            btnEdit.setOnClickListener {
                onEditClick(profile)
            }
        }
    }

    override fun getItemCount(): Int = profiles.size
}

