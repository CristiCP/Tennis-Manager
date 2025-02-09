package com.example.androidproject.ui.tennis.playercontentviewpager.playerdetails

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.androidproject.R
import com.example.androidproject.api.ApiUtils
import com.example.androidproject.core.domain.Match
import com.example.androidproject.core.domain.Media
import com.example.androidproject.ui.BaseFragment
import com.example.androidproject.core.domain.PlayerData
import com.example.androidproject.core.domain.Rankings
import com.example.androidproject.databinding.FragmentTennisPlayerDetailsBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class TennisPlayerDetailsFragment(
    private val playerId: Int
) : BaseFragment<FragmentTennisPlayerDetailsBinding>(
    FragmentTennisPlayerDetailsBinding::inflate
) {

    private val viewModel: TennisPlayerDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.getPlayerMedia(playerId)
        viewModel.getPlayer(playerId)
        viewModel.getPlayerLastMatch(playerId)
        viewModel.getPlayerHighestRank(playerId)
    }

    private fun initObservers() {
        viewModel.player.observe(viewLifecycleOwner) { player ->
            player?.let { bindPlayerDetails(it) }
        }

        viewModel.media.observe(viewLifecycleOwner) { media ->
            media?.let { bindPlayerMedia(it) }
        }

        viewModel.lastMatch.observe(viewLifecycleOwner) { lastMatch ->
            lastMatch?.let { bindPlayerLastMatch(it) }
        }

        viewModel.homePlayer.observe(viewLifecycleOwner) { homePlayer ->
            homePlayer?.let { bindHomePlayer(it) }
        }

        viewModel.awayPlayer.observe(viewLifecycleOwner) { awayPlayer ->
            awayPlayer?.let { bindAwayPlayer(it) }
        }

        viewModel.highestRank.observe(viewLifecycleOwner) { rank ->
            rank?.let { bindPlayerHighestRank(it) }
        }

        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            binding.run {
                loadingIndicator.isVisible = isLoading
                featuredMatchCardView.isVisible = !isLoading
                profileCardView.isVisible = !isLoading
                atpCardView.isVisible = !isLoading
                prizeCardView.isVisible = !isLoading
            }
        }
    }

    private fun bindPlayerLastMatch(lastMatch: Match) {
        viewModel.getHomePlayer(lastMatch.homeTeam.id)
        viewModel.getAwayPlayer(lastMatch.awayTeam.id)
        bindText(lastMatch)
        bindImage(lastMatch)
        viewModel.loadingTrigger()
    }

    private fun bindText(lastMatch: Match) {
        binding.run {
            tournamentNameTextView.text = lastMatch.tournament.name
            homePlayerTextView.text = lastMatch.homeTeam.name
            homePlayerScoreTextView.text = String.format(lastMatch.homeScore.current.toString())
            awayPlayerTextView.text = lastMatch.awayTeam.name
            awayPlayerScoreTextView.text = String.format(lastMatch.awayScore.current.toString())

            if (lastMatch.homeScore.current > lastMatch.awayScore.current) {
                homePlayerTextView.setTypeface(null, android.graphics.Typeface.BOLD)
                homePlayerScoreTextView.setTypeface(null, android.graphics.Typeface.BOLD)
            } else if (lastMatch.awayScore.current > lastMatch.homeScore.current) {
                awayPlayerTextView.setTypeface(null, android.graphics.Typeface.BOLD)
                awayPlayerScoreTextView.setTypeface(null, android.graphics.Typeface.BOLD)
            }
        }
    }

    private fun bindImage(lastMatch: Match) {
        binding.run {
            Glide.with(root.context)
                .load(
                    ApiUtils.BASE_TOURNAMENT_IMAGE_URL + lastMatch.tournament.uniqueTournament?.id + ApiUtils.IMAGE
                ).error(R.drawable.tournament_icon)
                .into(tournamentImageView)
            Glide.with(root.context)
                .load(ApiUtils.BASE_PLAYER_IMAGE_URL + lastMatch.homeTeam.id + ApiUtils.IMAGE)
                .into(homePlayerImageView)
            Glide.with(root.context)
                .load(ApiUtils.BASE_PLAYER_IMAGE_URL + lastMatch.awayTeam.id + ApiUtils.IMAGE)
                .into(awayPlayerImageView)
        }
    }

    private fun bindHomePlayer(player: PlayerData) {
        binding.run {
            Glide.with(root.context).load(
                ApiUtils.BASE_COUNTRY_IMAGE_URL + (player.player.country?.alpha2?.lowercase(
                    Locale.ROOT
                )
                    ?: ApiUtils.DEFAULT_IMAGE) + ApiUtils.IMAGE_FORMAT
            ).into(homePlayerCountryImageView)
        }
    }

    private fun bindAwayPlayer(player: PlayerData) {
        binding.run {
            Glide.with(root.context).load(
                ApiUtils.BASE_COUNTRY_IMAGE_URL + (player.player.country?.alpha2?.lowercase(
                    Locale.ROOT
                )
                    ?: ApiUtils.DEFAULT_IMAGE) + ApiUtils.IMAGE_FORMAT
            ).into(awayPlayerCountryImageView)
        }
    }

    private fun bindPlayerHighestRank(highestRank: Rankings) {
        binding.run {
            highestRankValueTextView.text =
                String.format(highestRank.rankings[0].bestRanking.toString())
        }
    }

    private fun bindPlayerDetails(player: PlayerData) {
        binding.run {
            fullNameValueTextView.text = player.player.fullName
            countryValueTextView.text = player.player.country?.name
            ageValueTextView.text =
                String.format(
                    viewModel.formatAge(player.player.playerInfo.birthDateTimestamp).toString()
                )
            heightValueTextView.text = String.format(player.player.playerInfo.height.toString())
            playsWithValueTextView.text =
                player.player.playerInfo.plays.replaceFirstChar { it.uppercase() }
            currentRankValueTextView.text = String.format(player.player.ranking.toString())
            thisYearValueTextView.text =
                context?.let { viewModel.formatMoney(it, player.player.playerInfo.prizeCurrent) }
            careerTotalValueTextView.text = context?.let {
                viewModel.formatMoney(
                    it,
                    player.player.playerInfo.prizeTotal
                )
            }
        }
    }

    private fun bindPlayerMedia(playerMedia: Media) {
        val videoId = viewModel.extractVideoId(playerMedia.url)
        binding.run {
            youtubeMediaPlayer.apply {
                lifecycle.addObserver(this)
                visibility = View.VISIBLE
                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
            mediaTitleTextView.text = playerMedia.title
            mediaSubtitleTextView.text = playerMedia.subtitle
            mediaCardView.visibility = View.VISIBLE
        }
    }
}