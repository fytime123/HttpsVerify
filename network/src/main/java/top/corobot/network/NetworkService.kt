package top.corobot.network

import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import top.corobot.network.cookie.CookieJarImpl
import top.corobot.network.cookie.MemoryCookieStore
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

object NetworkService {

    private const val DEFAULT_TIMEOUT = 30L
    private const val CONNECT_TIMEOUT = 30L

    private val cookieJar: CookieJarImpl = CookieJarImpl(MemoryCookieStore())

    private val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        //.cache(cache)
        .retryOnConnectionFailure(true)
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .cookieJar(cookieJar)

    private val build: OkHttpClient = builder.build()

    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z")
        .disableHtmlEscaping()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .client(build)
        .baseUrl("http://api.corobot.top/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val apiService = retrofit.create<ApiService>()

    suspend fun load(
        url: String,
        header: Map<String, String>?,
        querys: Map<String, String>?
    ): Response<ResponseBody?>? {
        var urls = getQuesysUrl(url, querys)
        //Log.v("liufuyi",urls)
        return apiService.load(header, urls)
    }

    suspend fun load(
        url: String,
        header: Map<String, String>?,
        querys: Map<String, String>?,
        body: Any?
    ): Response<ResponseBody?>? {
        var urls = getQuesysUrl(url, querys)
        return apiService.load(header, urls, body)
    }

    //上传单个文件,带多个form参数
    suspend fun uploadFileFormData(
        url: String,
        header: Map<String, String>?,
        querys: Map<String, String>?,
        formData: Map<String, String>?,
        fileType: String?,
        filePath: String?
    ): Response<ResponseBody?>? {

        if (TextUtils.isEmpty(filePath)) {
            return null
        }

        val file = File(filePath)
        if (!file.exists()) {
            return null
        }

        val uploadUrl = getQuesysUrl(url, querys)
        val body = getFileRequestBody(fileType, file, formData)

        return apiService.upload(header, uploadUrl, body)
    }


    private fun getQuesysUrl(urlIn: String, querys: Map<String, String>?): String {
        var url = urlIn
        if (querys == null || querys.isEmpty()) {
            return url
        }
        if (!url.contains("?")) {
            url = "$url?"
        }
        var buffer: StringBuffer = StringBuffer()
        val iterator = querys.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            var value: String = try {
                URLEncoder.encode(querys[key], "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                ""
            }
            buffer.append(key).append("=").append(value)
            if (iterator.hasNext()) buffer.append("&")
        }
        url += buffer.toString()

        return url
    }

    private fun getFileRequestBody(
        fileType: String?,
        file: File,
        formData: Map<String, String>?
    ): MultipartBody {

        //构建requestbody
        val requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file)

        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM) //可以根据自己的接口需求在这里添加上传的参数
            .addFormDataPart(fileType, file.name, requestFile)

        if (formData != null && !formData.isEmpty()) {
            val iterator: Iterator<String> = formData.keys.iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                val value = formData[key]
                builder.addFormDataPart(key, value)
            }
        }

        //MultipartBody.Part body = MultipartBody.Part.create(multipartBody);
        return builder.build()
    }


}