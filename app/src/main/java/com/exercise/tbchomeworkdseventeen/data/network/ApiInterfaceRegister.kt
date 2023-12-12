package com.exercise.tbchomeworkdseventeen.data.network


import com.exercise.tbchomeworkdseventeen.data.models.RegisterRequest
import com.exercise.tbchomeworkdseventeen.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterfaceRegister {

    @POST("register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

}