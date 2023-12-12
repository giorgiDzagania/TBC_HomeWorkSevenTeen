package com.exercise.tbchomeworkdseventeen.ui.mainpage



import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.exercise.tbchomeworkdseventeen.BaseFragment
import com.exercise.tbchomeworkdseventeen.R
import com.exercise.tbchomeworkdseventeen.UserPreferencesManager
import com.exercise.tbchomeworkdseventeen.data.resource.Resources
import com.exercise.tbchomeworkdseventeen.databinding.FragmentMainPageBinding
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainPageFragment : BaseFragment<FragmentMainPageBinding>(FragmentMainPageBinding::inflate) {
    private val viewModel: MainPageViewModel by viewModels()
    private val userPreferencesManager by lazy {
        UserPreferencesManager(requireContext())
    }

    override fun setUp() {
        loadRegisterPage()
        checkValidation()

        lifecycleScope.launch {
            val savedToken = userPreferencesManager.getToken().firstOrNull()

            if (!savedToken.isNullOrBlank()) {
                findNavController().navigate(R.id.action_mainPageFragment_to_homePageFragment)
            } else {
                collectUserLogInAndMoveToHomePage()
                observeResultFragment()
            }
        }
    }

    private fun observeResultFragment(){
        setFragmentResultListener("REGISTER_USER_RESULT"){_,result->
            val emailResult = result.getString("email","")
            val passwordResult = result.getString("password","")
            binding.email.setText(emailResult)
            binding.password.setText(passwordResult)
        }
    }

    private fun loadRegisterPage() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_registerPageFragment)
        }
    }

    private fun collectUserLogInAndMoveToHomePage(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.token.collect{it->
                    if (it is Resources.SUCCESS){
                        val email = binding.email.text.toString()

                        val tokenResponse = viewModel.token.value
                        if (tokenResponse is Resources.SUCCESS){
                            if (binding.checkBox.isChecked){
                                val token = tokenResponse.data!!.token
                                userPreferencesManager.saveToken(token)
                            }
                        }

                        val bundle = Bundle().apply {
                            putString("user_email", email)
                        }

                        findNavController().navigate(R.id.action_mainPageFragment_to_homePageFragment,bundle)
                        Toast.makeText(context, "Log in  success", Toast.LENGTH_SHORT).show()
                    }else if(it is Resources.ERROR){
                        Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                    }else if(it is Resources.LOADING){
                        if (it.loading){
                            binding.progressBar.visibility = View.VISIBLE
                        }else{
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun checkValidation() {
        binding.btnLogIn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (checkUserInputValidation(email, password)) {
                viewModel.loginUser(email,password)
            }
        }
    }

    private fun checkUserInputValidation(email:String, password: String): Boolean {
        var isValid = false
        if (email.isNotEmpty() && password.isNotEmpty() && isEmailValid(email)) {
            isValid = true
        } else {
            Toast.makeText(context, "Please fill in both email and password.", Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"
        val pattern = Regex(emailRegex)
        return pattern.matches(email)
    }
}

