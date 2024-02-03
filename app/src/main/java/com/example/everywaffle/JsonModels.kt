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
    @Json(name = "comments") val comments:Int,
    @Json(name = "isVoting") val isVoting:Boolean,
    @Json(name = "makeVoteCnt") val makeVoteCnt:Int,
    @Json(name = "agree") val agree:Int,
    @Json(name = "disagree") val disagree:Int
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
data class UserId2(
    @Json(name = "user1Id") val user1Id:Int,
    @Json(name = "user2Id") val user2Id:Int
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

@JsonClass(generateAdapter = true)
data class MypostResponse(
    @Json(name = "content") val content:List<PostDetail>,
    @Json(name = "pageable") val pageable:Pageable,
    @Json(name = "last") val last:Boolean,
    @Json(name = "totalPages") val totalPages:Int,
    @Json(name = "totalElements") val totalElements:Int,
    @Json(name = "size") val size:Int,
    @Json(name = "number") val number:Int,
    @Json(name = "sort") val sort:Sort,
    @Json(name = "first") val first:Boolean,
    @Json(name = "numberOfElements") val numberOfElements:Int,
    @Json(name = "empty") val empty:Boolean
)

@JsonClass(generateAdapter = true)
data class Pageable(
    @Json(name = "pageNumber") val pageNumber:Int,
    @Json(name = "pageSize") val pageSize:Int,
    @Json(name = "sort") val sort:Sort,
    @Json(name = "offset") val offset:Int,
    @Json(name = "paged") val paged:Boolean,
    @Json(name = "unpaged") val unpaged:Boolean
)

@JsonClass(generateAdapter = true)
data class Sort(
    @Json(name = "empty") val empty:Boolean,
    @Json(name = "sorted") val sorted:Boolean,
    @Json(name = "unsorted") val unsorted:Boolean
)

@JsonClass(generateAdapter = true)
data class MakeVoteDetail(
    @Json(name = "userId") val userid:Int,
    @Json(name = "postId") val postid:Int,
    @Json(name = "currentMakeVoteCnt") val makevotecnt:Int
)

@JsonClass(generateAdapter = true)
data class Vote(
    @Json(name = "userId") val userid:Int,
    @Json(name = "vote") val vote:String
)

@JsonClass(generateAdapter = true)
data class VoteResult(
    @Json(name = "userId") val userId:Int,
    @Json(name = "postId") val postId:Int,
    @Json(name = "currentAgreeCnt") val currentAgreeCnt:Int,
    @Json(name = "currentDisagreeCnt") val currentDisagreeCnt:Int,
)

@JsonClass(generateAdapter = true)
data class SessionDetail(
    @Json(name = "sessionId") val sessionId:Int,
    @Json(name = "user1Id") val user1Id:Int,
    @Json(name = "user2Id") val user2Id:Int,
    @Json(name = "lastMessage") val lastMessage:MessageDetail
)

@JsonClass(generateAdapter = true)
data class MessageDetail(
    @Json(name = "messageId") val messageId:Int,
    @Json(name = "sessionId") val sessionId:Int,
    @Json(name = "senderId") val senderId:Int,
    @Json(name = "content") val content:String,
    @Json(name = "createdAt") val createdAt:String
)

@JsonClass(generateAdapter = true)
data class SendMessage(
    @Json(name = "sessionId") val sessionId:Int,
    @Json(name = "senderId") val senderId:Int,
    @Json(name = "content") val content:String
)

@JsonClass(generateAdapter = true)
data class Password(
    @Json(name = "password") val password:String
)