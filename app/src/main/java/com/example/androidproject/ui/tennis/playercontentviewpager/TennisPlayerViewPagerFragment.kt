package com.example.androidproject.ui.tennis.playercontentviewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.androidproject.ui.BaseFragment
import com.example.androidproject.R
import com.example.androidproject.api.ApiUtils
import com.example.androidproject.core.domain.PlayerData
import com.example.androidproject.databinding.FragmentTennisPlayerViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class TennisPlayerViewPagerFragment : BaseFragment<FragmentTennisPlayerViewPagerBinding>(
    FragmentTennisPlayerViewPagerBinding::inflate
) {

    private val viewModel: TennisPlayerViewPagerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
        setButtonsOnClickListener()
        arguments?.getInt(getString(R.string.player_id))?.let { viewModel.setPlayer(it) }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.player_details)
                1 -> getString(R.string.player_matches)
                else -> null
            }
        }.attach()
    }

    private fun initAdapter() {
        val playerId = arguments?.getInt(getString(R.string.player_id)) ?: return
        val viewPagerAdapter = PlayerDetailsPagerAdapter(this, playerId)
        binding.viewPager.adapter = viewPagerAdapter
    }

    private fun initObservers() {
        viewModel.player.observe(viewLifecycleOwner) { player ->
            player?.let { bindPlayerDetails(it) }
        }
    }

    private fun setButtonsOnClickListener() {
        binding.run {
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun bindPlayerDetails(player: PlayerData) {
        binding.run {
            Glide.with(root.context)
                .load(ApiUtils.BASE_PLAYER_IMAGE_URL + player.player.id + ApiUtils.IMAGE)
                .into(profileImageView)
            playerNameTextView.text = player.player.fullName
            Glide.with(root.context).load(
                ApiUtils.BASE_COUNTRY_IMAGE_URL + (player.player.country?.alpha2?.lowercase(
                    Locale.ROOT
                )
                    ?: ApiUtils.DEFAULT_IMAGE) + ApiUtils.IMAGE_FORMAT
            ).into(countryImageView)
            countryTextView.text = player.player.country?.name
        }
    }
}
