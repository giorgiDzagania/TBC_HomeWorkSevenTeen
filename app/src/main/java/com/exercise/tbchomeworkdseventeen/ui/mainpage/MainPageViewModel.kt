package com.exercise.tbchomeworkdseventeen.ui.mainpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.tbchomeworkdseventeen.data.models.LoginRequest
import com.exercise.tbchomeworkdseventeen.data.models.LoginResponse
import com.exercise.tbchomeworkdseventeen.data.network.ApiClient
import com.exercise.tbchomeworkdseventeen.data.resource.Resources
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class MainPageViewModel: ViewModel() {
    private val _token: MutableStateFlow<Resources<LoginResponse>?> = MutableStateFlow(null)
    val token: StateFlow<Resources<LoginResponse>?> get()  = _token

    fun loginUser(userEmail: String, userPassword: String) {
        viewModelScope.launch {
            _token.value = Resources.LOADING(true)
            try {
                val response = ApiClient.networkApiService.logInUser(LoginRequest(userEmail, userPassword))
                if (response.isSuccessful) {
                    _token.value = Resources.SUCCESS(
                        data = response.body()!!
                    )
                }else{
                    _token.value = Resources.ERROR(
                        message = response.errorBody()?.string() ?: "Error"
                    )
                }
            } catch (e: Exception) {
                Log.e("MainPageViewModel", "Check internet connection: ${e.message}")
            }
            delay(1000L)
            _token.value = Resources.LOADING(false)
        }
    }

}