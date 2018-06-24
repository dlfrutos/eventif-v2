package br.edu.ifsc.eventos.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import br.edu.ifsc.database.DatabaseHelper
import br.edu.ifsc.eventos.R
import br.edu.ifsc.eventos.entities.Talk
import br.edu.ifsc.eventos.fragments.HomeFragment
import br.edu.ifsc.eventos.fragments.ScheduleFragment
import br.edu.ifsc.eventos.services.RetrofitInitializer
import kotlinx.android.synthetic.main.activity_event.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventActivity : AppCompatActivity() {
    var eventDescription = ""
    var contactInfo = ""
    var eventID = ""

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        eventID = intent.getStringExtra("eventID")
        title = intent.getStringExtra("eventName")
        eventDescription = intent.getStringExtra("eventDescription")
        contactInfo = intent.getStringExtra("contactInfo")

        updateBottomNavigation(eventID)
        navigation.setOnNavigationItemSelectedListener(navigationListener)

        if (savedInstanceState == null)
            navigation.selectedItemId = R.id.navigation_home
    }

    private fun updateBottomNavigation(eventID: String): MutableList<Talk> {

        var talks = mutableListOf<Talk>()
        var call = RetrofitInitializer().eventsService().getEventTalks(eventID)
        val database = DatabaseHelper(this)
        call.enqueue(object : Callback<List<Talk>?> {
            override fun onResponse(call: Call<List<Talk>?>?, response: Response<List<Talk>?>?) {
                response?.body()?.let(talks::addAll)
                database.addTalks(talks)
                var days = mutableListOf<String>()
                for (talk in talks)
                    if (!days.contains(talk.day))
                        days.add(talk.day)

                var menu = navigation.menu
                var menuIndex = 1
                for (day in 0..(days.size - 1)) {
                    menu.getItem(menuIndex).isEnabled = true
                    menu.getItem(menuIndex).title = days[day]
                    menuIndex++
                }
            }

            override fun onFailure(call: Call<List<Talk>?>?,
                                   t: Throwable?) {
                talks.addAll(database.getTalks(eventID))
                var days = mutableListOf<String>()
                for (talk in talks)
                    if (!days.contains(talk.day))
                        days.add(talk.day)

                var menu = navigation.menu
                var menuIndex = 1
                for (day in 0..(days.size - 1)) {
                    menu.getItem(menuIndex).isEnabled = true
                    menu.getItem(menuIndex).title = days[day]
                    menuIndex++
                }

            }
        })
        return talks
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        val transaction = supportFragmentManager.beginTransaction()
        val attachedFragment = supportFragmentManager.findFragmentById(R.id.layoutFragment)

        if (attachedFragment != null)
            transaction.remove(attachedFragment)

        when (item.itemId) {
            R.id.navigation_home -> {
                val fragmentHome = HomeFragment()
                val args = Bundle()
                args.putString("eventDescription", eventDescription)
                args.putString("contactInfo", contactInfo)
                fragmentHome.arguments = args
                transaction.setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)
                transaction.add(R.id.layoutFragment, fragmentHome, "home")
            }
            R.id.navigation_day1 -> {
                val scheduleFragment = ScheduleFragment()
                val args = Bundle()
                args.putString("eventID", eventID)
                args.putString("day", navigation.menu.getItem(1).title.toString())
                scheduleFragment.arguments = args
                transaction.setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)
                transaction.add(R.id.layoutFragment, scheduleFragment, "dayOne")
            }
            R.id.navigation_day2 -> {

                val scheduleFragment = ScheduleFragment()
                val args = Bundle()
                args.putString("eventID", eventID)
                args.putString("day", navigation.menu.getItem(2).title.toString())
                scheduleFragment.arguments = args
                transaction.setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)
                transaction.add(R.id.layoutFragment, scheduleFragment, "dayTwo")
            }
            R.id.navigation_all -> {
                val scheduleFragment = ScheduleFragment()
                val args = Bundle()
                args.putString("eventID", eventID)
                args.putString("day", "")
                scheduleFragment.arguments = args
                transaction.setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)
                transaction.add(R.id.layoutFragment, scheduleFragment, "all")
            }
        }
        transaction.commit()
        true
    }
}