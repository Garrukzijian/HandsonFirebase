package com.example.handsonfirebase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.handsonfirebase.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Buy_button.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        history_button.setOnClickListener {
            val intent1 = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent1)
        }
        take_order_button.setOnClickListener {
            val intent2 = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent2)
        }
    }
}