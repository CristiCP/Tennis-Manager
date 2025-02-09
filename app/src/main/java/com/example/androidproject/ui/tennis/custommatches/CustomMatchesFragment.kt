package com.example.androidproject.ui.tennis.custommatches

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.androidproject.R
import com.example.androidproject.databinding.FragmentCustomMatchesBinding
import com.example.androidproject.ui.BaseFragment
import com.example.androidproject.ui.tennis.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class CustomMatchesFragment : BaseFragment<FragmentCustomMatchesBinding>(
    FragmentCustomMatchesBinding::inflate
) {

    private val viewModel: CustomMatchesViewModel by viewModels()
    private lateinit var customMatchesAdapter: CustomMatchesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
        setButtonsOnClickListener()
    }

    private fun initAdapter() {
        customMatchesAdapter = CustomMatchesAdapter()
        binding.customMatchesRecyclerView.adapter = customMatchesAdapter
    }

    private fun initObservers() {
        binding.run {
            viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
                customMatchesRecyclerView.isVisible = !isLoading
            }
            viewModel.matches.observe(viewLifecycleOwner) { matches ->
                customMatchesAdapter.submitList(matches)
            }
            (requireActivity() as MainActivity).viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
                if (query.isNullOrEmpty()) {
                    viewModel.matches.observe(viewLifecycleOwner) { matches ->
                        customMatchesAdapter.submitList(matches)
                    }
                } else {
                    customMatchesAdapter.submitList(viewModel.filterMatches(query))
                }
            }
        }
    }

    private fun setButtonsOnClickListener() {
        binding.run {
            addMatchFloatingButton.setOnClickListener {
                findNavController().navigate(R.id.navigate_matches_to_add_match)
            }
            filterDateFloatingButton.setOnClickListener {
                showDatePicker()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                viewModel.filterMatchesByDate(selectedDate.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setButton(
            DatePickerDialog.BUTTON_NEUTRAL,
            getString(R.string.clear_filter)
        ) { _, _ ->
            viewModel.loadCustomMatches()
        }
        datePickerDialog.show()
    }
}