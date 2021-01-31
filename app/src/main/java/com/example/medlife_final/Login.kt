package com.example.medlife_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    //lateinit var gso: GoogleSignInOptions
    //val RC_SIGN_IN: Int = 1
    lateinit var signOut:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val signIn = findViewById<View>(R.id.signInBtn) as SignInButton
        //Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()

        signOut = findViewById<View>(R.id.signOutBtn) as Button
        signOut.visibility = View.INVISIBLE
        var registerbtn = findViewById(R.id.registerbtn) as Button
        registerbtn.setOnClickListener {
            val myIntent = Intent(this, Register::class.java)
            startActivity(myIntent) }
        signInBtn.setOnClickListener{ view: View? -> signInGoogle()
        }

    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("Login", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("Login", "Google sign in failed", e)
                }
            }else{
                Log.w("Login", exception.toString())
            }

        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val user = hashMapOf(
            "Id" to currentUser?.uid.toString(),
            "Name" to currentUser?.displayName.toString(),
            "Email" to currentUser?.email.toString(),
            "Blood" to null,
            "Breakfast" to null,
            "Lunch" to null,
            "Dinner" to null,
            "Sleep" to null,
            "EmergencyCall" to null,
            "Allergy" to null,
            "Disease" to null
        )
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithCredential:success")
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    println("Login hey")
                    val datab = FirebaseFirestore.getInstance()
                    val user = hashMapOf(
                            "Id" to currentUser!!.uid.toString(),
                            "Name" to currentUser!!.displayName.toString(),
                            "Email" to currentUser!!.email.toString(),
                            "Blood" to null,
                            "Breakfast" to null,
                            "Lunch" to null,
                            "Dinner" to null,
                            "Sleep" to null,
                            "EmergencyCall" to null,
                            "Allergy" to null,
                            "Disease" to null
                    )
                    datab.collection("users").document("${currentUser.email}").set(user)
                    /*datab.collection("users")
                        .get()
                        .addOnCompleteListener {
                            val result:StringBuffer = StringBuffer()
                            if(it.isSuccessful) {
                                for(document in it.result!!){
                                    if(document.data.getValue("Id").toString()==currentUser?.uid.toString()){
                                        val intent = Intent(this , Register::class.java)
                                        startActivity(intent)
                                    }
                                    else{
                                        datab.collection("users")
                                            .add(user)
                                            .addOnSuccessListener { documentReference ->
                                                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w(TAG, "Error adding document", e)
                                            }
                                        val intent = Intent(this , Register::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }*/
                    //if()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                }
            }

        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener{ view: View? -> mGoogleSignInClient.signOut().addOnCompleteListener{ task: Task<Void> -> dispTxt.text = " "
            signOut.visibility = View.INVISIBLE
        }
        }
        /*signOutBtn.setOnClickListener{
            mAuth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }*/
        /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if(requestCode == RC_SIGN_IN){
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

                handleResult(task)

            }

        }*/
        /*@Override
        private fun handleResult(completedTask: Task<GoogleSignInAccount>){
            try {
                val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
                    updateUI(account)
            }catch (e: ApiException){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()

            }

        }*/
        /*private fun updateUI(account: GoogleSignInAccount){
            val dispTxt = findViewById<View>(R.id.dispTxt) as TextView
            dispTxt.text=account.displayName
            dispTxt.append(account.id)
            //dispTxt.append(account.idToken)
            signOut.visibility = View.VISIBLE
            signOut.setOnClickListener{ view: View? -> mGoogleSignInClient.signOut().addOnCompleteListener{ task: Task<Void> -> dispTxt.text = " "
                    signOut.visibility = View.INVISIBLE
            }
            }*/
    }


}