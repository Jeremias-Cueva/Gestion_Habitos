package com.example.gestionhabitos.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {

    // Tu URL exacta de Supabase terminada en /rest/v1/
    private const val BASE_URL = "https://azogcumdxcahxdoassax.supabase.co/rest/v1/"

    // ⚠️ REEMPLAZA ESTO: Pega aquí la llave larguísima que dice "anon" y "public" en Supabase
    private const val API_KEY = "TU_LLAVE_AQUI"

    // Este es el "Guardia de Seguridad" que inyecta tu llave en cada petición
    private val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("apikey", API_KEY)
            .addHeader("Authorization", "Bearer $API_KEY")
            .build()
        chain.proceed(request)
    }.build()

    // Configuración principal de Retrofit
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) // Le pasamos el cliente con la llave
            .addConverterFactory(GsonConverterFactory.create()) // Para que entienda los JSON
            .build()
    }

    // Instancia lista para usarse en tu Repository
    val apiService: SupabaseApiService by lazy {
        retrofit.create(SupabaseApiService::class.java)
    }
}