package com.example.gestionhabitos.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {

    private const val BASE_URL = "https://azogcumdxcahxdoassax.supabase.co/rest/v1/"
    private const val API_KEY = "TU_API"

    // 🚩 CONFIGURACIÓN DE GSON: Esto es lo que hace magia con @Expose
    private val gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation() // 🛡️ Solo envía/recibe lo que tú marques con @SerializedName o @Expose
        .create()

    private val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("apikey", API_KEY)
            .addHeader("Authorization", "Bearer $API_KEY")
            .addHeader("Content-Type", "application/json")
            .addHeader("Prefer", "return=representation")
            .build()
        chain.proceed(request)
    }.build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            // 🚩 PASAMOS EL GSON CONFIGURADO AQUÍ:
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: SupabaseApiService by lazy {
        retrofit.create(SupabaseApiService::class.java)
    }
}