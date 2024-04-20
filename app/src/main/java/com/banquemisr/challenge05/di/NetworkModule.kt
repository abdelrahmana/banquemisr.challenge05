package com.banquemisr.swensonhe.di

import android.content.Context
import android.content.SharedPreferences
import com.banquemisr.androidtask.data.model.Constants
import com.banquemisr.androidtask.data.model.Constants.ACCESSTOKEN
import com.banquemisr.androidtask.data.model.Constants.API_KEY
import com.banquemisr.androidtask.data.model.Constants.API_KEY_K
import com.banquemisr.androidtask.data.model.Constants.LANG
import com.banquemisr.challenge05.BuildConfig
import com.banquemisr.challenge05.data.model.SourceModel
import com.banquemisr.challenge05.data.source.remote.EndPoints
import com.google.gson.GsonBuilder
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@InstallIn(ViewModelComponent::class, FragmentComponent::class, ServiceComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideRetrofitBuilder(@ApplicationContext applicationContext: Context,sharedPreferences: SharedPreferences): OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getAuthInterceptor(ACCESSTOKEN,applicationContext,sharedPreferences))
            .addInterceptor(logging)
            .build()
    }
    @Provides
    @ViewModelScoped
    fun getAuthEndPoints(@ApplicationContext context: Context?,prefsUtil: SharedPreferences) : EndPoints {
            return provideEndPoints(provideRetrofitBuilder(context!!,prefsUtil), BuildConfig.baseUrl).create(
            EndPoints::class.java
        )
    }
    @Provides
    fun getAuthInterceptor(token : String,applicationContext: Context,prefsUtil : SharedPreferences): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val url: HttpUrl =
                original.url.newBuilder().addQueryParameter(LANG,
                    prefsUtil.getString(Constants.LOCALE_LANGUAGE, "en")?:"en")
                    .addQueryParameter(API_KEY_K,
                        API_KEY).build()// add query in request
            chain.proceed(
                original.newBuilder().url(url)
                    .header(Constants.TOKENKEY, "Bearer "+token)
                    .header("Accept","application/json")
                    .method(original.method, original.body)
                    .build()
            )
        }
    }

    @Provides
    fun provideEndPoints(client: OkHttpClient,baseUrl : String): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .build()
          //  .create(EndPoints::class.java)
    }

}