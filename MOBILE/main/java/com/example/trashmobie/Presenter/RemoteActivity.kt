package com.example.trashmobie.Presenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.trashmobie.Model.ApiService
import com.example.trashmobie.Model.Model
import com.example.trashmobie.Model.Model_trash
import com.example.trashmobie.R
import com.example.trashmobie.databinding.RemoteLayoutBinding
import retrofit2.Call
import retrofit2.Response


private lateinit var binding:RemoteLayoutBinding
class RemoteActivity : ComponentActivity() {
    private var previousSelection: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RemoteLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chaneintent()
        setupspiner()
        setstatus()
   }

    private fun setstatus() {
        binding.btnLock.setOnClickListener{
            if(binding.status.text=="Bạn chưa cài thùng rác nào!")
            {
                binding.status.text=="Bạn chưa cài thùng rác nào!"
            }
            else
            {
                Toast.makeText(this@RemoteActivity,"Thùng rác đã bị đóng nắp!",Toast.LENGTH_SHORT).show()
                binding.status.text="đang bị khóa...."
                binding.status.setTextColor(android.graphics.Color.parseColor("#7C0505"))
            }

        }
        binding.btnUnlock.setOnClickListener{
            if(binding.status.text=="Bạn chưa cài thùng rác nào!")
            {
                binding.status.text=="Bạn chưa cài thùng rác nào!"
            }
            else
            {
                Toast.makeText(this@RemoteActivity,"Thùng rác đã được mở nắp!",Toast.LENGTH_SHORT).show()
                binding.status.text="đang hoạt động...."
                binding.status.setTextColor(android.graphics.Color.parseColor("#136E2A"))
            }

        }


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
                        binding.status.text="Bạn chưa cài thùng rác nào!"
                        binding.sptrash.setSelection(0)
                    }
                    list2.add(Model_trash(R.drawable.icon_add,"Thêm"))
                    val customTrashSpiner = CustomTrashSpiner(this@RemoteActivity, list2)
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
                                val intentadd = Intent( this@RemoteActivity, AddTrashActivity::class.java)
                                startActivity(intentadd)
                                previousSelection=list2.size-1
                            }
                            else
                            {
                                if(previousSelection!=list2.size-1)
                                {
                                    if(list2[position].trash_name!="")
                                    {
                                        Toast.makeText(this@RemoteActivity,"Bạn đang chọn thùng rác "+list2[position].trash_name,Toast.LENGTH_SHORT).show()
                                    }
                                    else
                                    {
                                        Toast.makeText(this@RemoteActivity,"Bạn chưa thêm thùng rác",Toast.LENGTH_SHORT).show()
                                    }
                                }
                                previousSelection = position
                                Model.trash = position
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?,) {

                        }
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@RemoteActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<accInfResponseModel>>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@RemoteActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
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
        binding.historyMenuBtn.setOnClickListener {
            finish()
            val intent3 = Intent( this, HistoryActivity::class.java)
            startActivity(intent3)
        }
//        binding.remoteMenuBtn.setOnClickListener {
//            val intent4 = Intent( this, RemoteActivity::class.java)
//            startActivity(intent4)
//        }
        binding.statisticalMenuBtn.setOnClickListener {
            finish()
            val intent5 = Intent( this, StatisticalActivity::class.java)
            startActivity(intent5)
        }
    }
}