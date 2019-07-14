package com.example.ta.Api


import com.example.ta.EditProfileAct
import com.example.ta.Model.PostResponse
import com.example.ta.Model.ServerResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

public interface UploadAPI {
    @Multipart
    @POST("/ecommerce/upload.php")
    fun uploadFile(
        @Part image: MultipartBody.Part,
        @Part("username") name: RequestBody
    ): Call<ResponseBody>

}