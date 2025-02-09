package com.example.androidproject.ui.tennis.custommatches

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidproject.R
import com.example.androidproject.api.ApiUtils
import com.example.androidproject.core.domain.CustomMatch
import com.example.androidproject.databinding.ItemCustomMatchBinding
import java.util.Locale

class CustomMatchesAdapter : ListAdapter<CustomMatch, CustomMatchesAdapter.MatchViewHolder>(
    MatchDiffCallback()
) {

    private class MatchDiffCallback : DiffUtil.ItemCallback<CustomMatch>() {
        override fun areItemsTheSame(oldItem: CustomMatch, newItem: CustomMatch): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: CustomMatch, newItem: CustomMatch): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding =
            ItemCustomMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding)
    }

    inner class MatchViewHolder(
        private val binding: ItemCustomMatchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(match: CustomMatch) {
            val homeSetsWon = calculateSetsWon(match.homeScores, match.awayScores)
            val awaySetsWon = calculateSetsWon(match.awayScores, match.homeScores)
            bindText(match, homeSetsWon, awaySetsWon)
            bindImages(match)
            highlightWinner(homeSetsWon, awaySetsWon)
        }

        private fun bindText(match: CustomMatch, homeSetsWon: Int, awaySetsWon: Int) {
            binding.run {
                root.context.run {
                    tournamentTextView.text =
                        getString(R.string.custom_tournament_text, match.tournamentName)
                    timeTextView.text =
                        getString(R.string.date_text, formatTimestamp(match.startTimestamp))
                }
                homePlayerTextView.text = match.homePlayerName
                homePlayerFirstSetTextView.text = String.format(match.homeScores[0].toString())
                homePlayerSecondSetTextView.text = String.format(match.homeScores[1].toString())
                awayPlayerTextView.text = match.awayPlayerName
                awayPlayerFirstSetTextView.text = String.format(match.awayScores[0].toString())
                awayPlayerSecondSetTextView.text = String.format(match.awayScores[1].toString())
                setBoldScore(
                    homePlayerFirstSetTextView,
                    awayPlayerFirstSetTextView,
                    match.homeScores[0],
                    match.awayScores[0]
                )
                setBoldScore(
                    homePlayerSecondSetTextView,
                    awayPlayerSecondSetTextView,
                    match.homeScores[1],
                    match.awayScores[1]
                )
                if (match.homeScores.size == 3) {
                    homePlayerThirdSetTextView.visibility = View.VISIBLE
                    awayPlayerThirdSetTextView.visibility = View.VISIBLE
                    homePlayerThirdSetTextView.text = String.format(match.homeScores[2].toString())
                    awayPlayerThirdSetTextView.text = String.format(match.awayScores[2].toString())
                    setBoldScore(
                        homePlayerThirdSetTextView,
                        awayPlayerThirdSetTextView,
                        match.homeScores[2],
                        match.awayScores[2]
                    )
                }
                homePlayerFinalScore.text = String.format(homeSetsWon.toString())
                awayPlayerFinalScore.text = String.format(awaySetsWon.toString())
            }
        }

        private fun bindImages(match: CustomMatch) {
            binding.run {
                Glide.with(root.context)
                    .load(ApiUtils.BASE_PLAYER_IMAGE_URL + match.homePlayerId + ApiUtils.IMAGE)
                    .into(homePlayerImageView)
                Glide.with(root.context)
                    .load(ApiUtils.BASE_PLAYER_IMAGE_URL + match.awayPlayerId + ApiUtils.IMAGE)
                    .into(awayPlayerImageView)
            }
        }

        private fun highlightWinner(homeSetsWon: Int, awaySetsWon: Int) {
            binding.run {
                when {
                    homeSetsWon > awaySetsWon -> {
                        homePlayerFinalScore.setTypeface(null, Typeface.BOLD)
                        awayPlayerFinalScore.setTypeface(null, Typeface.NORMAL)
                        homePlayerTextView.setTypeface(null, Typeface.BOLD)
                        awayPlayerTextView.setTypeface(null, Typeface.NORMAL)
                    }

                    else -> {
                        awayPlayerFinalScore.setTypeface(null, Typeface.BOLD)
                        homePlayerFinalScore.setTypeface(null, Typeface.NORMAL)
                        awayPlayerTextView.setTypeface(null, Typeface.BOLD)
                        homePlayerTextView.setTypeface(null, Typeface.NORMAL)
                    }
                }
            }
        }

        private fun calculateSetsWon(playerScores: List<Int>, opponentScores: List<Int>): Int {
            var setsWon = 0
            for (i in playerScores.indices) {
                if (playerScores[i] > opponentScores[i]) {
                    setsWon++
                }
            }
            return setsWon
        }

        private fun formatTimestamp(timestamp: Long): String {
            val format = java.text.SimpleDateFormat(
                DATE_FORMAT,
                Locale.getDefault()
            )
            return format.format(java.util.Date(timestamp))
        }

        private fun setBoldScore(
            homeTextView: TextView,
            awayTextView: TextView,
            homeScore: Int,
            awayScore: Int
        ) {
            homeTextView.setTypeface(null, Typeface.NORMAL)
            awayTextView.setTypeface(null, Typeface.NORMAL)

            when {
                homeScore > awayScore -> homeTextView.setTypeface(null, Typeface.BOLD)
                awayScore > homeScore -> awayTextView.setTypeface(null, Typeface.BOLD)
            }

            homeTextView.text = String.format(homeScore.toString())
            awayTextView.text = String.format(awayScore.toString())
        }
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private const val DATE_FORMAT = "dd.MM.yyyy"
    }
}