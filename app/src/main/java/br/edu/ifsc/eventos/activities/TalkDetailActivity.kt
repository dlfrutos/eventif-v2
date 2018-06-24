package br.edu.ifsc.eventos.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import br.edu.ifsc.eventos.R
import kotlinx.android.synthetic.main.activity_talk_detail.*
import kotlinx.android.synthetic.main.item_talk.*

class TalkDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val talkName = intent.getStringExtra("talkName")
        val talkDay = intent.getStringExtra("talkDay")
        val talkSpeaker = intent.getStringExtra("talkSpeaker")
        val talkDescription = intent.getStringExtra("talkDescription")
        val talkTime = intent.getStringExtra("talkTime")
        talkDetailTitle.setText(talkName)
        talkDetailDate.setText(talkDay)
        talkDetailHour.setText(talkTime)
        talkDetailSpeaker.setText(talkSpeaker)
        talkDetailDescription.setText(talkDescription)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
