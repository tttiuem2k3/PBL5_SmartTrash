package com.example.trashmobie.Presenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.trashmobie.Model.ApiService.apiService
import com.example.trashmobie.Model.Model
import com.example.trashmobie.databinding.LoginLayoutBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


private lateinit var binding: LoginLayoutBinding
private var isResponseReceived = false
class LoginAtivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginLayoutBinding.inflate(layoutInflater)
        if (getUserInfo(this)) {
            createintern()
//            finish()
        } else {
            // Hiển thị màn hình đăng nhập
            setContentView(binding.root)
        }


        binding.btnLogin.setOnClickListener {
            val username = binding.username.text
            val password = binding.pass.text
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val userLogindata = userLogin(
                    username.toString(),
                    password.toString()
                )
                apiService.loginApi(userLogindata)
                    .enqueue(object : retrofit2.Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            // Xử lý khi nhận được phản hồi thành công từ máy chủ

                            if (response.isSuccessful) {
                                val loginResponse = response.body()
                                val token = loginResponse?.token

                                // Phân tích chuỗi token thành một đối tượng JSON
                                val tokenParts = token.toString().split(".")
                                val payloadJson =
                                    String(Base64.decode(tokenParts[1], Base64.DEFAULT))
                                val jsonObject = JSONObject(payloadJson)

                                // Trích xuất các thông tin từ token
                                val userId = jsonObject.getInt("user_id")
                                val expirationTime = jsonObject.getLong("exp")
                                Model.user_name = username.toString()
                                Model.token_login = token.toString()
                                Model.user_id = userId.toString().toUpperCase()
                                Model.exp = expirationTime.toString()
                                saveUserInfo(this@LoginAtivity, username.toString(), token.toString(), userId.toString().toUpperCase(), expirationTime.toString())

                                Toast.makeText(
                                    this@LoginAtivity,
                                    "Đăng nhập thành công",
                                    Toast.LENGTH_LONG
                                ).show()
                                createintern()
                            } else {
                                Toast.makeText(
                                    this@LoginAtivity,
                                    "Sai thông tin đăng nhập",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(this@LoginAtivity, "Lỗi mịa rồi", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
            else {
                Toast.makeText(
                    this@LoginAtivity,
                    "Vui lòng điền đầy đủ!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.dangky.setOnClickListener {
            val intentreg = Intent(this, RegisterAtivity::class.java)
            startActivity(intentreg)
        }
        binding.quenmk.setOnClickListener {
            Toast.makeText(this@LoginAtivity,"Sử dụng số điện thoại đăng ký của quý khách nhắn tin với cú pháp sau để được cấp lại mật khẩu:\nMK gửi 0329966939\nXIN CẢM ƠN",
                Toast.LENGTH_LONG).show()
        }
    }
    private fun getUserInfo(context: Context):Boolean {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("userName", null)
        val token = sharedPreferences.getString("token", null)
        val userId = sharedPreferences.getString("userId", null)
        val expirationTime = sharedPreferences.getString("expirationTime", null)

        if (username != null && token != null && userId != null && expirationTime != null) {
            Model.user_name = username
            Model.token_login = token
            Model.user_id = userId
            Model.exp = expirationTime
            return true
        } else {
            return false
        }
    }
    private fun saveUserInfo(context: Context, username: String, token: String, userId: String, expirationTime: String) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userName", username)
        editor.putString("token", token)
        editor.putString("userId", userId)
        editor.putString("expirationTime", expirationTime)
        editor.apply()
    }
    private fun createintern()
    {

        val intentinf = Intent(this, MainActivity::class.java)
        startActivity(intentinf)
//        finish()
    }
}
//-----------------------------------------------------------------
class userLogin(  val username: String,
                  val password: String
)
//-----------------------------------------------------------------
class LoginResponse(val token: String)
//-----------------------------------------------------------------
