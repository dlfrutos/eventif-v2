package br.edu.ifsc.eventos.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.105:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun eventsService() = retrofit.create(EventsService::class.java)


}