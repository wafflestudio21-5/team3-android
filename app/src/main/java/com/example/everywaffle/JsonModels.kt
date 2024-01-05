package com.example.everywaffle

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupRequest(
    @Json(name = "userName") val userName:String,
    @Json(name = "password") val password:String,
    @Json(name = "email") val email:String
)

@JsonClass(generateAdapter = true)
data class SignupResponse(
    @Json(name = "userName") val userName:String,
    @Json(name = "email") val email:String,
    @Json(name = "token") val token:String
)

@JsonClass(generateAdapter = true)
data class SigninRequest(
    @Json(name = "userName") val userName:String,
    @Json(name = "password") val password:String
)

@JsonClass(generateAdapter = true)
data class SigninResponse(
    @Json(name = "userId") val userId:Int,
    @Json(name = "userName") val userName:String,
    @Json(name = "email") val email:String,
    @Json(name = "token") val token:String
)