package com.example.trashmobie.Presenter


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.trashmobie.Model.ApiService
import com.example.trashmobie.Model.Model
import com.example.trashmobie.Model.Model_trash
import com.example.trashmobie.Model.Model_user
import com.example.trashmobie.R
import com.example.trashmobie.databinding.HomeLayoutBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response


private  lateinit var binding: HomeLayoutBinding
class MainActivity : ComponentActivity() {
    private var previousSelection: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animation = AnimationUtils.loadAnimation(this, R.anim.circle_rotate)
        binding.ProgressBarmetal.startAnimation(animation)
        binding.ProgressBarmetal.progress = 90
        val animation1 = AnimationUtils.loadAnimation(this, R.anim.circle_rotate)
        binding.ProgressBarplastic.startAnimation(animation1)
        binding.ProgressBarplastic.progress = 60
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.circle_rotate)
        binding.ProgressBarpaper.startAnimation(animation2)
        binding.ProgressBarpaper.progress = 75
        val animation3 = AnimationUtils.loadAnimation(this, R.anim.circle_rotate)
        binding.ProgressBarother.startAnimation(animation3)
        binding.ProgressBarother.progress = 34

        chaneintent()
        setupspiner()
        setupuser()
        setbtntypetrash()
        }
    private fun setbtntypetrash()
    {
        binding.tvmetal.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Xác nhận")
            builder.setMessage("Bạn có muốn dọn dẹp ngăn rác Metal không?")
            builder.setPositiveButton("Có") { dialog, which ->
                resettrash(Model.trashList[Model.trash], Model.token_login,"metal")
                binding.tvmetal.text="\nMetal\nQuantity: 0"
            }

            builder.setNegativeButton("Không") { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        binding.tvplastic.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Xác nhận")
            builder.setMessage("Bạn có muốn dọn dẹp ngăn rác Plastic không?")
            builder.setPositiveButton("Có") { dialog, which ->
                resettrash(Model.trashList[Model.trash], Model.token_login,"plastic")
                binding.tvplastic.text="\nPlastic\nQuantity: 0"
            }

            builder.setNegativeButton("Không") { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        binding.tvpaper.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Xác nhận")
            builder.setMessage("Bạn có muốn dọn dẹp ngăn rác Paper không?")
            builder.setPositiveButton("Có") { dialog, which ->
                resettrash(Model.trashList[Model.trash], Model.token_login,"paper")
                binding.tvpaper.text="\nPaper\nQuantity: 0"
            }

            builder.setNegativeButton("Không") { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        binding.tvother.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Xác nhận")
            builder.setMessage("Bạn có muốn dọn dẹp ngăn rác CardBoard không?")
            builder.setPositiveButton("Có") { dialog, which ->
                resettrash(Model.trashList[Model.trash], Model.token_login,"cardboard")
                binding.tvother.text="\nCardBoard\nQuantity: 0"
            }

            builder.setNegativeButton("Không") { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
    private fun resettrash(id: Int, token: String, type :String)
    {
        ApiService.apiService.getDataCompartmentApi(id,"Bearer $token").enqueue(object : retrofit2.Callback<List<CompartmentListResponseModel>> {
            override fun onResponse(call: Call<List<CompartmentListResponseModel>>, response: Response<List<CompartmentListResponseModel>>) {
                if (response.isSuccessful) {
                    val CompartmentListResponseModel = response.body()

                    // Xử lý dữ liệu khi nhận được phản hồi thành công từ máy chủ
                    if (CompartmentListResponseModel!!.isNotEmpty()) {

                        for (compartmentListResponseModel in CompartmentListResponseModel) {
                            if(compartmentListResponseModel.type_name_compartment.equals(type))
                            {
                                deteletrash(compartmentListResponseModel.id,token)
                            }
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@MainActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<CompartmentListResponseModel>>, t: Throwable) {

                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun deteletrash(id : Int, token :String)
    {
        val deltrashData = DELtrashData(
           id
        )
        ApiService.apiService.deleteTrashApi("Bearer $token", deltrashData)
            .enqueue(object : retrofit2.Callback<DELtrashResponse> {
                override fun onResponse(
                    call: Call<DELtrashResponse>,
                    response: Response<DELtrashResponse>
                ) {

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@MainActivity,
                            "Dọn dẹp thành công",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Dọn dẹp thất bại",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DELtrashResponse>, t: Throwable) {
                    // Xử lý khi yêu cầu gặp lỗi
                    Toast.makeText(this@MainActivity, "Lỗi mịa rồi", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun setupuser() {
//
        val list2 = mutableListOf<Model_user>()
        list2.add(Model_user(R.drawable.icons_user,Model.user_name.toUpperCase()))
        list2.add(Model_user(R.drawable.icon_information,"Thông tin"))
        list2.add(Model_user(R.drawable.icon_change,"Đổi mật khẩu"))
        list2.add(Model_user(R.drawable.icon_logout,"Đăng xuất"))
        val customUserSpiner = CustomUserSpiner(this@MainActivity, list2)
        binding.spuser.adapter =customUserSpiner

        binding.spuser.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position==0)
                {
                    val selectedView = parent?.getChildAt(position)
                    // Kiểm tra xem mục được chọn có tồn tại không
                    if (selectedView != null) {
                        // Tìm TextView trong View của mục được chọn và thay đổi nội dung
                        val textView = selectedView.findViewById<TextView>(R.id.user_text)
                        textView.text = " "
                    }
                }
                else
                {
                    if(position==1)
                    {
                        finish()
                        val intentinf = Intent( this@MainActivity, AccountInformationActivity::class.java)
                        startActivity(intentinf)
                    }
                    else if(position==2)
                    {
                        finish()
                        val intentpass = Intent( this@MainActivity, ChangePassAtivity::class.java)
                        startActivity(intentpass)
                    }
                    else if(position==3)
                    {
                        Model.token_login=""
                        Model.user_id=""
                        Model.exp=""
                        Model.user_name=""
                        Model.trash=0
                        Model.trashList.clear()
                        logout(this@MainActivity)
                        startlogin()
                    }
                }
                binding.spuser.setSelection(0)
            }
            override fun onNothingSelected(parent: AdapterView<*>?,) {

            }
        }
    }
    private fun startlogin() {
        val intentlogin = Intent(this, LoginAtivity::class.java)
        startActivity(intentlogin)
        finish()
    }
    private fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
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
                        Model.trashList.clear()
                        for (accinfResponseModel in accinfResponseModelList) {
                            Model.trashList.add(accinfResponseModel.id)
                            Model.trashnamelist.add(accinfResponseModel.garbageCode)
                            list2.add(Model_trash(R.drawable.icon_trash,accinfResponseModel.garbageCode))
                        }
                    }
                    else
                    {
                        binding.sptrash.setSelection(0)
                    }

                    list2.add(Model_trash(R.drawable.icon_add,"Thêm"))
                    val customTrashSpiner = CustomTrashSpiner(this@MainActivity, list2)
                    binding.sptrash.adapter =customTrashSpiner
                    binding.sptrash.setSelection(Model.trash)
                    if(Model.trashList.isNotEmpty())
                    {
                        getquantity(Model.trashList[Model.trash], Model.token_login)
                    }
                    else
                    {
                        binding.tvmetal.text="\nMetal\nQuantity: 0"
                        binding.tvplastic.text="\nPlastic\nQuantity: 0"
                        binding.tvpaper.text="\nPaper\nQuantity: 0"
                        binding.tvother.text="\nCardBoard\nQuantity: 0"
                        binding.homespace.text = "No\nTrash!"
                    }

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
                                val intentadd = Intent( this@MainActivity, AddTrashActivity::class.java)
                                startActivity(intentadd)
                                previousSelection=list2.size-1
                            }
                            else
                            {
                                if(previousSelection!=list2.size-1)
                                    {
                                        if(list2[position].trash_name!="")
                                        {
                                            Toast.makeText(this@MainActivity,"Bạn đang chọn thùng rác "+list2[position].trash_name,Toast.LENGTH_SHORT).show()
                                        }
                                        else
                                        {
                                            Toast.makeText(this@MainActivity,"Bạn chưa thêm thùng rác",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                previousSelection = position
                                Model.trash = position
                                if(Model.trashList.isNotEmpty())
                                {
                                    getquantity(Model.trashList[Model.trash], Model.token_login)
                                }
                                else
                                {
                                    binding.tvmetal.text="\nMetal\nQuantity: 0"
                                    binding.tvplastic.text="\nPlastic\nQuantity: 0"
                                    binding.tvpaper.text="\nPaper\nQuantity: 0"
                                    binding.tvother.text="\nCardBoard\nQuantity: 0"
                                    binding.homespace.text = "No\nTrash!"
                                }
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?,) {

                        }
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@MainActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<accInfResponseModel>>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun getquantity(id: Int, token :String) {
        ApiService.apiService.getDataQuantityTrash(id,"Bearer $token").enqueue(object : retrofit2.Callback<trashData> {
            override fun onResponse(call: Call<trashData>, response: Response<trashData>) {
                if (response.isSuccessful) {
                    val trashData = response.body()

                    // Xử lý dữ liệu khi nhận được phản hồi thành công từ máy chủ
                    if (trashData != null) {
                        binding.tvmetal.text="\nMetal\nQuantity: "+trashData.total_metal.toString()
                        binding.tvplastic.text="\nPlastic\nQuantity: "+trashData.total_plastic.toString()
                        binding.tvpaper.text="\nPaper\nQuantity: "+trashData.total_paper.toString()
                        binding.tvother.text="\nCardBoard\nQuantity: "+trashData.total_cardboard.toString()
                        handlinghomespace(Model.trashList[Model.trash], Model.token_login)
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@MainActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<trashData>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun handlinghomespace( id:Int,token : String){
        ApiService.apiService.getDataDistanceApi(id,"Bearer $token").enqueue(object : retrofit2.Callback<List<DistanceResponseModel>> {
            override fun onResponse(call: Call<List<DistanceResponseModel>>, response: Response<List<DistanceResponseModel>>) {
                if (response.isSuccessful) {
                    val  DistanceResponseModel= response.body()

                    // Xử lý dữ liệu khi nhận được phản hồi thành công từ máy chủ

                    if (DistanceResponseModel!!.isNotEmpty()) {

                    var max : Double = 0.0
                        for (distanceResponseModel in DistanceResponseModel) {
                            if(distanceResponseModel.distance_is_full > max)
                            {
                                max=distanceResponseModel.distance_is_full
                                binding.homespace.text="${distanceResponseModel.type_name_compartment.capitalize()}\nFree:${100-(max*100).toInt()}%"
                            }
                        }

                    }
                    else
                    {

                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("TAG", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@MainActivity, "Lấy thông tin bị lỗi", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<DistanceResponseModel>>, t: Throwable) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("TAG", "Error: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Lỗi mịa rồi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun chaneintent() {
//        binding.homeMenuBtn.setOnClickListener {
//            val intent1 = Intent( this, MainActivity::class.java)
//            startActivity(intent1)
//        }
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
        binding.statisticalMenuBtn.setOnClickListener {
            finish()
            val intent5 = Intent( this, StatisticalActivity::class.java)
            startActivity(intent5)
        }
    }
}


//----------------------------------------------------------------

class CustomTrashSpiner(val activity: Activity, val list2: List<Model_trash>): ArrayAdapter<Model_trash>(activity,
    R.layout.spiner_trash_item
)
{
    override fun getCount(): Int {
        return list2.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initview(position, convertView , parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initview(position, convertView , parent)
    }
    private fun initview(position: Int, convertView: View?, parent: ViewGroup) :View {
        val contexs = activity.layoutInflater
        val rowview = contexs.inflate(R.layout.spiner_trash_item, parent, false)
        val image = rowview.findViewById<ImageView>(R.id.trash_image)
        val trash_name= rowview.findViewById<TextView>(R.id.trash_name)
        image.setImageResource(list2[position].image)
        trash_name.text = list2[position].trash_name
        return rowview

    }
}

//----------------------------------------------------------------

class CustomUserSpiner(val activity: Activity, val list: List<Model_user>): ArrayAdapter<Model_user>(activity,
    R.layout.spiner_user_item
)
{
    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initview(position, convertView , parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initview(position, convertView , parent)
    }
    private fun initview(position: Int, convertView: View?, parent: ViewGroup) :View {
        val contexs = activity.layoutInflater
        val rowview = contexs.inflate(R.layout.spiner_user_item, parent, false)
        val user_image = rowview.findViewById<ImageView>(R.id.user_image)
        val user_text= rowview.findViewById<TextView>(R.id.user_text)
        user_image.setImageResource(list[position].image_user)

        user_text.text = list[position].user_text
        return rowview

    }
}
//----------------------------------------------------------------

class trashData(  val total_metal: Int,
                  val total_plastic :Int,
                  val total_paper : Int,
                  val total_cardboard : Int
)
//-------------------------------------------------------------------------
data class CompartmentListResponseModel(
    @SerializedName("id") val id: Int,
    @SerializedName("type_name_compartment") val type_name_compartment: String
)
//-------------------------------------------------------------------------
data class DistanceResponseModel(
    @SerializedName("type_name_compartment") val type_name_compartment: String,
    @SerializedName("distance_is_full") val distance_is_full: Double
)
//-----------------------------------------------------------------
class DELtrashData(var id_compartment: Int)

//-----------------------------------------------------------------
class DELtrashResponse(  val message: String
){
    override fun toString(): String {
        return "Mess = $message"
    }
}