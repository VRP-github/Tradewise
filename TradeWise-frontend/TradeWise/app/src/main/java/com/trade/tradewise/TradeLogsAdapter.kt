package com.trade.tradewise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trade.tradewise.databinding.ItemTradeLogBinding
import com.trade.tradewise.model.TradLogsModel

class TradeLogsAdapter(private val list: List<TradLogsModel>) :
    RecyclerView.Adapter<TradeLogsAdapter.TradeLogsViewHolder>() {

    inner class TradeLogsViewHolder(val binding: ItemTradeLogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeLogsViewHolder {
        val binding = ItemTradeLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TradeLogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TradeLogsViewHolder, position: Int) {
        val item = list[position]

        // Bind data to each TextView in the layout
        holder.binding.tvFilename.text = item.fILENAME ?: "-"
        holder.binding.tvDate.text = item.cREATEDATE ?: "-"
        holder.binding.tvUploadedBy.text = item.cREATEDBY ?: "-"
        holder.binding.tvDuplicate.text = item.dUPLICATEENTRIES?.toString() ?: "0"
    }

    override fun getItemCount(): Int = list.size
}
