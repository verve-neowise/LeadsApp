package com.neowise.leads.ui.screen.main

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.neowise.leads.R
import com.neowise.leads.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding()

    override fun onStart() {
        super.onStart()

        val navController = binding.navHostFragment.findNavController()

        binding.bottomNavigation.setupWithNavController(
            navController = navController
        )
    }
}