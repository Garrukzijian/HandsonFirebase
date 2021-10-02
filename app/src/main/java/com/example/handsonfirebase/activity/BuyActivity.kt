package com.example.handsonfirebase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.handsonfirebase.R
import com.example.handsonfirebase.model.Chat
import com.example.handsonfirebase.model.NotificationData
import com.example.handsonfirebase.model.PushNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_buy.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class BuyActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        btnProceed.setOnClickListener{
            val Product = etProduct.text.toString()
            val Location=etLocation.text.toString()
            val Price = etPrice.text.toString()
            var time =getNow()
            if(Product.isEmpty()){
                Toast.makeText(applicationContext,"Product is required", Toast.LENGTH_SHORT).show()
            }
            if(Location.isEmpty()){
                Toast.makeText(applicationContext,"Location is required", Toast.LENGTH_SHORT).show()
            }
            if(Price.isEmpty()){
                Toast.makeText(applicationContext,"Price is required", Toast.LENGTH_SHORT).show()
            }
            transport_data(firebaseUser!!.uid,Product,time, Price,Location)
        }
        image_buy.setOnClickListener {
            onBackPressed()
        }
    }

    private fun transport_data(senderId: String,Product: String, Date: String, Price: String, Location: String){
        var refernce: DatabaseReference? = FirebaseDatabase.getInstance().getReference()
        val hashMap:HashMap<String,String> = HashMap()
        hashMap.put("sendId", senderId)
        hashMap.put("Product",Product)
        hashMap.put("Date",Date)
        hashMap.put("Location",Location)
        hashMap.put("Price",Price)
        refernce!!.child("Order").push().setValue(hashMap)
        refernce!!.child("History Order").push().setValue(hashMap)
        val intent = Intent(this@BuyActivity, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }

        fun getNow(): String {
            if (android.os.Build.VERSION.SDK_INT >= 24){
                return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
            }else{
                var tms = Calendar.getInstance()
                return tms.get(Calendar.YEAR).toString() + "-"+ tms.get(Calendar.MONTH).toString() + "-" + tms.get(Calendar.DAY_OF_MONTH).toString() + " " + tms.get(Calendar.HOUR_OF_DAY).toString() + ":" + tms.get(Calendar.MINUTE).toString() +":" + tms.get(Calendar.SECOND).toString() +"." + tms.get(Calendar.MILLISECOND).toString()
            }

        }

}