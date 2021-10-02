package com.example.handsonfirebase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.handsonfirebase.R
import com.example.handsonfirebase.model.History
import com.example.handsonfirebase.model.TakeOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    val history_list = ArrayList<History>()
    val Takeorder_list=ArrayList<TakeOrder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        imageBack_history.setOnClickListener {
            onBackPressed()
        }
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("History Order")
        val databaseReference2: DatabaseReference = FirebaseDatabase.getInstance().getReference("Take_Order")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                history_list.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val history = dataSnapShot.getValue(History::class.java)
                    val sendId = history!!.sendId
                    if (firebaseUser!!.uid.equals(sendId)) {
                        history_list.add(history!!)
                        History_recyclerView.layoutManager =
                            LinearLayoutManager(this@HistoryActivity)
                        History_recyclerView.adapter = MyAdapter(history_list)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    inner class MyAdapter(val historylist: List<History>) :
        RecyclerView.Adapter<MyviewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
            return MyviewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
            val history = historylist[position]
            holder.product.text = history.Product
            holder.price.text = history.Price
            holder.date.text = history.Date
            holder.location.text = history.Location
            }

        override fun getItemCount(): Int {
            return historylist.size
        }

    }
        inner class MyviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var product: TextView = itemView.Product_history
            var price: TextView = itemView.Price_history
            var date: TextView = itemView.Date_history
            var location: TextView = itemView.location_history
        }
    }
