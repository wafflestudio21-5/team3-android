package com.example.everywaffle

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: RestAPI
):ViewModel(){
    suspend fun signup(id:String, pw:String, email:String):SignupResponse?{
        var signupresponse:SignupResponse?
        try {
            signupresponse= api.signup(SignupRequest(id,pw,email))
        }
        catch (e:retrofit2.HttpException){
            signupresponse= null
        }
        return signupresponse
    }

    suspend fun signin(id:String, pw:String):SigninResponse?{
        var signinresponse:SigninResponse?
        try {
            signinresponse = api.signin(SigninRequest(id, pw))
            Log.d("aaaa", "success")
        }
        catch (e:retrofit2.HttpException){
            signinresponse = null
            Log.d("aaaa", "fail")
        }
        return signinresponse
    }
}