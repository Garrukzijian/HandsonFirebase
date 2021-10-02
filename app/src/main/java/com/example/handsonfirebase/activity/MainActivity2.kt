package com.example.handsonfirebase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.handsonfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity2 : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        Buy_button.setOnClickListener {
            val intent = Intent(this@MainActivity2, BuyActivity::class.java)
            intent.putExtra("UserId", firebaseUser!!.uid)
            startActivity(intent)
            finish()
        }
        history_button.setOnClickListener {
            val intent1 = Intent(this@MainActivity2, HistoryActivity::class.java)
            intent.putExtra("UserId", firebaseUser!!.uid)
            startActivity(intent1)
            finish()
        }
        take_order_button.setOnClickListener {
            val intent2 = Intent(this@MainActivity2, TakeOrderActivity::class.java)
            intent.putExtra("UserId", firebaseUser!!.uid)
            startActivity(intent2)
        }

    }
}