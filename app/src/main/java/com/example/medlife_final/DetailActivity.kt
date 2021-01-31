package com.example.medlife_final

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    lateinit var medName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        medName = intent.getStringExtra("medicine")
        println(medName+"medname")
        medname.text = medName
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return  super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.next) {
            val myIntent = Intent(this, DetailActivity2::class.java)
            startActivity(myIntent)
        }
        return super.onOptionsItemSelected(item)
    }


    fun getData(){
        val datab = FirebaseFirestore.getInstance()
        datab.collection("MedLife")
            .get()
            .addOnCompleteListener {
                val result:StringBuffer = StringBuffer()
                if(it.isSuccessful){
                    for(document in it.result!!){
                        if(document.data.getValue("Name").toString()==medName){
                            //result.append(document.data.getValue("Name")).append("\n\n")
                            //.append(document.data.getValue("Groups")).append("\n\n")
                            medgroup.text = "กลุ่มยา : "+document.data.getValue("Groups").toString()
                            meduse.text = "สรรพคุณ : "+document.data.getValue("Use").toString()
                            medwarn.text = "คำเตือนการใช้ยา : "+document.data.getValue("Warning").toString()
                        }

                    }
                }
            }
    }
}