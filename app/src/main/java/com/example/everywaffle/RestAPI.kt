package com.example.everywaffle

import android.content.pm.PackageInstaller
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("id") userid:Int
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
        @Header("Authorization") token:String = "Bearer " + MyApplication.prefs.getString("token"),
        @Body() postRequest: PostRequest
    ):PostDetail

    @PUT("/api/post/{postId}")
    suspend fun updatepost(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("postId") postid:Int,
        @Body() postRequest: PostRequest
    ):PostDetail

    @DELETE("/api/post/{postId}")
    suspend fun deletepost(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("postId") postid:Int
    ):Response<Unit>

    @GET("/api/post/myposts")
    suspend fun getmyposts(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Query("user") userid:Int,
        @Query("page") page:Int,
        @Query("size") size:Int
    ):MypostResponse

    @GET("/api/post/mycommented")
    suspend fun getmycommented(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Query("user") userid:Int,
        @Query("page") page:Int,
        @Query("size") size:Int
    ):MypostResponse

    @GET("/api/post/myscrapped")
    suspend fun getmyscrapped(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Query("user") userid:Int,
        @Query("page") page:Int,
        @Query("size") size:Int
    ):MypostResponse

    @GET("/api/post/search")
    suspend fun searchwhole(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Query("keyword") keyword:String,
        @Query("page") page:Int,
        @Query("size") size:Int
    ):List<PostDetail>

    @GET("/api/post/search/{category}")
    suspend fun searchcategory(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("category") category:String,
        @Query("keyword") keyword:String,
        @Query("page") page:Int,
        @Query("size") size:Int
    ):List<PostDetail>

    @POST("/api/comment/{commentId}/likes")
    suspend fun postcommentlike(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("commentId") commentId:Int,
        @Body() userid:UserId
    ):Response<Unit>

    @DELETE("/api/comment/{commentId}")
    suspend fun deletecomment(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("commentId") commentId:Int
    ):Response<Unit>

    @POST("/api/post/{postId}/makeVote")
    suspend fun postmakevote(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("postId") postId:Int,
        @Body() userid:UserId
    ):MakeVoteDetail

    @GET("/api/post/vote-board")
    suspend fun getpostvote(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Query("page") page:Int,
        @Query("size") size:Int
    ):List<PostDetail>

    @POST("/api/post/{postId}/vote")
    suspend fun postvote(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("postId") postId:Int,
        @Body() vote:Vote
    ):VoteResult

    @GET("/api/messages/sessions/{userId}")
    suspend fun getsessions(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("userId") userId:Int
    ):List<SessionDetail>

    @GET("/api/messages/session/{sessionId}")
    suspend fun getmessages(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Path("sessionId") sessionId:Int
    ):List<MessageDetail>

    @POST("/api/messages/send")
    suspend fun sendmessage(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Body() messagebody:SendMessage
    ):MessageDetail

    @POST("/api/messages/random")
    suspend fun sendmessagerandom(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Body() messagebody:SendMessage
    ):MessageDetail

    @POST("/api/messages/session")
    suspend fun makesession(
        @Header("Authorization") token:String="Bearer "+MyApplication.prefs.getString("token"),
        @Body() users:UserId2
    ):Int
}