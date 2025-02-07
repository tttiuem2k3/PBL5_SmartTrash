package com.example.trashmobie.Presenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.trashmobie.Model.ApiService
import com.example.trashmobie.Model.Model
import com.example.trashmobie.databinding.AddtrashLayoutBinding
import retrofit2.Call
import retrofit2.Response


private lateinit var binding: AddtrashLayoutBinding
class AddTrashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddtrashLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
            val intent1 = Intent( this, MainActivity::class.java)
            startActivity(intent1)
        }
        binding.btnAddtrash.setOnClickListener {
            val garbage_code = binding.trashName.text
            val name_country = binding.zoneName.text
            val description = binding.trashInf.text
            if (garbage_code.isNotEmpty() && name_country.isNotEmpty() && description.isNotEmpty()) {
                val addtrashData = AddtrashData(
                    Model.user_id.toInt(),
                    garbage_code.toString(),
                    name_country.toString(),
                    description.toString()
                )
                val token = Model.token_login
                ApiService.apiService.addTrashApi("Bearer $token", addtrashData)
                    .enqueue(object : retrofit2.Callback<AddtrashResponse> {
                        override fun onResponse(
                            call: Call<AddtrashResponse>,
                            response: Response<AddtrashResponse>
                        ) {
                            // Xử lý khi nhận được phản hồi thành công từ máy chủ
                            val addtrashResponse: AddtrashResponse?

                            if (response.isSuccessful) {
                                addtrashResponse = response.body()
                                Log.d("TAG", addtrashResponse.toString())
                                Toast.makeText(
                                    this@AddTrashActivity,
                                    "Thêm thùng rác thành công",
                                    Toast.LENGTH_LONG
                                ).show()
                                binding.trashName.text=null
                                binding.zoneName.text=null
                                binding.trashInf.text=null
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.d("TAG", "Error: ${response.code()} - $errorBody")
                                Toast.makeText(
                                    this@AddTrashActivity,
                                    "Thêm thùng rác thất bại",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<AddtrashResponse>, t: Throwable) {
                            Toast.makeText(this@AddTrashActivity, "Lỗi mịa rồi", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
            else
            {
                Toast.makeText(
                    this@AddTrashActivity,
                    "Vui lòng điền đầy đủ!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}
//-----------------------------------------------------------------
class AddtrashData(  val user_id: Int,
                       val garbage_code: String,
                       val name_country: String,
                       val description: String
)
//-----------------------------------------------------------------
class AddtrashResponse(  val message: String
){
    override fun toString(): String {
        return "Mess = $message"
    }
}