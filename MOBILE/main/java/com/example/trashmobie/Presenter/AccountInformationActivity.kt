package com.example.trashmobie.Presenter
import com.google.gson.annotations.SerializedName
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.trashmobie.Model.ApiService
import com.example.trashmobie.Model.Model
import com.example.trashmobie.Model.Model_history
import com.example.trashmobie.Model.Model_trash_in4
import com.example.trashmobie.R
import com.example.trashmobie.databinding.AccountInformationLayoutBinding
import retrofit2.Call
import retrofit2.Response

private lateinit var binding: AccountInformationLayoutBinding
class AccountInformationActivity : ComponentActivity() {
    lateinit var customAdapterin4: CustomAdapterin4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AccountInformationLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
            val intent1 = Intent( this, MainActivity::class.java)
            startActivity(intent1)
        }
        setview()
    }

    private fun setview() {
        val token = Model.token_login
        ApiService.apiService.getDatatrashApi("Bearer $token").enqueue(object : retrofit2.Callback<List<accInfResponseModel>> {
            override fun onResponse(call: Call<List<accInfResponseModel>>, response: Response<List<accInfResponseModel>>) {
                if (response.isSuccessful) {
                    val accinfResponseModelList = response.body()

                    // Xử lý dữ liệu khi nhận được phản hồi thành công từ máy chủ
                    if (accinfResponseModelList != null) {
                        var list = mutableListOf<Model_trash_in4>()
                        for (accinfResponseModel in accinfResponseModelList) {
                            Log.d("TAG", accinfResponseModel.toString())
                            list.add(
                                Model_trash_in4(
                                    R.drawable.icon_trash,accinfResponseModel.garbageCode,
                                accinfResponseModel.nameCountry,accinfResponseModel.description)
                            )
                            // Xử lý dữ liệu từ mỗi accInfResponseModel ở đây
                        }
                        customAdapterin4 = CustomAdapterin4(this@AccountInformationActivity,list)
                        binding.lvtrashin4.adapter =customAdapterin4
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@AccountInformationActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<accInfResponseModel>>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@AccountInformationActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        ApiService.apiService.getDataUserApi("Bearer $token").enqueue(object : retrofit2.Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    val userData = response.body()

                    // Xử lý dữ liệu khi nhận được phản hồi thành công từ máy chủ
                    if (userData != null) {
                        binding.username.text =userData.username.toUpperCase()
                        binding.Email.text=userData.email
                        binding.Phone.text=userData.profile.phone
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@AccountInformationActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@AccountInformationActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}



//-------------------------------------------------------------------------------------------------
class CustomAdapterin4(val activity: Activity, var list: List<Model_trash_in4>) : ArrayAdapter<Model_history>(activity,
    R.layout.in4_trash_item
)
{
    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexs= activity.layoutInflater
        val rowview = contexs.inflate(R.layout.in4_trash_item,parent,false)

        val image= rowview.findViewById<ImageView>(R.id.trash_image)
        val trashname= rowview.findViewById<TextView>(R.id.trash_name)
        val trash_inf =  rowview.findViewById<TextView>(R.id.trash_inf)
        val zone_name =  rowview.findViewById<TextView>(R.id.zone_name)
        image.setImageResource(list[position].image)
        trashname.text= list[position].trash_name
        zone_name.text= list[position].zone_name
        trash_inf.text= list[position].trash_inf

        return rowview
    }
}
//-------------------------------------------------------------------------------------------------


data class accInfResponseModel(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("garbage_code") val garbageCode: String,
    @SerializedName("name_country") val nameCountry: String,
    @SerializedName("description") val description: String,
//    @SerializedName("created_at") val createdAt: String,
//    @SerializedName("updated_at") val updatedAt: String
)

data class UserData(
    val username: String,
    val email: String,
    val profile: ProfileData
)

data class ProfileData(
    val phone: String
)