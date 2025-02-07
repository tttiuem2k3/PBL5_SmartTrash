package com.example.trashmobie.Presenter

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
import com.example.trashmobie.Model.Model_notification
import com.example.trashmobie.R
import com.example.trashmobie.databinding.NotificationLayoutBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response

private lateinit var binding: NotificationLayoutBinding
class NotificationActivity : ComponentActivity() {
    lateinit var customAdapterNotification: CustomAdapterNotification
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotificationLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chaneintent()
        setuplistview(Model.token_login)

    }


    private fun setuplistview(token :String)
    {

        ApiService.apiService.getDataNotificationApi("Bearer $token").enqueue(object : retrofit2.Callback<List<NotificationListResponseModel>> {
            override fun onResponse(call: Call<List<NotificationListResponseModel>>, response: Response<List<NotificationListResponseModel>>) {
                if (response.isSuccessful) {
                    val  NotificationListResponseModel= response.body()
                    var list = mutableListOf<Model_notification>()
                    // Xử lý dữ liệu khi nhận được phản hồi thành công từ máy chủ

                    if (NotificationListResponseModel!!.isNotEmpty()) {
                        for (notificationListResponseModel in NotificationListResponseModel) {
                            Log.d("TAG", notificationListResponseModel.garbage.toString())
                            list.add(Model_notification(R.drawable.icon_error,handlinggarbage(notificationListResponseModel.garbage),handlingtime(notificationListResponseModel.created_at),notificationListResponseModel.message))
                        }
                    }
                    else
                    {

                    }
                    customAdapterNotification = CustomAdapterNotification(this@NotificationActivity,list)
                    binding.lvNotification.adapter =customAdapterNotification
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@NotificationActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<NotificationListResponseModel>>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@NotificationActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun handlingtime(chuoi: String): String {
        var mahoa: String =""
        val dateTimeString = "2024-05-06T14:25:52.741551Z"

        val datePart = chuoi.substring(0, 10) // Lấy phần ngày tháng năm (yyyy-MM-dd)
        val timePart = chuoi.substring(11, 16) // Lấy phần giờ phút giây (HH:mm)

        mahoa = "$datePart,$timePart"
        return mahoa
    }
    private fun handlinggarbage(id: Int): String {
        var mahoa: String =""
        var i =0
        for(trashList in Model.trashList)
        {
            if(trashList == id )
            {
                break;
            }
            else
            {
                i++
            }
        }
        mahoa = Model.trashnamelist[i]
        return mahoa
    }
    private fun chaneintent() {
        binding.homeMenuBtn.setOnClickListener {
            finish()
            val intent1 = Intent( this, MainActivity::class.java)
            startActivity(intent1)
        }
//        binding.notificationMenuBtn.setOnClickListener {
//            val intent2 = Intent( this, NotificationActivity::class.java)
//            startActivity(intent2)
//        }
        binding.historyMenuBtn.setOnClickListener {
            finish()
            val intent3 = Intent( this, HistoryActivity::class.java)
            startActivity(intent3)
        }
        binding.remoteMenuBtn.setOnClickListener {
            finish()
            val intent4 = Intent( this, RemoteActivity::class.java)
            startActivity(intent4)
        }
        binding.statisticalMenuBtn.setOnClickListener {
            finish()
            val intent5 = Intent( this, StatisticalActivity::class.java)
            startActivity(intent5)
        }
    }
}

//-------------------------------------------------------------------------

class CustomAdapterNotification(val activity: Activity, var list: List<Model_notification>) : ArrayAdapter<Model_notification>(activity,
    R.layout.notification_item
)
{
    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexs= activity.layoutInflater
        val rowview = contexs.inflate(R.layout.notification_item,parent,false)

        val image = rowview.findViewById<ImageView>(R.id.image_notification)
        val trashtype= rowview.findViewById<TextView>(R.id.trashname)
        val datetime =  rowview.findViewById<TextView>(R.id.date_time)
        val notification =  rowview.findViewById<TextView>(R.id.notification)

        image.setImageResource(list[position].image_notification)
        trashtype.text = list[position].trashname
        datetime.text= list[position].datetime
        notification.text= list[position].notification

        return rowview
    }
}
//------------------------------------------------------------------------
data class NotificationListResponseModel(
    @SerializedName("id") var id: Int,
    @SerializedName("message") val message: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("garbage") val garbage: Int
)