package br.edu.ifsc.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.support.annotation.RequiresApi
import br.edu.ifsc.eventos.entities.Event
import br.edu.ifsc.eventos.entities.Talk
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASENAME, null, DATABASEVERSION) {

    companion object {
        private val DATABASENAME = "eventif.db"
        private val DATABASEVERSION = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(database: SQLiteDatabase?) {
        drop(database)
        var query = "CREATE TABLE EVENT (id INT PRIMARY KEY, name TEXT, description TEXT, contactInfo TEXT); "
        database!!.execSQL(query)
        database.execSQL("CREATE TABLE TALKS (id INT PRIMARY KEY, name TEXT, eventId INT, day TEXT, speaker TEXT, time TEXT, description TEXT);")

        val now = LocalDateTime.now()
        var formatter = DateTimeFormatter.ofPattern("dd/mm/aaaa HH:MM:ss")

    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        drop(database)
        onCreate(database)
    }

    private fun drop(database: SQLiteDatabase?) {
        database!!.execSQL("DROP TABLE IF EXISTS TALKS")
        database!!.execSQL("DROP TABLE IF EXISTS EVENT")
    }

    fun addEvents(events: List<Event>) {
        val database = this.writableDatabase
        drop(database)
        onCreate(database)
        for(event: Event in events) {
            var values = ContentValues()
            values.put("id", event.id)
            values.put("name", event.name)
            values.put("description", event.description)
            values.put("contactInfo", event.contactInfo)
            database.insert("EVENT", null, values)
        }
        database.close()
    }

    fun addTalks(talks: List<Talk>) {
        val database = this.writableDatabase
        database.execSQL("DROP TABLE TALKS")
        database.execSQL("CREATE TABLE TALKS (id INT PRIMARY KEY, name TEXT, eventId INT, day TEXT, speaker TEXT, time TEXT, description TEXT);")
        for(talk: Talk in talks) {
           // var query = "UPDATE TALKS SET NAME = " + "\"" + talk.name + "\"" +
            //database.execSQL("")
            var values = ContentValues()
            values.put("id", talk.id)
            values.put("name", talk.name)
            values.put("eventId", talk.eventId)
            values.put("day", talk.day)
            values.put("speaker", talk.speaker)
            values.put("time", talk.time)
            values.put("description", talk.description)
            database.insert("TALKS", null, values)
        }
        database.close()
    }

    fun alterTalks(id: String, talks: List<Talk>) {
        val database = this.writableDatabase

        for(talk: Talk in talks) {
            var values = ContentValues()
            values.put("id", talk.id)
            values.put("name", talk.name)
            values.put("eventId", talk.eventId)
            values.put("day", talk.day)
            values.put("speaker", talk.speaker)
            values.put("time", talk.time)
            database.insert("TALKS", null, values)
        }
        database.close()
    }

    fun getEvents(): List<Event> {
        val db = this.writableDatabase
        val list = ArrayList<Event>()
        val cursor: Cursor
        cursor = db.rawQuery("SELECT * FROM EVENT", null)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    val id = cursor.getString(cursor.getColumnIndex("id"))
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val description = cursor.getString(cursor.getColumnIndex("description"))
                    val contactInfo = cursor.getString(cursor.getColumnIndex("contactInfo"))
                    val event = Event(id.toLong(), name, description, contactInfo)
                    list.add(event)
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return list
    }

    fun getTalks(eventID: String): List<Talk> {
        val db = this.writableDatabase
        val list = ArrayList<Talk>()
        val cursor: Cursor
        cursor = db.rawQuery("SELECT * FROM TALKS WHERE eventId =" + eventID, null)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    val id = cursor.getString(cursor.getColumnIndex("id"))
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val eventId = cursor.getString(cursor.getColumnIndex("eventId"))
                    val day = cursor.getString(cursor.getColumnIndex("day"))
                    val speaker = cursor.getString(cursor.getColumnIndex("speaker"))
                    val time = cursor.getString(cursor.getColumnIndex("time"))
                    val description = cursor.getString(cursor.getColumnIndex("description"))
                    val talk = Talk(id.toLong(), name, eventId.toLong(), day, speaker, time, description)
                    list.add(talk)
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return list
    }

    fun getTalksByDay(eventID: String, day: String): List<Talk> {
        val db = this.writableDatabase
        val list = ArrayList<Talk>()
        val cursor: Cursor
        cursor = db.rawQuery("SELECT * FROM TALKS WHERE eventId = " + eventID + " AND day = " + "\"" + day + "\"", null)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    val id = cursor.getString(cursor.getColumnIndex("id"))
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val eventId = cursor.getString(cursor.getColumnIndex("eventId"))
                    val day = cursor.getString(cursor.getColumnIndex("day"))
                    val speaker = cursor.getString(cursor.getColumnIndex("speaker"))
                    val time = cursor.getString(cursor.getColumnIndex("time"))
                    val description = cursor.getString(cursor.getColumnIndex("description"))
                    val talk = Talk(id.toLong(), name, eventId.toLong(), day, speaker, time, description)
                    list.add(talk)
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return list
    }
}