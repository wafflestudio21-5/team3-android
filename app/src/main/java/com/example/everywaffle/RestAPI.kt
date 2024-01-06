package com.example.everywaffle

import retrofit2.http.Body
import retrofit2.http.POST

interface RestAPI {
    @POST("/api/signup")
    suspend fun signup(
        @Body() signup:SignupRequest
    ):SignupResponse

    @POST("/api/signin")
    suspend fun signin(
        @Body() signin:SigninRequest
    ):SigninResponse
}