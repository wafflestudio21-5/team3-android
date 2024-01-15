package com.example.everywaffle

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: RestAPI
):ViewModel(){

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo
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

    fun updateUserInfo(name: String, nickname: String, department: String, studentId: String){
        _userInfo.value = UserInfo(name, nickname, department, studentId)
    }
}

data class UserInfo(
    val name: String,
    val nickname: String,
    val department: String,
    val studentId: String
)