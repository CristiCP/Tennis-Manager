package com.example.androidproject.ui.tennis.playercontentviewpager.matches

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.androidproject.ui.BaseFragment
import com.example.androidproject.databinding.FragmentPlayerMatchesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerMatchesFragment(
    private val playerId: Int
) : BaseFragment<FragmentPlayerMatchesBinding>(
    FragmentPlayerMatchesBinding::inflate
) {

    private val viewModel: PlayerMatchesViewModel by viewModels()
    private lateinit var matchesAdapter: MatchesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
        viewModel.loadPlayerMatches(playerId)
    }

    private fun initAdapter() {
        matchesAdapter = MatchesAdapter(playerId)
        binding.matchesRecyclerView.adapter = matchesAdapter
    }

    private fun initObservers() {
        binding.run {
            viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
                loadingIndicator.isVisible = isLoading
                matchesRecyclerView.isVisible = !isLoading
            }
            viewModel.matches.observe(viewLifecycleOwner) { matches ->
                matchesAdapter.submitList(matches)
            }
        }
    }
}