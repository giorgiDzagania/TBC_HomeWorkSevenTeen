package com.exercise.tbchomeworkdseventeen.data.network

import com.exercise.tbchomeworkdseventeen.data.models.LoginRequest
import com.exercise.tbchomeworkdseventeen.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("login")
    suspend fun logInUser(@Body request: LoginRequest): Response<LoginResponse>

}