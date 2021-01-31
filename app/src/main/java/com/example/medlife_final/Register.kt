package com.example.medlife_final

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class Register : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var signOut:Button
    private lateinit var mAuth : FirebaseAuth
    lateinit var bloodtype : Spinner
    lateinit var selectDisease : Button
    lateinit var diseaseView : TextView
    lateinit var selectAllergy : Button
    lateinit var AllergyView : TextView
    lateinit var blooddata : String
    var allergy = arrayListOf<String>()
    var disease = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        welcome.append(currentUser?.displayName.toString())

        Blood()
        Disease()
        Allergy()

        regnext.setOnClickListener{
            /*val user = hashMapOf(
                "Id" to currentUser?.uid.toString(),
                "Name" to currentUser?.displayName.toString(),
                "Email" to currentUser?.email.toString(),
                "Blood" to blooddata,
                "Breakfast" to null,
                "Lunch" to null,
                "Dinner" to null,
                "Sleep" to null,
                "EmergancyCall" to null,
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
                }*/
            val myIntent = Intent(this, Register2::class.java)
            myIntent.putExtra("blooddata",blooddata)
            myIntent.putExtra("allergy",allergy)
            myIntent.putExtra("disease",disease)
            startActivity(myIntent)
        }
        nextpage.setOnClickListener {
            val myIntent = Intent(this, Register2::class.java)
            startActivity(myIntent)
        }



    }
    fun Allergy(){
        selectAllergy = findViewById(R.id.selectAllergy) as Button
        AllergyView = findViewById(R.id.allergyView) as TextView

        selectAllergy.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val allergyArray = arrayOf("อาหารทะเล","แป้งสาลี","ผลิตภัณฑ์จากนมวัว","เกสรดอกไม้","ไข่","ถั่ว","รังแคของสัตว์","ไรฝุ่น","ผงชูรส","อื่นๆ")
            val checkedAllergyArray = booleanArrayOf(false,false,false,false,false,false,false,false,false,false)
            val diseaseList = Arrays.asList(*allergyArray)
            builder.setTitle("สิ่งที่คุณแพ้")
            builder.setMultiChoiceItems(allergyArray,checkedAllergyArray) { dialog: DialogInterface, which:Int, isChecked:Boolean ->
                checkedAllergyArray[which] = isChecked
                val currentItem = diseaseList[which]
            }
            builder.setPositiveButton("OK"){ dialog: DialogInterface, which:Int ->
                for(i in checkedAllergyArray.indices){
                    val checked = checkedAllergyArray[i]
                    if(checked){
                        allergyView.text = allergyView.text.toString() + diseaseList[i] + "  "

                        allergy.add(diseaseList[i].toString())
                    }
                }
            }
            builder.setNegativeButton("Cancel"){ dialog: DialogInterface, which:Int ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        deleteall.setOnClickListener{
            allergyView.text = null
        }
    }

    fun Disease(){
        selectDisease = findViewById(R.id.selectDisease) as Button
        diseaseView = findViewById(R.id.diseaseView) as TextView

        selectDisease.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val diseaseArray = arrayOf("โรคเบาหวาน","ความดันโลหิตสูง","โรคหัวใจและหลอดเลือด","โรคไต","ไขมันในเลือดสูง","โรคหอบหืด","โรคภูมิแพ้","โรคเลือดจาง","โรคไขมันสูง","โรคไมเกรน","โรคไทรอยด์","โรคลมบ้าหมูหรือลมชัก")
            val checkedDiseaseArray = booleanArrayOf(false,false,false,false,false,false,false,false,false,false,false,false)
            val diseaseList = Arrays.asList(*diseaseArray)
            builder.setTitle("โรคประจำตัวของคุณ")
            builder.setMultiChoiceItems(diseaseArray,checkedDiseaseArray) { dialog: DialogInterface, which:Int, isChecked:Boolean ->
                checkedDiseaseArray[which] = isChecked
                val currentItem = diseaseList[which]
            }
            builder.setPositiveButton("OK"){ dialog: DialogInterface, which:Int ->
                for(i in checkedDiseaseArray.indices){
                    val checked = checkedDiseaseArray[i]
                    if(checked){
                        diseaseView.text = diseaseView.text.toString() + diseaseList[i] + "  "

                        disease.add(diseaseList[i].toString())
                    }
                }
            }
            builder.setNegativeButton("Cancel"){ dialog: DialogInterface, which:Int ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        deletedis.setOnClickListener{
            diseaseView.text = null
        }
    }

    fun Blood(){
        bloodtype = findViewById(R.id.bloodType) as Spinner
        val options = arrayOf("A RhD positive","A RhD negative","B RhD positive","B RhD negative",
                "O RhD positive","O RhD negative","AB RhD positive","AB RhD negative")
        bloodtype.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)
        bloodtype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                blood.text = options.get(position)
                blooddata = options.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                blood.text = "please select your blood type"
            }
        }
    }

}