package com.example.rorders.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rorders.R
import com.example.rorders.admin.AdminMainActivity
import com.example.rorders.kitchen.KitchenMainActivity
import com.example.rorders.staff.StaffMainActivity
import com.example.rorders.staffnew.StaffMainActivityNew
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    lateinit var mContext:Context
    lateinit var username:EditText
    lateinit var password:EditText
    lateinit var signInBtn:Button
    lateinit var signUpBtn:Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var userType:String=""
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mContext=this
        db=Firebase.firestore
        init()
        onClick()
    }
    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
    private fun init(){
        auth = Firebase.auth
        username=findViewById(R.id.username_edt)
        password=findViewById(R.id.password_edt)
        signInBtn=findViewById(R.id.submitBtn)
        signUpBtn=findViewById(R.id.signupBtn)


    }
    private fun onClick(){
        signInBtn.setOnClickListener {
           loginUser()
        }
        /*signUpBtn.setOnClickListener {
            signUpUser()
        }*/
    }
    private fun signUpUser(){
        val user = hashMapOf(
            "name" to "Swaraj",
            "email" to "swaraj.s@mobatia.com",
            "type" to "staff"
        )

        // Add a new document with a generated ID
        db.collection("users_table")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.e("id", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("error", "Error adding document", e)
            }
      /*  val email = username.text.toString()
        val pass = password.text.toString()

        if (email.isBlank() || pass.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Signed Up Failed!", Toast.LENGTH_SHORT).show()
            }
        }*/
    }
    private fun loginUser(){
        val email = username.text.toString()
        val pass = password.text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { it ->
                if (it.isSuccessful) {
                    db.collection("users_table").get().
                    addOnSuccessListener{
                        for ( i in it.documents.indices) {
                            if (email == it.documents[i].get("email").toString()){
                                userType=it.documents[i].get("type").toString()
                            }
                        }

                        if (userType == "admin"){
                            val i = Intent(this@LoginActivity, AdminMainActivity::class.java)
                            startActivity(i)
                        }else if (userType == "staff"){
                            val i = Intent(this@LoginActivity, StaffMainActivityNew::class.java)
                            startActivity(i)
                        }else if (userType == "kitchen"){
                            val i = Intent(this@LoginActivity, KitchenMainActivity::class.java)
                            startActivity(i)
                        }else{
                            Toast.makeText(mContext, "User does not exist", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show()
                    }
                } else
                    Toast.makeText(mContext, "Log In failed ", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(mContext, "Please Enter Email", Toast.LENGTH_SHORT).show()
        }





    }
    override fun onStart() {
        super.onStart()
        // in on start method checking if
        // the user is already sign in.

    }
    }
