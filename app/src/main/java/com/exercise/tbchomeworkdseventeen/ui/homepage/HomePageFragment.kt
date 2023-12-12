package com.exercise.tbchomeworkdseventeen.ui.homepage

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.exercise.tbchomeworkdseventeen.BaseFragment
import com.exercise.tbchomeworkdseventeen.R
import com.exercise.tbchomeworkdseventeen.UserPreferencesManager
import com.exercise.tbchomeworkdseventeen.databinding.FragmentHomePageBinding
import kotlinx.coroutines.launch

class HomePageFragment: BaseFragment<FragmentHomePageBinding>(FragmentHomePageBinding::inflate) {

    private val userPreferencesManager by lazy {
        UserPreferencesManager(requireContext())
    }

    override fun setUp() {
        showResultEmail()
        goToMainPage()
    }

    private fun showResultEmail() {
        viewLifecycleOwner.lifecycleScope.launch {
            userPreferencesManager.getEmail().collect { userEmail ->
                userEmail?.let {
                    binding.emailResult.text = it
                }
            }
        }
    }

    private fun goToMainPage() {
        binding.btnLogOut.setOnClickListener {
            lifecycleScope.launch {
                userPreferencesManager.clearUser()
                findNavController().navigate(R.id.action_homePageFragment_to_mainPageFragment)
            }
        }
    }

}