package br.edu.ifsc.eventos.services

import br.edu.ifsc.eventos.entities.Event
import br.edu.ifsc.eventos.entities.Talk
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EventsService {

    @GET("events")
    fun getEvents(): Call<List<Event>>

    @GET("talks")
    fun getTalks(): Call<List<Talk>>

    @GET("talks/?eventId=eventId")
    fun getEventTalks(@Query("eventId") eventId: String): Call<List<Talk>>

    @GET("talks/?eventId=eventId&day=day")
    fun getEventDayTalks(@Query("eventId") eventId: String, @Query("day") day: String): Call<List<Talk>>
}