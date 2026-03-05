package com.example.gestionhabitos.model.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface SendGridApiService {
    @Headers("Content-Type: application/json")
    @POST("v3/mail/send")
    suspend fun enviarCorreo(
        @Header("Authorization") token: String,
        @Body request: SendGridMailRequest
    ): Response<Unit>
}

data class SendGridMailRequest(
    val personalizations: List<Personalization>,
    val from: EmailUser,
    val subject: String,
    val content: List<MailContent>
)

data class Personalization(
    val to: List<EmailUser>
)

data class EmailUser(
    val email: String
)

data class MailContent(
    val type: String = "text/plain",
    val value: String
)