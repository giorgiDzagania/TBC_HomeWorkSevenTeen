package com.exercise.tbchomeworkdseventeen.ui.registerpage


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.exercise.tbchomeworkdseventeen.BaseFragment
import com.exercise.tbchomeworkdseventeen.R
import com.exercise.tbchomeworkdseventeen.data.resource.Resources
import com.exercise.tbchomeworkdseventeen.databinding.FragmentRegisterPageBinding
import kotlinx.coroutines.launch

class RegisterPageFragment : BaseFragment<FragmentRegisterPageBinding>(FragmentRegisterPageBinding::inflate) {
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun setUp() {
        checkValidation()
        collectUserRegister()
    }

    private fun collectUserRegister(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                registerViewModel.registerToken.collect{
                    if (it is Resources.SUCCESS){

                        val email = binding.email.text.toString()
                        val password = binding.password.text.toString()
                        val result = Bundle().apply {
                            putString("email", email)
                            putString("password", password)
                        }
                        setFragmentResult("REGISTER_USER_RESULT",result)

                        findNavController().navigate(R.id.action_registerPageFragment_to_mainPageFragment)
                        Toast.makeText(context, "Register completed", Toast.LENGTH_SHORT).show()
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
        binding.registerBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val repeatPassword = binding.repeatPassword.text.toString()
            if (checkUserInputValidation(email, password, repeatPassword)) {
                registerViewModel.registerUser(email,password)
            }
        }
    }

    private fun checkUserInputValidation(email:String, password: String, repeatPassword: String): Boolean {
        var isValid = false
        if (email.isNotEmpty() && password.isNotEmpty() && isEmailValid(email) && password == repeatPassword) {
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