package com.example.everywaffle

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: RestAPI
):ViewModel(){
    suspend fun signup(id:String, pw:String, email:String){
        val signupresponse = api.signup(SignupRequest(id,pw,email))
    }

    suspend fun signin(id:String, pw:String){
        val signinresponse = api.signin(SigninRequest(id,pw))
        Log.d("aaaa",signinresponse.toString())
    }
}