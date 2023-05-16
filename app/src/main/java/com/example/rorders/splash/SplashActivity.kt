package com.example.rorders.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.rorders.R
import com.example.rorders.admin.AdminMainActivity
import com.example.rorders.constants.PreferenceManager
import com.example.rorders.kitchen.KitchenMainActivity
import com.example.rorders.login.LoginActivity
import com.example.rorders.staff.StaffMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    lateinit var mContext: Context
    val SPLASH_TIME_OUT:Long = 3000
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext=this
        auth = Firebase.auth
        db= Firebase.firestore
        Handler(Looper.myLooper()!!).postDelayed({

            var userTypeLoggedIn = ""
            val user: FirebaseUser? = auth.currentUser

            if (user != null) {
                db.collection("users_table").get().
                addOnSuccessListener{
                    for( i in it.documents.indices){
                        if (it.documents[i].get("email").toString() == user.email.toString()){
                            userTypeLoggedIn = it.documents[i].get("type").toString()
                        }
                        Log.e("usefor",userTypeLoggedIn)
                    }
                    Log.e("use",userTypeLoggedIn)
                    if (userTypeLoggedIn.equals("admin")){
                        PreferenceManager.setAdminValue(mContext,"1")
                        val i = Intent(mContext, AdminMainActivity::class.java)
                        startActivity(i)
                        finish()
                    }else if (userTypeLoggedIn.equals("staff")){
                        val i = Intent(mContext, StaffMainActivity::class.java)
                        startActivity(i)
                        finish()
                    }else if (userTypeLoggedIn.equals("kitchen")){
                        val i = Intent(mContext, KitchenMainActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                }


                //finish()
            }else{
                startActivity(Intent(mContext, LoginActivity::class.java))
                finish()
            }


        }, SPLASH_TIME_OUT)
    }
}