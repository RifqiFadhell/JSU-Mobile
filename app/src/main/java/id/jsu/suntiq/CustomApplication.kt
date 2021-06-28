package id.jsu.suntiq

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import id.jsu.suntiq.api.ApiConstant
import id.jsu.suntiq.api.ApiFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

class CustomApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        app = this
        val hostnameVerifier = HostnameVerifier { hostname, session -> true }

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(ChuckInterceptor(app))
            .connectTimeout(ApiConstant.TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConstant.TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConstant.TIMEOUT, TimeUnit.SECONDS)
            .hostnameVerifier(hostnameVerifier)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        apiClient = Retrofit.Builder().baseUrl("https://jsuonline.xyz/api/v1/apps/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(ApiFactory::class.java)
    }

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        var app: CustomApplication? = null
        var apiClient: ApiFactory? = null
    }
}