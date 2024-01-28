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
    @Json(name = "userId") val userId:Int,
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

@JsonClass(generateAdapter = true)
data class UserDetail(
    @Json(name = "realName") val realName:String,
    @Json(name = "nickname") val nickname:String,
    @Json(name = "department") val department:String,
    @Json(name = "studentId") val studentId:Int
)

@JsonClass(generateAdapter = true)
data class GetUserDetail(
    @Json(name = "userId") val userId:Int,
    @Json(name = "realName") val realName:String,
    @Json(name = "nickname") val nickname:String,
    @Json(name = "department") val department:String,
    @Json(name = "studentId") val studentId:Int
)

@JsonClass(generateAdapter = true)
data class PostDetail(
    @Json(name = "id") val postId:Int,
    @Json(name = "userId") val userId:Int,
    @Json(name = "title") val title:String,
    @Json(name = "content") val content:String,
    @Json(name = "category") val category:String,
    @Json(name = "createdAt") val createdAt:String,
    @Json(name = "likes") val likes:Int,
    @Json(name = "scraps") val scraps:Int,
    @Json(name = "comments") val comments:Int
)

@JsonClass(generateAdapter = true)
data class ChildComment(
    @Json(name = "id") val childcommentid:Int,
    @Json(name = "userId") val userId:Int,
    @Json(name = "postId") val postId:Int,
    @Json(name = "content") val content:String,
    @Json(name = "parentCommentId") val parentCommentId:Int?,
    @Json(name = "createdAt") val createdAt:String,
    @Json(name = "likes") val likes:Int
)

@JsonClass(generateAdapter = true)
data class ParentComment(
    @Json(name = "id") val parentcommentid:Int,
    @Json(name = "userId") val userId:Int,
    @Json(name = "postId") val postId:Int,
    @Json(name = "content") val content:String,
    @Json(name = "createdAt") val createdAt:String,
    @Json(name = "childComments") val childComments:List<ChildComment>,
    @Json(name = "likes") val likes:Int
)

@JsonClass(generateAdapter = true)
data class UserId(
    @Json(name = "userId") val userId:Int
)

@JsonClass(generateAdapter = true)
data class PostComment(
    @Json(name = "userId") val userid:Int,
    @Json(name = "postId") val postid:Int,
    @Json(name = "content") val content:String,
    @Json(name = "parentCommentId") val parentcommentid:Int,
    @Json(name = "likes") val likes:Int
)

@JsonClass(generateAdapter = true)
data class PostScrapResponse(
    @Json(name = "scrapId") val scrapid:Int,
    @Json(name = "userId") val userid:Int,
    @Json(name = "postId") val postId:Int
)

@JsonClass(generateAdapter = true)
data class PostRequest(
    @Json(name = "userId") val userId:Int,
    @Json(name = "title") val title:String,
    @Json(name = "content") val content:String,
    @Json(name = "category") val category:String
)
