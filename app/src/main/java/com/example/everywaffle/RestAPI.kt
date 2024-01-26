package com.example.everywaffle

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestAPI {
    @POST("/api/signup")
    suspend fun signup(
        @Body() signup:SignupRequest
    ):SignupResponse

    @POST("/api/signin")
    suspend fun signin(
        @Body() signin:SigninRequest
    ):SigninResponse

    @POST("/api/details/{id}")
    suspend fun updateUserInfo(
        @Path("id") userid:Int,
        @Body() userdetail:UserDetail,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token")
    ):UserDetail

    @GET("/api/details/{id}")
    suspend fun getUserInfo(
        @Path("id") userid:Int,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token")
    ):GetUserDetail

    @POST("/api/details/{id}/change-passowrd")
    suspend fun changepassword(
        @Path("id") userid:Int,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Query("newPassword") newpw:String,
        @Body() empty: Any =Object()
    ): Response<Void>

    @POST("/api/details/{id}/change-email")
    suspend fun changemail(
        @Path("id") userid:Int,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Query("newEmail") newmail:String,
        @Body() empty: Any =Object()
    ): Response<Void>

    @GET("/api/post/category/{category}")
    suspend fun getpostcategory(
        @Path("category") boardid:String,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Query("page") page:Int,
        @Query("size") size:Int
    ): List<PostDetail>

    @GET("/api/post/{postId}")
    suspend fun getpost(
        @Path("postId") postid:Int,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token")
    ): PostDetail

    @GET("/api/post/{postId}/comments")
    suspend fun getcomments(
        @Path("postId") postid:Int,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token")
    ): List<ParentComment>

    @POST("/api/post/{postId}/likes")
    suspend fun postlike(
        @Path("postId") postid:Int,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Body() userid:UserId
    )

    @POST("/api/comment")
    suspend fun postcomment(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Body() comment:PostComment
    ): ChildComment

    @POST("/api/post/{postId}/scrap")
    suspend fun postscrap(
        @Path("postId") postid:Int,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Body() userid:UserId
    ): PostScrapResponse

    @GET("/api/home/post/{category}")
    suspend fun getrecent(
        @Path("category") category:String,
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token")
    ): PostDetail

    @GET("/api/home/post-trending")
    suspend fun gettrending(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token")
    ): List<PostDetail>

    @POST("/api/post")
    suspend fun createpost(
        @Header("Authorization")token:String = "Bearer" + MyApplication.prefs.getString("token"),
        @Body() postRequest: PostRequest
    ):PostResponse
}