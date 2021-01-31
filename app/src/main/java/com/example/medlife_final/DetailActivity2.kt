package com.example.medlife_final

import android.app.PendingIntent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DetailActivity2 : AppCompatActivity() {
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    lateinit var pendingIntent: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail2)
        /*btnStart = findViewById(R.id.btnStartService)
        btnStop = findViewById(R.id.btnStopService)*/

    }
}