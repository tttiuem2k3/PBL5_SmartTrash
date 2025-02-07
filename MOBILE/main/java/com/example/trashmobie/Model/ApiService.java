package com.example.trashmobie.Model;

import com.example.trashmobie.Presenter.AddtrashData;
import com.example.trashmobie.Presenter.AddtrashResponse;
import com.example.trashmobie.Presenter.ChangepassData;
import com.example.trashmobie.Presenter.ChangepassResponse;
import com.example.trashmobie.Presenter.CompartmentListResponseModel;
import com.example.trashmobie.Presenter.DELtrashData;
import com.example.trashmobie.Presenter.DELtrashResponse;
import com.example.trashmobie.Presenter.DistanceResponseModel;
import com.example.trashmobie.Presenter.HistoryListResponseModel;
import com.example.trashmobie.Presenter.LoginResponse;
import com.example.trashmobie.Presenter.NotificationListResponseModel;
import com.example.trashmobie.Presenter.StatisticalListResponseModel;
import com.example.trashmobie.Presenter.UserData;
import com.example.trashmobie.Presenter.accInfResponseModel;
import com.example.trashmobie.Presenter.registerReciveAPi;
import com.example.trashmobie.Presenter.trashData;
import com.example.trashmobie.Presenter.userLogin;
import com.example.trashmobie.Presenter.userRegister;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//    https://lab-moving-grizzly.ngrok-free.app/
//    http://192.168.1.168:8000/


    ApiService apiService = new Retrofit.Builder().baseUrl(Model.INSTANCE.getDoman()+"/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @POST("api/account_register_add_api/")
    Call<registerReciveAPi> registerAPi(@Body userRegister userregister);
    @POST("api/account_login_api/")
    Call<LoginResponse> loginApi(@Body userLogin userlogin);
    @POST("api/account_reset_password_api/")
    Call<ChangepassResponse> changepassApi(@Header("Authorization") String token , @Body ChangepassData changepassData);

    @POST("api/garbage/add_garbage_api/")
    Call<AddtrashResponse> addTrashApi(@Header("Authorization") String token , @Body AddtrashData addtrashData);
    @GET("api/garbage/garbage_get_all_by_user_api/")
    Call<List<accInfResponseModel>> getDatatrashApi(@Header("Authorization") String token );

    @GET("api/account_get_by_id_api/")
    Call<UserData> getDataUserApi(@Header("Authorization") String token );
    @GET("api/garbage/get_quantity_compartment_by_id_api/{id}/")
    Call<trashData> getDataQuantityTrash(@Path("id") int id, @Header("Authorization") String token);
    @GET("api/history/get_all_predict_infor_by_id_garbage_api/{id}/")
    Call<List<HistoryListResponseModel>> getDataHistoryApi(@Path("id") int id, @Header("Authorization") String token );

    @GET("api/garbage/get_distance_is_full_compartment_by_id_api/{id}/")
    Call<List<DistanceResponseModel>> getDataDistanceApi(@Path("id") int id, @Header("Authorization") String token );
    @GET("api/garbage/get_average_distance_is_full_compartment_by_all_garbage_api/")
    Call<List<StatisticalListResponseModel>> getDataStaticticalApi(@Header("Authorization") String token );

    @GET("api/garbage/get_all_notify_api_by_user/")
    Call<List<NotificationListResponseModel>> getDataNotificationApi(@Header("Authorization") String token );

    @GET("api/garbage/get_id_compartment_by_id_garbage_api/{id}/")
    Call<List<CompartmentListResponseModel>> getDataCompartmentApi(@Path("id") int id, @Header("Authorization") String token );

//    @DELETE("api/history/delete_all_predict_infor_by_id_compartment_api/")
    @HTTP(method = "DELETE", path = "api/history/delete_all_predict_infor_by_id_compartment_api/", hasBody = true)
    Call<DELtrashResponse> deleteTrashApi(@Header("Authorization") String token , @Body DELtrashData deltrashData);
}
