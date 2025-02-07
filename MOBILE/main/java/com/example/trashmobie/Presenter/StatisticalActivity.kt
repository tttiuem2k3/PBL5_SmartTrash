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
import androidx.appcompat.app.AppCompatActivity
import com.example.trashmobie.Model.ApiService
import com.example.trashmobie.Model.Model
import com.example.trashmobie.Model.Model_statictical_list
import com.example.trashmobie.R
import com.example.trashmobie.databinding.StatisticalLayoutBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response

private lateinit var binding: StatisticalLayoutBinding
class StatisticalActivity : AppCompatActivity() {
    lateinit var customAdapterStatisticalList: CustomAdapterStatisticallist
    var list = mutableListOf<Model_statictical_list>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StatisticalLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)
        chaneintent()
        setdata(Model.token_login);




        list.clear()
        list.add(Model_statictical_list(R.drawable.icon_circle,"Lượng rác thải kim loại ở Hải Châu nhiều"))
        list.add(Model_statictical_list(R.drawable.icon_circle,"Giấy là loại rác thải nhiều nhất ở Sơn Trà và Hòa Khánh"))
        list.add(Model_statictical_list(R.drawable.icon_circle,"Rác thải nhựa ở mức trung bình ở các khu vực"))
        list.add(Model_statictical_list(R.drawable.icon_circle,"Các loại rác thải khác ở mức trung bình ở các khu vực"))
        customAdapterStatisticalList = CustomAdapterStatisticallist(this@StatisticalActivity, list)
        binding.lvstatistical.adapter= customAdapterStatisticalList







    }

    private fun setdata(token :String)
    {
        ApiService.apiService.getDataStaticticalApi("Bearer $token").enqueue(object : retrofit2.Callback<List<StatisticalListResponseModel>> {
            override fun onResponse(call: Call<List<StatisticalListResponseModel>>, response: Response<List<StatisticalListResponseModel>>) {
                if (response.isSuccessful) {
                    val  StatisticalListResponseModel= response.body()

                    // Xử lý dữ liệu khi nhận được phản hồi thành công từ máy chủ

                    if (StatisticalListResponseModel!!.isNotEmpty()) {


                        for (statisticalListResponseModel in StatisticalListResponseModel) {
                            checkzone(statisticalListResponseModel.name_country,statisticalListResponseModel.value_average)

                        }

                    }
                    else
                    {

                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@StatisticalActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<StatisticalListResponseModel>>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@StatisticalActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun checkzone(zone: String, value: Value_average) {
        if(zone=="Son Tra")
        {
            val myTextView1: TextView = findViewById(R.id.metal_sontra)
            setHeight(myTextView1,(value.metal*200).toInt())
            val myTextView2: TextView = findViewById(R.id.flastic_sontra)
            setHeight(myTextView2,(value.plastic*200).toInt())
            val myTextView3: TextView = findViewById(R.id.paper_sontra)
            setHeight(myTextView3,(value.paper*200).toInt())
            val myTextView4: TextView = findViewById(R.id.other_sontra)
            setHeight(myTextView4,(value.cardboard*200).toInt())
        }
        else if(zone== "Hai Chau")
        {
            val myTextView1: TextView = findViewById(R.id.metal_haichau)
            setHeight(myTextView1,(value.metal*200).toInt())
            val myTextView2: TextView = findViewById(R.id.flastic_haichau)
            setHeight(myTextView2,(value.plastic*200).toInt())
            val myTextView3: TextView = findViewById(R.id.paper_haichau)
            setHeight(myTextView3,(value.paper*200).toInt())
            val myTextView4: TextView = findViewById(R.id.other_haichau)
            setHeight(myTextView4,(value.cardboard*200).toInt())
        }
        else if(zone =="Lien Chieu")
        {
            val myTextView1: TextView = findViewById(R.id.metal_hoakhanh)
            setHeight(myTextView1,(value.metal*200).toInt())
            val myTextView2: TextView = findViewById(R.id.flastic_hoakhanh)
            setHeight(myTextView2,(value.plastic*200).toInt())
            val myTextView3: TextView = findViewById(R.id.paper_hoakhanh)
            setHeight(myTextView3,(value.paper*200).toInt())
            val myTextView4: TextView = findViewById(R.id.other_hoakhanh)
            setHeight(myTextView4,(value.cardboard*200).toInt())
        }
    }
    private fun setHeight(textView: TextView, height: Int) {
        val params: ViewGroup.LayoutParams = textView.layoutParams
        params.height = dpToPx(height+1)
        textView.layoutParams = params
    }
    fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
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
//        binding.statisticalMenuBtn.setOnClickListener {
//            val intent5 = Intent( this, StatisticalActivity::class.java)
//            startActivity(intent5)
//        }
    }
}
//------------------------------------------------------------------------
class CustomAdapterStatisticallist(val activity: Activity, var list: List<Model_statictical_list>) : ArrayAdapter<Model_statictical_list>(activity,
    R.layout.statictical_list_item
)

{
    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexs= activity.layoutInflater
        val rowview = contexs.inflate(R.layout.statictical_list_item,parent,false)

        val image = rowview.findViewById<ImageView>(R.id.image)
        val cmt= rowview.findViewById<TextView>(R.id.cmt)

        image.setImageResource(list[position].image)

        cmt.text= list[position].cmt

        return rowview
    }
}
//------------------------------------------------------------------------
data class StatisticalListResponseModel(
    @SerializedName("name_country") var name_country: String,
    @SerializedName("value_average") val value_average: Value_average


)
data class Value_average(

    var metal: Double,
    var plastic: Double,
    var paper: Double,
    var cardboard: Double
)