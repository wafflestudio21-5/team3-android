package com.example.everywaffle

import android.content.Context
import android.util.Log
import androidx.compose.runtime.remember
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: RestAPI
):ViewModel(){
    companion object{
        val _postchanged: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val postchanged = _postchanged.asStateFlow()
    }
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
            MyApplication.prefs.setString("mail",signinresponse.email)
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
        Log.d("aaaa",updateUserInforesponse.toString())
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

    suspend fun changemail(newmail:String):Int?{
        try{
            api.changemail(userid = MyApplication.prefs.getString("userid").toInt(), newmail = newmail)
            MyApplication.prefs.setString("mail",newmail)
            return 1
        }
        catch (e:retrofit2.HttpException){
            return null
        }
    }

    suspend fun getpostcategory(boardid:String, page:Int, size:Int=10, keyword:String=""):List<PostDetail>?{
        var getPostCategory:List<PostDetail>?
        try{
            getPostCategory = when {
                boardid=="myposts" -> api.getmyposts(
                    userid = MyApplication.prefs.getString("userid").toInt(),
                    page = page,
                    size = size
                ).content
                boardid=="mycommented" -> api.getmycommented(
                    userid = MyApplication.prefs.getString("userid").toInt(),
                    page = page,
                    size = size
                ).content
                boardid=="myscrapped" -> api.getmyscrapped(
                    userid = MyApplication.prefs.getString("userid").toInt(),
                    page = page,
                    size = size
                ).content
                "whole" in boardid -> api.searchwhole(
                    keyword = keyword,
                    page = page,
                    size = size
                )
                "Search" in boardid -> api.searchcategory(
                    category = boardid.substring(7),
                    keyword = keyword,
                    page = page,
                    size = size
                )
                else -> api.getpostcategory(boardid = boardid, page = page, size = size)
            }
        }
        catch (e: retrofit2.HttpException){
            Log.d("aaaa",e.toString())
            getPostCategory=null
        }
        return getPostCategory
    }

    suspend fun getpost(postid:Int):PostDetail?{
        var getPost:PostDetail?
        try {
            getPost = api.getpost(postid = postid)
        }
        catch (e: retrofit2.HttpException){
            getPost=null
        }
        return getPost
    }

    suspend fun getcomments(postid:Int):List<ParentComment>?{
        var getComments:List<ParentComment>?
        try{
            getComments = api.getcomments(postid)
        }
        catch (e: retrofit2.HttpException){
            getComments = null
        }
        return getComments
    }

    suspend fun postlike(postid:Int):Int? {
        try {
            api.postlike(
                postid = postid,
                userid = UserId(userId = MyApplication.prefs.getString("userid").toInt())
            )
            return 1
        }
        catch (e: retrofit2.HttpException){
            return null
        }
    }

    suspend fun postcomment(comment: PostComment):ChildComment?{
        var postComment:ChildComment?
        try{
            postComment = api.postcomment(comment = comment)
        }
        catch (e: retrofit2.HttpException){
            postComment = null
        }
        return postComment
    }

    suspend fun postscrap(postid:Int):PostScrapResponse? {
        var postScrap:PostScrapResponse?
        try{
            postScrap = api.postscrap(postid = postid,
                userid = UserId(userId = MyApplication.prefs.getString("userid").toInt()))
        }
        catch (e: retrofit2.HttpException){
            postScrap = null
        }
        return postScrap
    }

    suspend fun getrecent(category:String):PostDetail? {
        var getRecent:PostDetail?
        try{
            getRecent = api.getrecent(category = category)
        }
        catch (e: retrofit2.HttpException){
            getRecent = null
        }
        return getRecent
    }

    suspend fun gettrending():List<PostDetail>? {
        var gettrending:List<PostDetail>?
        try {
            gettrending = api.gettrending()
        }
        catch (e: retrofit2.HttpException){
            gettrending = null
        }
        return gettrending
    }


    suspend fun createpost(title: String, content: String, category:String): PostDetail? {
        return try {
            val topost = PostRequest(
                userId = MyApplication.prefs.getString("userid").toInt(),
                title = title,
                content = content,
                category = category
            )
            api.createpost(postRequest = topost)
        } catch (e: retrofit2.HttpException) {
            Log.d("aaaa",e.toString())
            null
        }
    }

    suspend fun updatepost(title: String, content: String, category:String, postid:Int): PostDetail? {
        return try {
            val topost = PostRequest(
                userId = MyApplication.prefs.getString("userid").toInt(),
                title = title,
                content = content,
                category = category
            )
            api.updatepost(postid = postid, postRequest = topost)
        } catch (e: retrofit2.HttpException) {
            Log.d("aaaa",e.toString())
            null
        }
    }

    suspend fun deletepost(postid:Int):Int?{
        try {
            api.deletepost(
                postid = postid
            )
            return 1
        }
        catch (e: retrofit2.HttpException){
            Log.d("aaaa",e.toString())
            return null
        }
    }
}
