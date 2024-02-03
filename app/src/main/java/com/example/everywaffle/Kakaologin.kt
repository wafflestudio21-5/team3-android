
package com.example.everywaffle

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaologinActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_kakaologin) // XML 레이아웃을 사용하는 경우


        val btnKakao: Button = findViewById(R.id.btnKakao)
        btnKakao.setOnClickListener {
            loginWithKakao()
        }

    }

    fun loginWithKakao() {
        // 카카오톡 로그인 가능 여부 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            // 카카오톡으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                handleLoginResult(token, error)
            }
        } else {
            // 카카오계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                handleLoginResult(token, error)
            }
        }
    }

    fun handleLoginResult(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                Log.i("LOGIN", "카카오톡 로그인이 취소되었습니다.")
                return
            } else {
                Log.e("LOGIN", "카카오계정으로 로그인 실패", error)
            }
        } else if (token != null) {
            Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")
            navigateToHomeScreen()
        }
    }

    fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigateTo", "Home")
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }
}
