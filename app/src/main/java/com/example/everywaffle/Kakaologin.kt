package com.example.everywaffle

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Kakaologin {

    suspend fun loginWithKakao(context: Context): OAuthToken {
        return if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            try {
                loginWithKakaoTalk(context)
            } catch (e: ClientError) {
                if (e.reason == ClientErrorCause.Cancelled) {
                    throw e
                } else {
                    loginWithKakaoAccount(context)
                }
            } catch (e: Throwable) {
                loginWithKakaoAccount(context)
            }
        } else {
            loginWithKakaoAccount(context)
        }
    }

    private suspend fun loginWithKakaoTalk(context: Context): OAuthToken {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                when {
                    error != null -> continuation.cancel(error)
                    token != null -> continuation.resume(token)
                    else -> continuation.cancel(RuntimeException("Fail to access kakao"))
                }
            }
        }
    }

    private suspend fun loginWithKakaoAccount(context: Context): OAuthToken {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                when {
                    error != null -> continuation.cancel(
                        CancellationException(
                            "Kakao login failed",
                            error
                        )
                    )

                    token != null -> continuation.resume(token)
                    else -> continuation.cancel(RuntimeException("Fail to access Kakao."))
                }
            }
            continuation.invokeOnCancellation {}
        }
    }

    suspend fun getKakaoOAuthToken(context: Context): OAuthToken? {
        return runCatching {
            loginWithKakao(context)
        }.onSuccess { oAuthToken ->
            Log.i("getKakaoOAuthToken", oAuthToken.toString())
            return oAuthToken
        }.onFailure { error ->
            when (error) {
                is ClientError -> {
                    if (error.reason == ClientErrorCause.Cancelled) {
                        Log.i("getKakaoOAuthToken", "사용자가 명시적으로 취소")
                    } else {
                        Log.e("getKakaoOAuthToken", "인증 에러 발생", error)
                    }
                }

                else -> Log.e("getKakaoOAuthToken", "인증 에러 발생", error)
            }
            return null
        }.getOrNull()
    }

    suspend fun checkUserData(context: Context) {
        suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                    Log.e("checkUserData", "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    continuation.resume(Unit)
                    Log.e("checkUserData", "사용자 정보 요청 성공 : $user")
                    user.kakaoAccount?.profile?.nickname?.let { nickname ->
                        Log.d("checkUserData", "닉네임: $nickname")
                    }
                    Log.d("checkUserData", "회원번호: ${user.id}")
                }
            }
        }
    }

    suspend fun loginWithTokenVerification(context: Context) {
        if (AuthApiClient.instance.hasToken()) {
            try {
                val tokenInfo = suspendCancellableCoroutine { continuation ->
                    UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                        if (error != null) {
                            continuation.resumeWithException(error)
                        } else {
                            continuation.resume(tokenInfo)
                        }
                    }
                }
                if (tokenInfo != null) {
                    Log.i(
                        "토큰 인포 체크", "토큰 정보 보기 성공" +
                                "\n토큰인포: $tokenInfo" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초"
                    )
                }
            } catch (error: Throwable) {
                if (error is KakaoSdkError && error.isInvalidTokenError()) {
                    Log.e("토큰 인포 체크", "토큰 유효하지 않음: 로그인 필요", error)
                } else {
                    Log.e("토큰 인포 체크", "기타 에러 발생", error)
                }
            }
        } else {
            Log.i("토큰 인포 체크", "토큰 없음: 로그인 필요")
        }
    }
}