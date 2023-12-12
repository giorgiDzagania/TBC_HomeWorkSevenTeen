package com.exercise.tbchomeworkdseventeen.ui.registerpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.tbchomeworkdseventeen.data.models.RegisterRequest
import com.exercise.tbchomeworkdseventeen.data.models.RegisterResponse
import com.exercise.tbchomeworkdseventeen.data.network.ApiClientRegister
import com.exercise.tbchomeworkdseventeen.data.resource.Resources
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val _registerToken: MutableStateFlow<Resources<RegisterResponse>?> = MutableStateFlow(null)
    val registerToken: StateFlow<Resources<RegisterResponse>?> get()  = _registerToken

    fun registerUser(userEmail: String, userPassword: String) {
        viewModelScope.launch {
            _registerToken.value = Resources.LOADING(true)
            try {
                val response = ApiClientRegister.networkApiService.registerUser(RegisterRequest(userEmail, userPassword))
                if (response.isSuccessful) {
                    _registerToken.value = Resources.SUCCESS(
                        data = response.body()!!
                    )
                }else{
                    _registerToken.value = Resources.ERROR(
                        message = response.errorBody()?.string() ?: "Error"
                    )
                }
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Check internet connection: ${e.message}")
            }
            delay(1000L)
            _registerToken.value = Resources.LOADING(false)
        }
    }

}