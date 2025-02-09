package com.example.androidproject.ui.tennis.tennisplayers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.androidproject.ui.BaseFragment
import com.example.androidproject.databinding.FragmentPlayersPageBinding
import com.example.androidproject.ui.tennis.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TennisPlayersPageFragment : BaseFragment<FragmentPlayersPageBinding>(
    FragmentPlayersPageBinding::inflate
) {

    private val viewModel: TennisPlayersViewModel by viewModels()
    private lateinit var adapter: TennisPlayerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
    }

    private fun initAdapter() {
        adapter = TennisPlayerAdapter { player ->
            findNavController().navigate(
                TennisPlayersPageFragmentDirections.navigationMainToDetails(
                    player.player.id
                )
            )
        }
        binding.tennisPlayersRecyclerView.adapter = adapter
    }

    private fun initObservers() {
        (requireActivity() as MainActivity).viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            if (query.isNullOrEmpty()) {
                viewModel.players.observe(viewLifecycleOwner) { players ->
                    adapter.submitList(players)
                }
            } else {
                adapter.submitList(viewModel.filterPlayers(query))
            }
        }
    }
}
