package com.example.trashmobie.Presenter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.trashmobie.Model.ApiService
import com.example.trashmobie.Model.Model
import com.example.trashmobie.Model.Model_history
import com.example.trashmobie.Model.Model_trash
import com.example.trashmobie.R
import com.example.trashmobie.databinding.HistoryLayoutBinding
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

private lateinit var binding:HistoryLayoutBinding
class HistoryActivity : ComponentActivity() {
    lateinit var customAdapterHistory: CustomAdapterHistory
    private var previousSelection: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HistoryLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chaneintent()
        if(Model.trashList.isNotEmpty())
        {
            setuplistview(Model.trashList[Model.trash])
        }
        setupspiner()

        }

    private fun setuplistview(id: Int) {
        val token = Model.token_login
        ApiService.apiService.getDataHistoryApi(id,"Bearer $token").enqueue(object : retrofit2.Callback<List<HistoryListResponseModel>> {
            override fun onResponse(call: Call<List<HistoryListResponseModel>>, response: Response<List<HistoryListResponseModel>>) {
                if (response.isSuccessful) {
                    val historyListResponseModel = response.body()
                    var list = mutableListOf<Model_history>()
                    // Xử lý dữ liệu khi nhận được phản hồi thành công từ máy chủ
                    if (historyListResponseModel!!.isNotEmpty()) {
                        list.clear()
                        for (historylistResponseModel in historyListResponseModel) {
                            list.add(
                                Model_history(handlingimage(historylistResponseModel.image_garbage),historylistResponseModel.type_name_garbage
                                ,handlingtime(historylistResponseModel.created_at),handlingtype(historylistResponseModel.type_name_garbage))
                            )
                        }
                    }



                    customAdapterHistory = CustomAdapterHistory(this@HistoryActivity,list)
                    binding.lvHistory.adapter =customAdapterHistory
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@HistoryActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<HistoryListResponseModel>>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@HistoryActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun handlingtype(type: String): String {
        var mahoa: String =""
        if(type=="Metal")
        {
            mahoa ="1"
        }
        else if(type=="Plastic")
        {
            mahoa ="2"
        }
        else if(type=="Paper")
        {
            mahoa ="3"
        }
        else if(type=="Other")
        {
            mahoa ="4"
        }
        return mahoa
    }
    private fun handlingtime(chuoi: String): String {
        var mahoa: String =""
        val dateTimeString = "2024-05-06T14:25:52.741551Z"

        val datePart = chuoi.substring(0, 10) // Lấy phần ngày tháng năm (yyyy-MM-dd)
        val timePart = chuoi.substring(11, 19) // Lấy phần giờ phút giây (HH:mm:ss)

        mahoa = "Đã lọt vào thùng rác vào lúc $timePart ngày $datePart"
        return mahoa
    }
    private fun handlingimage(chuoi: String): String {

        val mahoa = Model.doman+chuoi

        return mahoa
    }
    private fun setupspiner() {
        val token = Model.token_login
        ApiService.apiService.getDatatrashApi("Bearer $token").enqueue(object : retrofit2.Callback<List<accInfResponseModel>> {
            override fun onResponse(call: Call<List<accInfResponseModel>>, response: Response<List<accInfResponseModel>>) {
                if (response.isSuccessful) {
                    val accinfResponseModelList = response.body()
                    val list2 = mutableListOf<Model_trash>()
                    list2.add(Model_trash(R.drawable.icon_trash,""))
                    // Xử lý dữ liệu khi nhận được phản hồi thành công từ máy chủ
                    if (accinfResponseModelList!!.isNotEmpty()) {
                        list2.clear()
                        for (accinfResponseModel in accinfResponseModelList) {
                            list2.add(Model_trash(R.drawable.icon_trash,accinfResponseModel.garbageCode))
                        }
                    }
                    else
                    {
                        binding.sptrash.setSelection(0)
                    }
                    list2.add(Model_trash(R.drawable.icon_add,"Thêm"))
                    val customTrashSpiner = CustomTrashSpiner(this@HistoryActivity, list2)
                    binding.sptrash.adapter =customTrashSpiner
                    binding.sptrash.setSelection(Model.trash)
                    binding.sptrash.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if(position==list2.size-1)
                            {
                                binding.sptrash.setSelection(previousSelection)
                                finish()
                                val intentadd = Intent( this@HistoryActivity, AddTrashActivity::class.java)
                                startActivity(intentadd)
                                previousSelection=list2.size-1
                            }
                            else
                            {
                                if(previousSelection!=list2.size-1)
                                {
                                    if(list2[position].trash_name!="")
                                    {
                                        Toast.makeText(this@HistoryActivity,"Bạn đang chọn thùng rác "+list2[position].trash_name,Toast.LENGTH_SHORT).show()
                                    }
                                    else
                                    {
                                        Toast.makeText(this@HistoryActivity,"Bạn chưa thêm thùng rác",Toast.LENGTH_SHORT).show()
                                    }
                                }
                                previousSelection = position
                                Model.trash = position
                                if(Model.trashList.isNotEmpty())
                                {
                                    setuplistview(Model.trashList[Model.trash])
                                }

                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?,) {

                        }
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@HistoryActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<accInfResponseModel>>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@HistoryActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun chaneintent() {
        binding.homeMenuBtn.setOnClickListener {
            finish()
            val intent1 = Intent( this, MainActivity::class.java)
            startActivity(intent1)
        }
        binding.notificationMenuBtn.setOnClickListener {
            finish()
            val intent2 = Intent( this, NotificationActivity::class.java)
            startActivity(intent2)
        }
//        binding.historyMenuBtn.setOnClickListener {
//            val intent3 = Intent( this, HistoryActivity::class.java)
//            startActivity(intent3)
//        }
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

class CustomAdapterHistory(val activity: Activity, var list: List<Model_history>) : ArrayAdapter<Model_history>(activity,
    R.layout.history_item
)
{
    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexs= activity.layoutInflater
        val rowview = contexs.inflate(R.layout.history_item,parent,false)

        val image= rowview.findViewById<ImageView>(R.id.trash_image)
        val trashtype= rowview.findViewById<TextView>(R.id.trash_name)
        val trash_inf =  rowview.findViewById<TextView>(R.id.trash_inf)
        val trash_id =  rowview.findViewById<TextView>(R.id.textView_id)
        Picasso.get().load(list[position].image).into(image)
        trashtype.text= list[position].trashtype
        trash_inf.text= list[position].trash_inf
        if(list[position].id.equals("1"))
        {
            trash_id.setBackgroundResource(R.drawable.history_item1)
            trashtype.setTextColor(android.graphics.Color.parseColor("#8A6F1F"))
        }
        else if (list[position].id.equals("2"))
        {
            trash_id.setBackgroundResource(R.drawable.history_item2)
            trashtype.setTextColor(android.graphics.Color.parseColor("#1B7BC8"))
        }
        else if (list[position].id.equals("3"))
        {
            trash_id.setBackgroundResource(R.drawable.history_item3)
            trashtype.setTextColor(android.graphics.Color.parseColor("#2E9113"))
        }
        else if (list[position].id.equals("4"))
        {
            trash_id.setBackgroundResource(R.drawable.history_item4)
            trashtype.setTextColor(android.graphics.Color.parseColor("#7A0F75"))
        }
        return rowview
    }
}
//-------------------------------------------------------------------------
data class HistoryListResponseModel(
    @SerializedName("type_name_garbage") val type_name_garbage: String,
    @SerializedName("image_garbage") val image_garbage: String,
    @SerializedName("created_at") val created_at: String,

)