package com.example.trashmobie.Presenter

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.trashmobie.Model.ApiService
import com.example.trashmobie.databinding.RegisterLayoutBinding
import retrofit2.Call
import retrofit2.Response


private lateinit var binding: RegisterLayoutBinding
class RegisterAtivity : ComponentActivity() {
//    private val BASE_URL = "http://192.168.1.168:8000/api/account_register_add_api/"
//    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish();
        }
        binding.btnRegister.setOnClickListener{

            val username = binding.username.text
            val phone = binding.Phone.text
            val email = binding.Email.text
            val password = binding.pass.text
            val confirm_password = binding.repass.text
            if (username.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm_password.isNotEmpty()) {
                val userRegisterdata = userRegister(
                    username.toString(),
                    phone.toString(),
                    email.toString(),
                    password.toString(),
                    confirm_password.toString()
                )
                ApiService.apiService.registerAPi(userRegisterdata)
                    .enqueue(object : retrofit2.Callback<registerReciveAPi> {
                        override fun onResponse(
                            call: Call<registerReciveAPi>,
                            response: Response<registerReciveAPi>
                        ) {
                            // Xử lý khi nhận được phản hồi thành công từ máy chủ
                            val registerReciveAPi: registerReciveAPi?

                            if (response.isSuccessful) {
                                registerReciveAPi = response.body()
                                Log.d("TAG", registerReciveAPi.toString())
                                Toast.makeText(
                                    this@RegisterAtivity,
                                    "Đăng ký tài khoản thành công",
                                    Toast.LENGTH_LONG
                                ).show()
                                binding.username.text=null
                                binding.Phone.text=null
                                binding.Email.text=null
                                binding.pass.text=null
                                binding.repass.text=null
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.d("TAG", "Error: ${response.code()} - $errorBody")
                                if (errorBody?.contains("Username is exist !") == true) {
                                    Toast.makeText(
                                        this@RegisterAtivity,
                                        "Tên người dùng đã tồn tại!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    binding.username.text=null
                                } else if (errorBody?.contains("Incorrect password and confirm_password confirmation.") == true) {
                                    Toast.makeText(
                                        this@RegisterAtivity,
                                        "Mật khẩu nhập lại không trùng khớp!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    binding.repass.text=null
                                }
                            }
                        }

                        override fun onFailure(call: Call<registerReciveAPi>, t: Throwable) {
                            Toast.makeText(this@RegisterAtivity, "Lỗi mịa rồi", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
            else
            {
                Toast.makeText(
                    this@RegisterAtivity,
                    "Vui lòng điền đầy đủ!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.dangnhap.setOnClickListener{
            finish()
        }
    }

}
//-----------------------------------------------------------------
class userRegister(  val username: String,
                     val phone: String,
                     val email: String,
                     val password: String,
                     val confirm_password: String
)
//-----------------------------------------------------------------
class registerReciveAPi(  val message: String
){
    override fun toString(): String {
        return "Mess = $message"
    }
}

