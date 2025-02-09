package com.example.androidproject.ui.tennis.playercontentviewpager.matches

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidproject.R
import com.example.androidproject.api.ApiUtils
import com.example.androidproject.core.domain.Match
import com.example.androidproject.databinding.ItemMatchBinding

class MatchesAdapter(
    private val playerId: Int
) : ListAdapter<Match, MatchesAdapter.MatchViewHolder>(
    MatchDiffCallback()
) {

    private class MatchDiffCallback : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding = ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding)
    }

    inner class MatchViewHolder(
        private val binding: ItemMatchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(match: Match) {
            bindText(match)
            bindImages(match)
            updateMatchResultUI(match)
        }

        private fun bindText(match: Match) {
            binding.run {
                tournamentTextView.text = root.context.getString(
                    R.string.tournament_text,
                    match.tournament.uniqueTournament?.name
                        ?: root.context.getString(R.string.unknown_tournament),
                    match.tournament.name
                )
                surfaceTextView.text = match.tournament.uniqueTournament?.groundType
                    ?: root.context.getString(R.string.unknown_ground)
                dateTextView.text = formatTimestamp(match.startTimestamp)
                homePlayerTextView.text = match.homeTeam.name
                awayPlayerTextView.text = match.awayTeam.name
                homeScoreTextView.text = String.format(match.homeScore.current.toString())
                awayScoreTextView.text = String.format(match.awayScore.current.toString())
            }
        }

        private fun bindImages(match: Match) {
            binding.run {
                Glide.with(root.context)
                    .load(
                        ApiUtils.BASE_TOURNAMENT_IMAGE_URL + match.tournament.uniqueTournament?.id + ApiUtils.IMAGE
                    ).error(R.drawable.tournament_icon)
                    .into(tournamentImageView)
                Glide.with(root.context)
                    .load(ApiUtils.BASE_PLAYER_IMAGE_URL + match.homeTeam.id + ApiUtils.IMAGE)
                    .into(homePlayerImageView)
                Glide.with(root.context)
                    .load(ApiUtils.BASE_PLAYER_IMAGE_URL + match.awayTeam.id + ApiUtils.IMAGE)
                    .into(awayPlayerImageView)
            }
        }

        private fun formatTimestamp(timestamp: Long): String {
            val format = java.text.SimpleDateFormat(DATE_FORMAT, java.util.Locale.getDefault())
            return format.format(java.util.Date(timestamp * TIME))
        }

        private fun getResultColor(context: Context, isWinner: Boolean): Int =
            ContextCompat.getColor(
                context,
                if (isWinner) R.color.md_theme_win else R.color.md_theme_errorContainer_mediumContrast
            )

        private fun updateMatchResultUI(match: Match) {
            val isHomeWinner = match.winnerCode == 1
            val isAwayWinner = match.winnerCode == 2

            binding.run {
                when {
                    isHomeWinner && match.homeTeam.id == playerId -> {
                        matchResultTextView.background.setTint(
                            getResultColor(
                                root.context,
                                true
                            )
                        )
                        matchResultTextView.text = root.context.getString(R.string.win)
                    }

                    isAwayWinner && match.awayTeam.id == playerId -> {
                        matchResultTextView.background.setTint(
                            getResultColor(
                                root.context,
                                true
                            )
                        )
                        matchResultTextView.text = root.context.getString(R.string.win)
                    }

                    match.homeTeam.id == playerId -> {
                        matchResultTextView.background.setTint(
                            getResultColor(
                                root.context,
                                false
                            )
                        )
                        matchResultTextView.text = root.context.getString(R.string.lose)
                    }

                    else -> {
                        matchResultTextView.background.setTint(
                            getResultColor(
                                root.context,
                                false
                            )
                        )
                        matchResultTextView.text = root.context.getString(R.string.lose)
                    }
                }

                homeScoreTextView.setTypeface(
                    null,
                    if (isHomeWinner) android.graphics.Typeface.BOLD_ITALIC else android.graphics.Typeface.NORMAL
                )
                awayScoreTextView.setTypeface(
                    null,
                    if (isAwayWinner) android.graphics.Typeface.BOLD_ITALIC else android.graphics.Typeface.NORMAL
                )
                homePlayerTextView.setTypeface(
                    null,
                    if (isHomeWinner) android.graphics.Typeface.BOLD_ITALIC else android.graphics.Typeface.NORMAL
                )
                awayPlayerTextView.setTypeface(
                    null,
                    if (isAwayWinner) android.graphics.Typeface.BOLD_ITALIC else android.graphics.Typeface.NORMAL
                )
            }
        }
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private const val TIME = 1_000
        private const val DATE_FORMAT = "dd.MM.yy"
    }
}

