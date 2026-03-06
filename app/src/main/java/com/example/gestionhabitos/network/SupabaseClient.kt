package com.example.gestionhabitos.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {

    private const val BASE_URL = "https://azogcumdxcahxdoassax.supabase.co/rest/v1/"
    private const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImF6b2djdW1keGNhaHhkb2Fzc2F4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI3NjM0MjAsImV4cCI6MjA4ODMzOTQyMH0.1QaF0jUcEmmSQG_g1Zl-Hmm28DQb0lxN2aXUf4M3Cik"

    // 🚩 CONFIGURACIÓN DE GSON: Indispensable para que @Expose funcione
    private val gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation() // Esto hace que ignore lo que no tenga @Expose o lo que tenga serialize=false
        .create()

    // 🚩 INTERCEPTOR: Agrega las llaves de seguridad automáticamente
    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("apikey", API_KEY)
            .addHeader("Authorization", "Bearer $API_KEY")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson)) // Usamos el gson configurado
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}