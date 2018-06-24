package br.edu.ifsc.eventos.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import br.edu.ifsc.database.DatabaseHelper
import br.edu.ifsc.eventos.R
import br.edu.ifsc.eventos.entities.ScheduleAdapter
import br.edu.ifsc.eventos.entities.Talk
import br.edu.ifsc.eventos.services.RetrofitInitializer
import kotlinx.android.synthetic.main.fragment_schedule.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.FieldPosition


class ScheduleFragment : Fragment() {
    var talks = mutableListOf<Talk>()
    val talkList = mutableListOf<Talk>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onResume() {
        super.onResume()
        val eventID = arguments!!.getString("eventID")
        val day = arguments!!.getString("day")
        Log.d(tag, day)
        updateTalks(eventID, day, this.context!!)

        val layoutManager = LinearLayoutManager(context)
        scheduleRecyclerView.layoutManager = layoutManager
    }

    private fun updateTalks(eventID: String, day: String, context: Context) {
        talks.clear()
        var call = RetrofitInitializer().eventsService().getEventDayTalks(eventID, day)
        val database = DatabaseHelper(this!!.getContext()!!)

        if (day.isNullOrEmpty())
            call = RetrofitInitializer().eventsService().getEventTalks(eventID)

        call.enqueue(object : Callback<List<Talk>?> {
            override fun onResponse(call: Call<List<Talk>?>?, response: Response<List<Talk>?>?) {
                response?.body()?.let(talks::addAll)
                scheduleRecyclerView.adapter = ScheduleAdapter(talks, context)
            }

            override fun onFailure(call: Call<List<Talk>?>?,
                                   t: Throwable?) {
                if(day == ""){
                    talks.addAll(database.getTalks(eventID))
                } else {
                    talks.addAll(database.getTalksByDay(eventID, day))
                }
                if(talks.isEmpty()){
                    talks.add(Talk(0, getString(R.string.none_available_event), 0, "", speaker = "", time = "", description = ""))
                    scheduleRecyclerView.adapter = ScheduleAdapter(talks, context)
                } else {
                    for (talk in talks)
                        talkList.add(Talk(talk.id, talk.name, talk.eventId, talk.day, talk.speaker, talk.time, talk.description))
                    scheduleRecyclerView.adapter = ScheduleAdapter(talkList, context)
                }
            }
        })
    }
}
