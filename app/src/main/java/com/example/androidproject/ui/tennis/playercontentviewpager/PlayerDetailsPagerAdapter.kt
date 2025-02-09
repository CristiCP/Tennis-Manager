package com.example.androidproject.ui.tennis.playercontentviewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androidproject.ui.tennis.playercontentviewpager.matches.PlayerMatchesFragment
import com.example.androidproject.ui.tennis.playercontentviewpager.playerdetails.TennisPlayerDetailsFragment

class PlayerDetailsPagerAdapter (
    fragment: Fragment,
    private val playerId: Int
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> TennisPlayerDetailsFragment(playerId)
        1 -> PlayerMatchesFragment(playerId)
        else -> throw IllegalArgumentException("$position")
    }
}