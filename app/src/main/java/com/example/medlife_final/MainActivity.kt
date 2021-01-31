package com.example.medlife_final

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

private lateinit var mAuth : FirebaseAuth
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        username.text = currentUser?.displayName
        var addname = findViewById(R.id.addName) as Button
        var addphoto = findViewById(R.id.addPhoto) as Button
        var add = findViewById(R.id.add) as Button



        addname.visibility = View.INVISIBLE
        addphoto.visibility = View.INVISIBLE
        add.setOnClickListener {
            if (addname.visibility== View.INVISIBLE && addphoto.visibility== View.INVISIBLE) {
                addname.visibility = View.VISIBLE
                addphoto.visibility = View.VISIBLE
            }
            else{
                addname.visibility = View.INVISIBLE
                addphoto.visibility = View.INVISIBLE
            }
        }
        addname.setOnClickListener {
            val myIntent = Intent(this, SearchActivity::class.java)
            startActivity(myIntent) }

        addphoto.setOnClickListener {
            val myIntent = Intent(this, CameraActivity::class.java)
            startActivity(myIntent) }


    }
}