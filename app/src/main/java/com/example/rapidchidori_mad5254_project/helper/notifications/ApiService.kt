package com.example.rapidchidori_mad5254_project.helper.notifications

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAAGP__5qk:APA91bGTVM3xOu0x5V300Ci-GcDHlux9S2HkYxeAhzJH85LAckT7NiXmQrXKjKcrhs9Q07ocb2ATtPnV8244lUwAg39N5yRfAekCdmyMBSeJQb87YI1u77Eop6RGswvUeY8DI-192AfP"
    )

    @POST("fcm/send")
    fun sendNotification(@Body body: NotificationSender?): Call<MyResponse>?
}