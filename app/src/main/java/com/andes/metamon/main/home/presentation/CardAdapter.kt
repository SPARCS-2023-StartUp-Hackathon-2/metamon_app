package com.andes.metamon.main.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andes.metamon.R
import com.andes.metamon.databinding.ItemIdCardBinding
import com.andes.metamon.main.home.domain.model.response.UserCardInfo
import com.andes.metamon.utils.ItemDiffCallback
import com.andes.metamon.utils.setOnSingleClickListener
import com.bumptech.glide.Glide

class CardAdapter(
    private val onCardClick: (Long) -> Unit
) : ListAdapter<UserCardInfo, CardAdapter.CardViewHolder>(
    ItemDiffCallback<UserCardInfo>(
        onContentsTheSame = { oldItem, newItem -> oldItem.hashCode() == newItem.hashCode() },
        onItemsTheSame = { oldItem, newItem -> oldItem.userId == newItem.userId }
    )
) {
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        val binding = ItemIdCardBinding.inflate(inflater, parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.onBind(getItem(position), onCardClick)
    }

    class CardViewHolder(private val binding: ItemIdCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            userCardInfo: UserCardInfo,
            onCardClick: (Long) -> Unit
        ) {
            binding.root.setOnSingleClickListener { onCardClick(userCardInfo.authCardId) }
            Glide.with(itemView)
                .load(userCardInfo.qrImageUrl)
                .into(binding.ivImage)
        }
    }
}