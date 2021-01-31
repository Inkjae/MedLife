package com.example.medlife_final

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register2.*

class Register2 : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    lateinit var hourbf : Spinner
    lateinit var minbf : Spinner
    lateinit var hourlunch : Spinner
    lateinit var minlunch : Spinner
    lateinit var hourdn : Spinner
    lateinit var mindn : Spinner
    lateinit var hourslp : Spinner
    lateinit var minslp : Spinner

    lateinit var breakfasttime:String
    lateinit var breakfasthr:String
    lateinit var breakfastmin:String
    lateinit var lunchtime:String
    lateinit var lunchhr:String
    lateinit var lunchmin:String
    lateinit var dinnertime:String
    lateinit var dinnerhr:String
    lateinit var dinnermin:String
    lateinit var sleeptime:String
    lateinit var sleephr:String
    lateinit var sleepmin:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        breakfasthr()
        breakfastmin()
        lunch()
        dinner()
        sleeping()
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val blooddata = intent.getStringExtra("blooddata")
        val allergy = intent.getStringArrayListExtra("allergy")
        val disease = intent.getStringArrayListExtra("disease")
        previous.setOnClickListener{
            val myIntent = Intent(this, Register::class.java)
            startActivity(myIntent)
        }
        confirm.setOnClickListener{
            val user = hashMapOf(
                    "Id" to currentUser?.uid.toString(),
                    "Name" to currentUser?.displayName.toString(),
                    "Email" to currentUser?.email.toString(),
                    "Blood" to blooddata,
                    "Breakfast" to breakfasttime,
                    "Lunch" to lunchtime,
                    "Dinner" to dinnertime,
                    "Sleep" to sleeptime,
                    "Allergy" to allergy,
                    "Disease" to disease
            )
            val datab = FirebaseFirestore.getInstance()
            datab.collection("users")
                    .get()
                    .addOnCompleteListener {
                        val result:StringBuffer = StringBuffer()
                        if(it.isSuccessful){
                            for(document in it.result!!){
                                if(document.data.getValue("Id").toString()== currentUser!!.uid.toString()){
                                    datab.collection("users")
                                            .document("${document.id}").set(user)
                                }

                            }
                        }
                    }
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }
    }

    fun breakfasthr(){
        hourbf = findViewById(R.id.hrbf) as Spinner
        val hr = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24")
        val min = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
        hourbf.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hr)
        hourbf.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                breakfasthr = hr.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    fun breakfastmin(){
        minbf = findViewById(R.id.minbf) as Spinner
        val hr = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24")
        val min = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
        minbf.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,min)
        minbf.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                breakfastmin = min.get(position)
                breakfasttime = breakfasthr+":"+breakfastmin+":00"
                println(breakfasttime)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    fun lunch(){
        hourlunch = findViewById<Spinner>(R.id.hrlunch)
        minlunch = findViewById<Spinner>(R.id.minlunch)
        val hr = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24")
        val min = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
        hourlunch.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hr)
        hourlunch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                lunchhr = hr.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        minlunch.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,min)
        minlunch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                lunchmin = min.get(position)
                lunchtime = lunchhr+":"+lunchmin+":00"
                println(lunchtime)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    fun dinner(){
        hourdn = findViewById<Spinner>(R.id.hrdn)
        mindn = findViewById<Spinner>(R.id.mindn)
        val hr = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24")
        val min = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
        hourdn.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hr)
        hourdn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dinnerhr = hr.get(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        mindn.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,min)
        mindn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dinnermin = min.get(position)
                dinnertime = dinnerhr+":"+dinnermin+":00"
                println(dinnertime)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    fun sleeping(){
        hourslp = findViewById<Spinner>(R.id.hrslp)
        minslp = findViewById<Spinner>(R.id.minslp)
        val hr = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24")
        val min = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
        hourslp.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hr)
        hourslp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sleephr = hr.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        minslp.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,min)
        minslp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sleepmin = min.get(position)
                sleeptime = sleephr+":"+sleepmin+":00"
                println(sleeptime)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}