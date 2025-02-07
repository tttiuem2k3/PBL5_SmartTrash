package com.example.trashmobie.Presenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.trashmobie.Model.ApiService
import com.example.trashmobie.Model.Model
import com.example.trashmobie.databinding.ChangePassLayoutBinding
import retrofit2.Call
import retrofit2.Response

private lateinit var binding: ChangePassLayoutBinding
class ChangePassAtivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChangePassLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
            val intent1 = Intent( this, MainActivity::class.java)
            startActivity(intent1)
        }
        binding.btnChangepass.setOnClickListener{

            val old_password = binding.pass.text
            val new_password = binding.newpass.text
            val confirm_new_password = binding.repass.text
            if (old_password.isNotEmpty() && new_password.isNotEmpty() && confirm_new_password.isNotEmpty()) {
                val changepassData = ChangepassData(
                    Model.user_id.toInt(),
                    old_password.toString(),
                    new_password.toString(),
                    confirm_new_password.toString()
                )
                val token = Model.token_login
                ApiService.apiService.changepassApi("Bearer $token", changepassData)
                    .enqueue(object : retrofit2.Callback<ChangepassResponse> {
                        override fun onResponse(
                            call: Call<ChangepassResponse>,
                            response: Response<ChangepassResponse>
                        ) {
                            // Xử lý khi nhận được phản hồi thành công từ máy chủ
                            val changepassResponse: ChangepassResponse?

                            if (response.isSuccessful) {
                                changepassResponse = response.body()
                                Log.d("TAG", changepassResponse.toString())
                                Toast.makeText(
                                    this@ChangePassAtivity,
                                    "Đổi mật khẩu thành công",
                                    Toast.LENGTH_LONG
                                ).show()
                                binding.pass.text=null
                                binding.newpass.text=null
                                binding.repass.text=null
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.d("TAG", "Error: ${response.code()} - $errorBody")
                                if (errorBody?.contains("Old password is incorrect") == true) {
                                    Toast.makeText(
                                        this@ChangePassAtivity,
                                        "Mật khẩu cũ không chính xác",
                                        Toast.LENGTH_LONG
                                    ).show()

                                } else if (errorBody?.contains("Incorrect new password and password confirmation.") == true) {
                                    Toast.makeText(
                                        this@ChangePassAtivity,
                                        "Mật khẩu mới nhập lại không trùng khớp!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<ChangepassResponse>, t: Throwable) {
                            Toast.makeText(
                                this@ChangePassAtivity,
                                "Lỗi mịa rồi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            else
            {
                Toast.makeText(
                    this@ChangePassAtivity,
                    "Vui lòng điền đầy đủ!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
//-----------------------------------------------------------------
class ChangepassData(  val id: Int,
                     val old_password: String,
                     val new_password: String,
                     val confirm_new_password: String
)
//-----------------------------------------------------------------
class ChangepassResponse(  val message: String
){
    override fun toString(): String {
        return "Mess = $message"
    }
}