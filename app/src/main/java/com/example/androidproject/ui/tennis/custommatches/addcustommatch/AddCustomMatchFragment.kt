package com.example.androidproject.ui.tennis.custommatches.addcustommatch

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.androidproject.R
import com.example.androidproject.core.domain.PlayerRanking
import com.example.androidproject.databinding.FragmentAddCustomMatchBinding
import com.example.androidproject.ui.BaseFragment
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCustomMatchFragment : BaseFragment<FragmentAddCustomMatchBinding>(
    FragmentAddCustomMatchBinding::inflate
) {
    private val matchesViewModel: AddCustomMatchViewModel by viewModels()
    private val scores = (0..6).toList()
    private lateinit var scoreSpinners: List<com.skydoves.powerspinner.PowerSpinnerView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initScoreSpinners()
        setButtonsOnClickListener()
    }

    override fun onPause() {
        super.onPause()
        closeAllSpinners()
    }

    private fun initObservers() {
        matchesViewModel.players.observe(viewLifecycleOwner) { playerList ->
            setupSpinners(playerList)
        }
    }

    private fun initScoreSpinners() {
        binding.run {
            scoreSpinners = listOf(
                homeFirstSetSpinner, homeSecondSetSpinner, homeThirdSetSpinner,
                awayFirstSetSpinner, awaySecondSetSpinner, awayThirdSetSpinner,
            )
        }
    }

    private fun setupSpinners(players: List<PlayerRanking>) {
        setupSetsSpinners()
        setupSpinnerListeners()
        setupPlayersSpinners(players)
    }

    private fun setupPlayersSpinners(players: List<PlayerRanking>) {
        val playerItems = listOf(IconSpinnerItem(getString(R.string.select_player))) +
                players.map { IconSpinnerItem(it.player.name) }
        binding.run {
            homePlayerSpinner.apply {
                setSpinnerAdapter(IconSpinnerAdapter(this))
                setItems(playerItems)
                selectItemByIndex(0)
            }
            awayPlayerSpinner.apply {
                setSpinnerAdapter(IconSpinnerAdapter(this))
                setItems(playerItems)
                selectItemByIndex(0)
            }
            homePlayerSpinner.tag = listOf(null) + players
            awayPlayerSpinner.tag = listOf(null) + players
        }
    }

    private fun setupSetsSpinners() {
        scoreSpinners.forEach { spinner ->
            spinner.apply {
                setSpinnerAdapter(IconSpinnerAdapter(this))
                setItems(scores.map { IconSpinnerItem(it.toString()) })
                selectItemByIndex(0)
            }
        }
    }

    private fun setupSpinnerListeners() {
        val onSpinnerItemSelectedListener =
            { _: Int, _: IconSpinnerItem?, _: Int, _: IconSpinnerItem? ->
                updateThirdSetVisibility()
            }
        scoreSpinners[0].setOnSpinnerItemSelectedListener(onSpinnerItemSelectedListener)
        scoreSpinners[3].setOnSpinnerItemSelectedListener(onSpinnerItemSelectedListener)
        scoreSpinners[1].setOnSpinnerItemSelectedListener(onSpinnerItemSelectedListener)
        scoreSpinners[4].setOnSpinnerItemSelectedListener(onSpinnerItemSelectedListener)
    }

    private fun closeAllSpinners() {
        scoreSpinners.forEach { spinner ->
            if (spinner.isShowing) {
                spinner.dismiss()
            }
        }
        binding.run {
            if (homePlayerSpinner.isShowing) {
                homePlayerSpinner.dismiss()
            }
            if (awayPlayerSpinner.isShowing) {
                awayPlayerSpinner.dismiss()
            }
        }
    }

    private fun updateThirdSetVisibility() {
        binding.run {
            val homeWins = listOf(
                scores[scoreSpinners[0].selectedIndex] == 6 && scores[scoreSpinners[0].selectedIndex] > scores[scoreSpinners[3].selectedIndex],
                scores[scoreSpinners[1].selectedIndex] == 6 && scores[scoreSpinners[1].selectedIndex] > scores[scoreSpinners[4].selectedIndex]
            ).count { it }

            val awayWins = listOf(
                scores[scoreSpinners[3].selectedIndex] == 6 && scores[scoreSpinners[3].selectedIndex] > scores[scoreSpinners[0].selectedIndex],
                scores[scoreSpinners[4].selectedIndex] == 6 && scores[scoreSpinners[4].selectedIndex] > scores[scoreSpinners[1].selectedIndex]
            ).count { it }

            if (homeWins == 1 && awayWins == 1) {
                homeThirdSetTextView.visibility = View.VISIBLE
                scoreSpinners[2].visibility = View.VISIBLE
                awayThirdSetTextView.visibility = View.VISIBLE
                scoreSpinners[5].visibility = View.VISIBLE
            } else {
                homeThirdSetTextView.visibility = View.GONE
                scoreSpinners[2].visibility = View.GONE
                awayThirdSetTextView.visibility = View.GONE
                scoreSpinners[5].visibility = View.GONE
            }
        }
    }

    private fun setButtonsOnClickListener() {
        binding.addMatchButton.setOnClickListener {
            addCustomMatch()
        }
    }

    private fun computeScores(): Pair<List<Int>, List<Int>> =
        Pair(
            listOfNotNull(
                scores[scoreSpinners[0].selectedIndex],
                scores[scoreSpinners[1].selectedIndex],
                if (scoreSpinners[2].visibility == View.VISIBLE) {
                    scores[scoreSpinners[2].selectedIndex]
                } else null
            ), listOfNotNull(
                scores[scoreSpinners[3].selectedIndex],
                scores[scoreSpinners[4].selectedIndex],
                if (scoreSpinners[5].visibility == View.VISIBLE) {
                    scores[scoreSpinners[5].selectedIndex]
                } else null
            )
        )

    private fun addCustomMatch() {
        binding.run {
            val (homeScores, awayScores) = computeScores()
            val validationError = matchesViewModel.validateAndAddMatch(
                homePlayerSpinner.tag.safeCast<List<PlayerRanking>>()
                    ?.getOrNull(homePlayerSpinner.selectedIndex),
                awayPlayerSpinner.tag.safeCast<List<PlayerRanking>>()
                    ?.getOrNull(awayPlayerSpinner.selectedIndex),
                tournamentNameEditText.text.toString(),
                homeScores,
                awayScores,
                onSuccess = { successMessage ->
                    findNavController().navigate(R.id.navigate_add_match_to_matches)
                    Toast.makeText(requireContext(), successMessage, Toast.LENGTH_LONG).show()
                },
                onFailure = { failureMessage ->
                    Toast.makeText(requireContext(), failureMessage, Toast.LENGTH_SHORT).show()
                }
            )
            validationError?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inline fun <reified T> Any?.safeCast(): T? = this as? T
}