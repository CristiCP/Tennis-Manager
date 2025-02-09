package com.example.androidproject.ui.tennis.tennisplayers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidproject.R
import com.example.androidproject.api.ApiUtils
import com.example.androidproject.core.domain.PlayerRanking
import com.example.androidproject.core.domain.TennisItem
import com.example.androidproject.databinding.ItemTennisPlayerBinding
import java.util.Locale

typealias OnPlayerClickListener = (player: PlayerRanking) -> Unit

class TennisPlayerAdapter(
    private val onPlayerClick: OnPlayerClickListener,
) : ListAdapter<TennisItem, RecyclerView.ViewHolder>(TennisPlayerDiffCallback()) {

    private class TennisPlayerDiffCallback : DiffUtil.ItemCallback<TennisItem>() {

        override fun areItemsTheSame(oldItem: TennisItem, newItem: TennisItem): Boolean =
            if (oldItem is TennisItem.PlayerItem && newItem is TennisItem.PlayerItem) {
                oldItem.player.player.id == newItem.player.player.id
            } else {
                oldItem == newItem
            }

        override fun areContentsTheSame(oldItem: TennisItem, newItem: TennisItem): Boolean =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is TennisItem.PlayerItem -> VIEW_TYPE_PLAYER
        is TennisItem.LoadingItem -> VIEW_TYPE_LOADING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_PLAYER -> PlayerViewHolder(
                ItemTennisPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> LoadingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_loading, parent, false)
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is TennisItem.PlayerItem -> (holder as PlayerViewHolder).bind(item.player)
            is TennisItem.LoadingItem -> Unit
        }
    }

    inner class PlayerViewHolder(
        private val binding: ItemTennisPlayerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(player: PlayerRanking) {
            binding.run {
                bindText(player)
                bindImage(player)
                itemView.setOnClickListener { onPlayerClick.invoke(player) }
            }
        }

        private fun bindText(player: PlayerRanking) {
            binding.run {
                playerPositionTextView.text = String.format(player.ranking.toString())
                countryTextView.text = player.player.country?.name ?: ApiUtils.DEFAULT_IMAGE
                pointsTextView.text = String.format(player.points.toString())
            }
        }

        private fun bindImage(player: PlayerRanking) {
            binding.run {
                Glide.with(root.context)
                    .load(ApiUtils.BASE_PLAYER_IMAGE_URL + player.player.id + ApiUtils.IMAGE)
                    .into(playerProfileImageView)
                playerNameTextView.text = player.player.name
                Glide.with(root.context).load(
                    ApiUtils.BASE_COUNTRY_IMAGE_URL + (player.player.country?.alpha2?.lowercase(
                        Locale.ROOT
                    )
                        ?: ApiUtils.DEFAULT_IMAGE) + ApiUtils.IMAGE_FORMAT
                ).into(countryImageView)
            }
        }
    }

    inner class LoadingViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_PLAYER = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}