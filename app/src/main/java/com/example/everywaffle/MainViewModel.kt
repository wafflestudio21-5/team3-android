package com.example.everywaffle

import android.content.Context
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.UserApiClient
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
            MyApplication.prefs.setString("userid",signupresponse.userId.toString())
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
            MyApplication.prefs.setString("userid",signinresponse.userId.toString())
            MyApplication.prefs.setString("token",signinresponse.token)
            MyApplication.prefs.setString("id",id)
            MyApplication.prefs.setString("password",pw)
        }
        catch (e:retrofit2.HttpException){
            signinresponse = null
        }
        return signinresponse
    }


    suspend fun updateUserInfo(realname: String, nickname: String, department: String, studentId: Int):UserDetail?{
        var updateUserInforesponse:UserDetail?
        try {
            updateUserInforesponse = api.updateUserInfo(userid = MyApplication.prefs.getString("userid").toInt(), userdetail = UserDetail(realname,nickname,department,studentId))
            MyApplication.prefs.setString("realname",realname)
            MyApplication.prefs.setString("nickname",nickname)
            MyApplication.prefs.setString("department",department)
            MyApplication.prefs.setString("studentid",studentId.toString())

        }
        catch (e:retrofit2.HttpException){
            updateUserInforesponse=null
        }
        return updateUserInforesponse
    }

    suspend fun getUserInfo(userId:Int = MyApplication.prefs.getString("userid").toInt()):GetUserDetail? {
        var getUserInforesponse: GetUserDetail?
        try {
            getUserInforesponse = api.getUserInfo(userid = userId)
            MyApplication.prefs.setString("realname", getUserInforesponse.realName)
            MyApplication.prefs.setString("nickname", getUserInforesponse.nickname)
            MyApplication.prefs.setString("department", getUserInforesponse.department)
            MyApplication.prefs.setString("studentid", getUserInforesponse.studentId.toString())
        } catch (e: retrofit2.HttpException) {
            getUserInforesponse = null
        }
        return getUserInforesponse
    }

    suspend fun changepassword(newpw:String):Int?{
        try{
            Log.d("aaaa",MyApplication.prefs.getString("userid"))
            Log.d("aaaa",MyApplication.prefs.getString("password"))
            Log.d("aaaa",newpw)
            api.changepassword(userid = MyApplication.prefs.getString("userid").toInt(), newpw=newpw)
            MyApplication.prefs.setString("password",newpw)
            return 1
        }
        catch (e:retrofit2.HttpException){
            return null
        }
    }

    fun loginWithKakao(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    onError(error.toString())
                } else if (token != null) {
                    onSuccess()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    onError(error.toString())
                } else if (token != null) {
                    onSuccess()
                }
            }
        }
    }
}
