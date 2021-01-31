package com.example.medlife_final

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import kotlin.collections.ArrayList


class SearchActivity : AppCompatActivity() {
    val db = Firebase.firestore
    var name:String = ""
    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }
    val arraylist = ArrayList<Item>()
    val displayList = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        getData()

    }

    private fun createExampleList(){
        val exampleList = arrayListOf<String>()
        //exampleList.add(new )
    }
    /*private fun generateDummyList(): List<Item> {
        val list = ArrayList<Item>()
        val datab = FirebaseFirestore.getInstance()
        datab.collection("MedLife")
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        /*val name = result.append(document.data.getValue("Name"))
                        val groups = result.append(document.data.getValue("Groups"))*/
                        val item = Item("hello", "bye")
                        list += item
                    }
                }
            }
        return list
    }*/

    private fun generateDummyList(size: Int): List<Item> {
        val list = ArrayList<Item>()
        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.pill3
                1 -> R.drawable.pill3
                else -> R.drawable.pill3
            }
            val item = Item(drawable, "Item $i", "Line 2")
            list += item
        }
        return list
    }

    fun getData(){
        val datab = FirebaseFirestore.getInstance()
        datab.collection("MedLife")
                .get()
                .addOnCompleteListener {
                    val result:StringBuffer = StringBuffer()

                    if(it.isSuccessful){
                        for(document in it.result!!){
                            result.append(document.data.getValue("Name")).append("\n\n")
                            var name = document.data.getValue("Name").toString()
                            var use = document.data.getValue("Use").toString()
                            arraylist.add(Item(1,"$name","$use"))
                                    //.append(document.data.getValue("Groups")).append("\n\n")
                        }
                        displayList.addAll(arraylist)
                        val myAdapter = MedAdapter(displayList)
                        recycler_view.layoutManager = LinearLayoutManager(this)
                        recycler_view.adapter = myAdapter
                    }
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.listmed, menu)
        val menuItem = menu!!.findItem(R.id.search)
        if(menuItem!= null){
            val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView
            val edittxt = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            edittxt.hint = "Search...."
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        arraylist.forEach{
                            if(it.text1.toLowerCase(Locale.getDefault()).contains(search)){
                                displayList.add(it)
                            }
                        }
                        recycler_view.adapter!!.notifyDataSetChanged()
                    }
                    else{
                        displayList.clear()
                        displayList.addAll(arraylist)
                        recycler_view.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }

            })
        }
        return  super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}


