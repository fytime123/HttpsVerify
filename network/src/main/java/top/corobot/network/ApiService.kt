package top.corobot.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET
    suspend fun load(@HeaderMap header:Map<String, String>?, @Url url:String? ): Response<ResponseBody?>?

    @POST
    suspend fun load(@HeaderMap header :Map<String, String>?, @Url url:String? , @Body body:Any?): Response<ResponseBody?>?

    //上传单个文件
    @POST
    suspend fun upload(@HeaderMap header: Map<String, String>?, @Url url: String?, @Body body: MultipartBody?): Response<ResponseBody?>?
}